import tornado.httpserver
import tornado.ioloop
import tornado.options
import tornado.web
from mongotruck import MongoTruck
from tornado.options import define, options

database = MongoTruck()
define("port", default = 8000, help = "run on the given port", type = int)

class TruckTopHandler(tornado.web.RequestHandler):
    def get(self):
        self.write(database.latest())
        self.flush()


tornado.options.parse_command_line()
app = tornado.web.Application(handlers = 
        [(r"/trucktop", TruckTopHandler)])
http_server = tornado.httpserver.HTTPServer(app)
http_server.listen(options.port)
tornado.ioloop.IOLoop.instance().start()
