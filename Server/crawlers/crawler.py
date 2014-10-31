import datetime
import logging
import urllib
import urllib.request
from mongosupply import MongoSupply

LIFETIME = 27
MAX_PAGE = 47
WINDOW_SIZE = 7
TIMEOUT = 17

logging.basicConfig(filename = "crawler.log", filemode = "a", 
        format = '%(asctime)s %(levelname)s: %(message)s', level = logging.INFO)

class Crawler():
    def __init__(self):
        self.database = MongoSupply()
        self.data = []
        self.window = WINDOW_SIZE
        self.HOST = ""
        self.prefix = ""
        self.suffix = ""

    def crawl(self):
        count = 1
        while self.window > 0 and count < MAX_PAGE:
            page = self.get(self.generate_url(count))
            if page is None:
                break
            self.uniform(page)
            count += 1
        logging.info("Successful to fetch %d items from %s", 
                len(self.data), self.HOST)
        return self.data

    def generate_url(self, num_page):
        return self.HOST + self.prefix + str(num_page) + self.suffix

    def get(self, url):
        try:
            response = urllib.request.urlopen(url, timeout = TIMEOUT)
        except urllib.error.URLError:
            logging.warning("Failed to fetch: " + url)
            return None
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
