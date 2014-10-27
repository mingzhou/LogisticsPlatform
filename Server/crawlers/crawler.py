import datetime
import urllib
import urllib.request
from mongodb import MongoDB

LIFETIME = 27
MAX_PAGE = 77
WINDOW_SIZE = 7

class Crawler():
    def __init__(self):
        self.database = MongoDB()
        self.data = []
        self.window = WINDOW_SIZE
        self.URL = ""
        self.suffix = ""

    def crawl(self):
        count = 1
        while self.window > 0 and count < MAX_PAGE:
            page = self.get(self.generate_url(count))
            self.uniform(page)
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

    def lifetime(self, begin, end = LIFETIME):
        today = datetime.datetime.combine(
                datetime.date.today(), datetime.time())
        if len(begin) == 2:
            begin.insert(0, today.year)
        date = today.replace(int(begin[0]), int(begin[1]), int(begin[-1]))
        if type(end) is int or len(end) < 2:
            if type(end) is not int:
                end = LIFETIME
            deadline = date + datetime.timedelta(end)
        else:
            if len(end) == 2:
                end.insert(0, today.year)
            deadline = today.replace(int(end[0]), int(end[1]), int(end[-1]))
            if deadline <= date:
                deadline = date + datetime.timedelta(LIFETIME)
        return date, deadline

    def exist(self, obj):
        return self.database.find_one(obj) is not None
