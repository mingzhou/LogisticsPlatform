import datetime
import logging
import urllib
import urllib.request
from mongotruck import MongoTruck

LIFETIME = 27
WINDOW_SIZE = 7
TIMEOUT = 77

logging.basicConfig(filename = "crawler.log", filemode = "a", 
        format = '%(asctime)s %(levelname)s: %(message)s', level = logging.INFO)

class Crawler():
    def __init__(self):
        self.database = MongoTruck()
        self.data = []
        self.window = WINDOW_SIZE
        self.HOST = ""
        self.prefix = ""
        self.suffix = ""
        self.MAX_PAGE = 47

    def crawl(self):
        count = 1
        while self.window > 0 and count < self.MAX_PAGE:
            page = self.get(self.request(count))
            if page is None:
                break
            self.uniform(page)
            count += 1
        logging.info("Successful to fetch %d items from %s", 
                len(self.data), self.HOST)
        return self.data

    def get(self, request):
        try:
            response = urllib.request.urlopen(request, timeout = TIMEOUT)
        except (urllib.error.URLError, socket.timeout):
            logging.warning("Failed to fetch: " + request.full_url)
            return self.get(request)
        page = response.read()
        try:
            page = page.decode("utf8")
        except UnicodeDecodeError:
            page = page.decode("gbk")
        return page

    def request(self, num_page):
        url = self.HOST + self.prefix + str(num_page) + self.suffix
        return self.url2request(url)

    def url2request(self, url):
        return urllib.request.Request(url)

    def uniform(self, page):
        pass

    def truck(self, item):
        keys = ['address', 'contact', 'date', 'datetime', 'deadline', 
                'fee', 'from', 'length', 'mobile', 'number', 'others', 
                'quality', 'site', 'telephone', 'to', 'url', 'vehicle']
        data = {key: "" for key in keys}
        for key in item:
            data[key] = item[key]
        return data

    def lifetime(self, begin, end = LIFETIME):
        date = self.list2date(begin)
        if type(end) is int or len(end) < 2:    # chinawutong ['长期货源']
            if type(end) is not int:
                end = LIFETIME
            deadline = date + datetime.timedelta(end)
        else:
            deadline = self.list2date(end)
        if date > self.today():
            date = self.today()
        if deadline < date:
            deadline = date + datetime.timedelta(LIFETIME)
        return date, deadline

    def list2date(self, date):
        today = self.today()
        date = [int(x) for x in ([today.year] + date)[-3:]]
        return today.replace(date[0], date[1], date[-1])

    def today(self):
        return datetime.datetime.combine(datetime.date.today(), datetime.time())

    def exist(self, obj):
        return self.database.find_one(obj) is not None
