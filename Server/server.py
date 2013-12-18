#! /usr/bin/python2.7

import tornado.httpserver
import tornado.ioloop
import tornado.web
from tornado.web import StaticFileHandler

from tornado.options import define, options

define("port", default=12345, help="run on the given port", type = int)

class Application(tornado.web.Application):
    def __init__(self):
        settings = dict(
            title = u'',

        )
