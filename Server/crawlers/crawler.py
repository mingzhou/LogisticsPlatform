import urllib
import urllib.request
from mongodb import MongoDB

class Crawler():
    def __init__(self):
        self.database = MongoDB()
        self.data = []
        self.goon = True

    def get(self, url, params = {}):
        url += urllib.parse.urlencode(params)
        response = urllib.request.urlopen(url)
        page = response.read()
        try:
            page = page.decode("utf8")
        except UnicodeDecodeError:
            page = page.decode("gbk")
        return page

    def crawl(self):
        pass

    def uniform(self, page):
        pass

    def find(self, obj):
        return self.database.find(obj)
