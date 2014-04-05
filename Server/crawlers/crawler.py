import pymongo
import urllib
import urllib.request
from http.cookiejar import CookieJar
from bs4 import BeautifulSoup
from mongo_writer import MongoWriter

class Crawler():
    def __init__(self):
        self.cookiejar = CookieJar()
        self.opener = urllib.request.build_opener(urllib.request.HTTPCookieProcessor(self.cookiejar), urllib.request.HTTPHandler(debuglevel = 0))
        self.opener.addheaders = [("User-agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1)")]
        self.opener.addheaders.append(("content-type", "application/json"))

        self.db = None

    def set_db(self, db):
        self.db = db

    def print_cookies(self):
        for cookie in self.cookiejar:
            print(cookie.name, cookie.value)
    
    def get(self, url, params = {}):
        url = url + urllib.parse.urlencode(params)
        print("GET:", url)

        request = urllib.request.urlopen(url)
        page = request.read()

        try:
            page = page.decode("utf8")
        except UnicodeDecodeError:
            page = page.decode("gbk")

        return page                 #   return self.opener.open(request).read()

    def post(self, url, params):
        data = urllib.urlencode(params)
        response = self.opener.open(urllib.request.Request(url, data))
        return response.read()

    def info_format(self):
        pass

    def write_to_mongo(self, data):
        self.db.insert(data)

    def crawl(self):
        pass

    def find(self, obj):
        return self.db.find(obj)
