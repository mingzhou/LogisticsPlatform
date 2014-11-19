import tornado.httpserver
import tornado.ioloop
import tornado.options
import tornado.web
from mongosupply import MongoSupply
from tornado.options import define, options

database = MongoSupply()
define("port", default = 8000, help = "run on the given port", type = int)

class CityTopHandler(tornado.web.RequestHandler):
    def post(self):
        str_json = self.get_argument("data")
        dict_json = eval(str_json)
        dict_id = dict_json["id"]
        del dict_json["id"]
        list_json = [{key: {"$regex": dict_json[key]}} for key in dict_json]
        obj = {"$or": list_json}
        if dict_id == "":
            dict_id = "54503ce46a63436a5088db00"    # a very tiny ObjectId
        self.write(database.query(obj))
        self.add_header("new", database.newer(dict_id, obj))
        self.flush()

class LatestHandler(tornado.web.RequestHandler):
    def get(self):
        self.write(database.latest())
        self.flush()

    def post(self):
        str_json = self.get_argument("data")
        dict_json = eval(str_json)
        dict_id = dict_json["_id"]["$oid"]
        self.add_header("new", database.newer(dict_id))
        self.get()

class PreviousHandler(tornado.web.RequestHandler):
    def post(self):
        str_json = self.get_argument("data")
        dict_json = eval(str_json)
        dict_id = dict_json["_id"]["$oid"]
        self.write(database.previous(dict_id))
        self.flush()

class QueryHandler(tornado.web.RequestHandler):
    def post(self):
        str_json = self.get_argument("data")
        dict_json = eval(str_json)
        for key in dict_json:
            dict_json[key] = {"$regex": dict_json[key]}
        self.write(database.query(dict_json))
        self.flush()


tornado.options.parse_command_line()
app = tornado.web.Application(handlers = 
        [(r"/citytop", CityTopHandler),
        (r"/latest", LatestHandler), 
        (r"/previous", PreviousHandler),
        (r"/query", QueryHandler)])
http_server = tornado.httpserver.HTTPServer(app)
http_server.listen(options.port)
tornado.ioloop.IOLoop.instance().start()
