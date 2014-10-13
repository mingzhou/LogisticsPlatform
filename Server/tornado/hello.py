import tornado.httpserver
import tornado.ioloop
import tornado.options
import tornado.web
from mongodb import MongoDB
from tornado.options import define, options

database = MongoDB()
define("port", default=8000, help="run on the given port", type=int)

class IndexHandler(tornado.web.RequestHandler):
    def get(self):
        self.write(database.last())

tornado.options.parse_command_line()
app = tornado.web.Application(handlers=[(r"/", IndexHandler)])
http_server = tornado.httpserver.HTTPServer(app)
http_server.listen(options.port)
tornado.ioloop.IOLoop.instance().start()
