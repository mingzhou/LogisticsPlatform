import tornado.httpserver
import tornado.ioloop
import tornado.options
import tornado.web
from mongouser import MongoUser
from tornado.options import define, options

SUCCESS = 0
FAIL = 1
database = MongoUser()
define("port", default = 8000, help = "run on the given port", type = int)

class LoginHandler(tornado.web.RequestHandler):
    def post(self):
        str_json = self.get_argument("data")
        dict_json = eval(str_json)
        obj = database.find_one(dict_json)
        if obj:
            self.add_header("new", SUCCESS)
            self.write(obj["user"])
        self.flush()

class SignupHandler(tornado.web.RequestHandler):
    def post(self):
        str_json = self.get_argument("data")
        dict_json = eval(str_json)
        status_code = 0
        info = ["user", "phone"]
        for i in range(len(info)):
            obj = {info[i] : dict_json[info[i]]}
            if database.find_one(obj):
                status_code += 1 << i
        if status_code == 0:
            database.insert(dict_json)
        self.add_header("new", status_code)
        self.flush()

class ResetHandler(tornado.web.RequestHandler):
    def post(self):
        str_json = self.get_argument("data")
        dict_json = eval(str_json)
        info = ["user", "phone"]
        obj = {key : dict_json[key] for key in info}
        if database.update(obj, {"$set": dict_json}):
            self.add_header("0", SUCCESS)
        self.flush()


tornado.options.parse_command_line()
app = tornado.web.Application(handlers = 
        [(r"/login", LoginHandler),
        (r"/signup", SignupHandler),
        (r"/reset", ResetHandler)])
http_server = tornado.httpserver.HTTPServer(app)
http_server.listen(options.port)
tornado.ioloop.IOLoop.instance().start()
