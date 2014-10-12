import urllib
import urllib.request
from mongodb import MongoDB

class Crawler():
    def __init__(self):
        self.database = MongoDB()
        self.data = []
        self.goon = True
        self.URL = ""
        self.suffix = ""

    def crawl(self):
        count = 1
        while self.goon and count < 77:
            page = self.get(self.generate_url(count))
            self.goon = self.uniform(page)
            count += 1
        return self.data

    def generate_url(self, num_page):
        return self.URL +  str(num_page) + self.suffix

    def get(self, url):
        response = urllib.request.urlopen(url)
        page = response.read()
        try:
            page = page.decode("utf8")
        except UnicodeDecodeError:
            page = page.decode("gbk")
        return page

    def uniform(self, page):
        pass

    def find(self, obj):
        return self.database.find(obj)
