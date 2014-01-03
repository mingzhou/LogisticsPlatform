#!/usr/bin/env python2.7
#encoding=utf-8

import urllib
import urllib2
import cookielib
import time
from bs4 import BeautifulSoup

import csv
import cStringIO
import codecs

import pymongo

null = "null"
true = "true"
false = "false"

class Crawler:
    HOME_URL = 'http://www.56qq.cn'
    HOST = 'http://www.56qq.cn'
    LOGIN_URL = ''
    QUERY_URL = HOST+'/logistics/message/query'
    MORE_URL = HOST+'/logistics/message/older'
    LASTEST_URL = HOST+'/logistics/message/latest'
    USERNAME = ''
    PASSWORD = ''
   
    def __init__(self):
        self.cookiejar = cookielib.CookieJar()
        self.opener = urllib2.build_opener(urllib2.HTTPCookieProcessor(self.cookiejar),urllib2.HTTPHandler)
        self.opener.addheaders = [('User-agent','Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1)')]
        self.opener.addheaders = [('content-type','application/json')]
        self.writer = CSVWriter(open("car.csv","w"),["id","uid","c","t","s","ct","tt","cpt","uc","hd","hwd","gnd","tpk","dpn","as","rc","ri","dep","dest","cgt","w","u","p","pu","tw","ext","ut","udn","un","src"])
        self.writer.writeheader()

        self.latestid = 0

        dbclient = pymongo.MongoClient()
        self.db = dbclient["56db"]

    def print_cookies(self):
        for cookie in self.cookiejar:
            print cookie.name
            print cookie.value
    
    def open_home(self):
        return self.open_page(self.HOME_URL)

    def open_page(self,url):
        request = urllib2.Request(url)
        return self.opener.open(request).read()
                                  
    def login(self,username,password):
        pass

    def get_pids(self,page):
        pids = []
        soup = BeautifulSoup(page)
        lis = soup.find_all("li",class_ = "expandable")
        for li in lis:
            #print li['id'][-2:]
            pids.append(li['id'][-2:])
        return pids

    def each_crawl(self,url,params):
        request = urllib2.Request(url,urllib.urlencode(params))
        res = self.opener.open(request).read()
        result = self.parse_response(res)
        if self.no_more(result):
            return None
        return result

    def no_more(self,r):
        return True if len(r["content"]["msgs"]) == 0 else False

    def parse_response(self,res):
        return eval(res)

    def record(self,result):
        #for msg in result["content"]["msgs"]:
        #self.writer.writerows(result["content"]["msgs"])
        print result

    def record_to_db(self,result,col):
        for msg in result["content"]["msgs"]:
            self.db[col].insert(msg)

    def record_latestid(self,newid,fname):
        if long(newid)>long(self.latestid):
            print "newid:",newid
            print "latestid",self.latestid
            print "id changed"
            self.latestid = newid
            with open(fname,'w') as f:
                f.write(str(newid))

class CarCrawler(Crawler):
    def __init__(self):
        Crawler.__init__(self)
        self.first_params = {
        "pid":-1,
        "cid":-1,
        "t":"V",
        "fs":30
        }
        self.more_params = {
        "pid":-1,
        "cid":-1,
        "t":"V",
        "idx":0,
        "s":20
        }
        #self.OPEN_URL = self.HOST+"/cheku/#msgboard/list/v"
        self.fname = "car_id"
        self.collection = "cars"

    def crawl(self):
        pids = self.get_pids(self.open_home())

        for p in pids:
            self.first_params["pid"] = p
            result = self.each_crawl(self.QUERY_URL,self.first_params)
            if result:
                self.record_latestid(result["content"]["max"],self.fname)
            while result:
                self.record_to_db(result,self.collection)
                time.sleep(10)
                self.more_params["pid"] = p
                self.more_params["idx"] = result["content"]["min"]
                result = self.each_crawl(self.MORE_URL,self.more_params)


class GoodCrawler(Crawler):

    def __init__(self):
        Crawler.__init__(self)
        self.first_params = {
        "pid":-1,
        "cid":-1,
        "t":"C",
        "fs":30
        }
        self.more_params = {
        "pid":-1,
        "cid":-1,
        "t":"C",
        "idx":0,
        "s":20
        }
        self.fname = "good_id"
        self.collection = "goods"

    def crawl(self):
        pids = self.get_pids(self.open_home())

        for p in pids:
            self.first_params["pid"] = p
            result = self.each_crawl(self.QUERY_URL,self.first_params)
            if result:
                self.record_latestid(result["content"]["max"],self.fname)
            while result:
                self.record_to_db(result,self.collection)
                time.sleep(6)
                self.more_params["pid"] = p
                self.more_params["idx"] = result["content"]["min"]
                result = self.each_crawl(self.MORE_URL,self.more_params)

class LatestCrawler(Crawler):

    def __init__(self):
        Crawler.__init__(self)
        self.car_params = {
        "pid":-1,
        "cid":-1,
        "t":"V",
        "idx":0
        }
        self.good_params = {
        "pid":-1,
        "cid":-1,
        "t":"C",
        "idx":0
        }
        self.good_fname = "good_id"
        self.car_fname = "car_id"
        self.good_col = "goods"
        self.car_col = "cars"

    def read_latestid(self,fname):
        with open(fname) as f:
            return f.readline().strip()

    def car_crawl(self):
        self.latestid = self.read_latestid(self.car_fname)
        self.car_params["idx"] = self.latestid
        result = self.each_crawl(self.LASTEST_URL,self.car_params)
        if result:
            print "car new data..."
            self.record_latestid(result["content"]["max"],self.car_fname)
            #self.record(result)
            self.record_to_db(result,self.car_col)

    def good_crawl(self):
        self.latestid = self.read_latestid(self.car_fname)
        self.car_params["idx"] = self.latestid
        result = self.each_crawl(self.LASTEST_URL,self.good_params)
        if result:
            print "good new data..."
            self.record_latestid(result["content"]["max"],self.good_fname)
            #self.record(result)
            self.record_to_db(result,self.good_col)

    def crawl(self):
        while True:
            self.good_crawl()
            time.sleep(10)
            self.car_crawl()
            time.sleep(120)

class CSVWriter:
    def __init__(self,f,fieldnames,dialect=csv.excel,encoding="utf-8",**kwds):
        self.queue = cStringIO.StringIO()
        self.writer = csv.DictWriter(self.queue, fieldnames, dialect=dialect, **kwds)
        self.stream = f
        self.encoder = codecs.getincrementalencoder(encoding)()

    def writerow(self, D):
        #for k in D.keys():
        #    D[k] = unicode(D[k]).encode("utf-8")
        self.writer.writerow(D)
            #self.writer.writerow({k:D[k].encode("utf-8")})
        # Fetch UTF-8 output from the queue ...
        data = self.queue.getvalue()
        data = data.decode("utf-8")
        # ... and reencode it into the target encoding
        data = self.encoder.encode(data)
        # write to the target stream
        self.stream.write(data)
        # empty queue
        self.queue.truncate(0)

    def writerows(self,rows):
        for D in rows:
            self.writerow(D)

    def writeheader(self):
        self.writer.writeheader()

if __name__=="__main__":
    #c = CarCrawler()
    #c.crawl()
    #c = GoodCrawler()
    #c.crawl()
    c = LatestCrawler()
    c.crawl()
