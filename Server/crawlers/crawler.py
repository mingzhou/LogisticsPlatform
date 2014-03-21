import urllib
import cookiejar
import pymongo
from bs4 import BeautifulSoup
from mongo_writer import MongoWriter

class Crawler():
    def __init__(self):
        self.cookiejar = cookielib.CookieJar()
        self.opener = urllib2.build_opener(urllib2.HTTPCookieProcessor(self.cookiejar),urllib2.HTTPHandler(debuglevel = 0))
        self.opener.addheaders = [('User-agent','Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1)')]
        self.opener.addheaders.append(('content-type','application/json'))

        # connect to mongodb
        self.db = None

    def set_db(self, db):
        self.db = db

    def print_cookies(self):
        for cookie in self.cookiejar:
            print cookie.name
            print cookie.value
    
    def get(self, url, params = {}):
        url+='?'
        if len(params) > 0:
            for k,v in params.items():
                url+=(str(k)+'='+str(v)+'&')
            url = url[:-1]
        print "GET:",url
        request = urllib2.Request(url)
        return self.opener.open(request).read()

    def post(self, url, params):
        data = urllib.urlencode(params)
        response = self.opener.open(urllib2.Request(url, data))
        return response.read()

    def info_format(self):
        pass

    def write_to_mongo(self, data):
        self.db.insert(data)

    def crawl(self,latest = True):
        #虚函数
        pass
