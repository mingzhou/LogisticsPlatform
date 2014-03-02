#/usr/bin/env/python2.7
#encoding=utf-8

from bs4 import BeautifulSoup
import time

from crawler import Crawler
from utils import *

null = "null"
true = "true"
false = "false"

class QQ56Crawler(Crawler):
    """
    """

    HOME_URL = 'http://www.56qq.cn'
    HOST = 'http://www.56qq.cn'
    LOGIN_URL = ''
    QUERY_URL = HOST+'/logistics/message/query'
    MORE_URL = HOST+'/logistics/message/older'
    LASTEST_URL = HOST+'/logistics/message/latest'
 
    def __init__(self):
        Crawler.__init__(self)
        self.latestid = 0
        self.regions = self.get_regions_dict()

    def get_regions_dict(self):
        """
        56qq自己有一个省市id对应表，获得这个对应关系
        """
        url = "http://www.56qq.cn/pagelet/common/regions?v=20110513"
        return self.parse_response(self.get(url))

    def get_pids(self,page):
        pids = []
        soup = BeautifulSoup(page)
        lis = soup.find_all("li",class_ = "expandable")
        for li in lis:
            #print li['id'][-2:]
            pids.append(li['id'][-2:])
        return pids

    def each_crawl(self,url,params):
        res = self.post(url,params)
        result = self.parse_response(res)
        if self.no_more(result):
            return None
        return result

    def no_more(self,r):
        return True if len(r["content"]["msgs"]) == 0 else False

    def parse_response(self,res):
        return eval(res)

    def record_latestid(self,newid,fname):
        if long(newid)>long(self.latestid):
            print "newid:",newid
            print "latestid",self.latestid
            print "id changed"
            self.latestid = newid
            with open(fname,'w') as f:
                f.write(str(newid))

    def region(self, ID):
        """
        通过region编号获得地址汉字字符串
        s:编号字符串
        """
        text = ""
        prov = int(str(ID)[0:2])
        city = int(str(ID)[0:4])
        dist = ID #区

        for p in self.regions:
            if p["id"] == prov:
                text += p["name"]
                for c in p["children"]:
                    if c["id"] == city:
                        text += "-"
                        text += c["name"]
                        for d in c["children"]:
                            if d["id"] == ID:
                                text += "-"
                                text += d["name"]
        return text

class QQ56CarCrawler(QQ56Crawler):
    def __init__(self):
        QQ56Crawler.__init__(self)
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

    def crawl(self):
        pids = self.get_pids(self.open_home())

        for p in pids:
            self.first_params["pid"] = p
            result = self.each_crawl(self.QUERY_URL,self.first_params)
            if result:
                self.record_latestid(result["content"]["max"],self.fname)
            while result:
                self.write_to_mongo(self.info_format(result))
                time.sleep(self.T)
                self.more_params["pid"] = p
                self.more_params["idx"] = result["content"]["min"]
                result = self.each_crawl(self.MORE_URL,self.more_params)

    def info_format(self, data):
        pass

class QQ56GoodCrawler(QQ56Crawler):

    def __init__(self):
        QQ56Crawler.__init__(self)
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

    def crawl(self):
        pids = self.get_pids(self.open_home())

        for p in pids:
            self.first_params["pid"] = p
            result = self.each_crawl(self.QUERY_URL,self.first_params)
            if result:
                self.record_latestid(result["content"]["max"],self.fname)
            while result:
                self.write_to_mongo(self.info_format(result))
                time.sleep(self.T)
                self.more_params["pid"] = p
                self.more_params["idx"] = result["content"]["min"]
                result = self.each_crawl(self.MORE_URL,self.more_params)

    def info_format(self, data):
        """
        Format json data to our data format
        """
        #print_dict(data)
        l = []
        for m in data["content"]["msgs"]:
            d = {}
            d["s_addr"] = self.region(m["dep"])
            d["e_addr"] = self.region(m["dest"])
            d["title"] = ""
            d["provider"] = m["tpk"] if m.has_key("tpk") else "" 
            #d["goods"] = {} 
            #d["goods"]["name"] = 
            #d["goods"]["type"] = 
            #d["goods"]["weight"] = 
            d["publish_time"] = m["ct"]/1000 #timestamp
            #timestamp to datetime
            #print(datetime.datetime.fromtimestamp(int("1393137957")).strftime('%Y-%m-%d %H:%M:%S'))
            d["validity"] = ""
            d["detail"] = m["c"]
            d["contact"] = {}
            d["contact"]["name"] = m["udn"]
            d["contact"]["phone"] = m["un"]
            d["source"] = "物流QQ"
            d["source_link"] = "http://www.56qq.cn/"
            d["status"] = m["s"]#可能有被删除的信息
            print_dict(d)
            l.append(d)

        return l 


class LatestCrawler(QQ56Crawler):

    def __init__(self):
        QQ56Crawler.__init__(self)
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
            self.write_to_mongo(result,self.car_col)

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

    def info_format(self, data):
        pass

    def record_latestid(self,newid,fname):
        if long(newid)>long(self.latestid):
            print "newid:",newid
            print "latestid",self.latestid
            print "id changed"
            self.latestid = newid
            with open(fname,'w') as f:
                f.write(str(newid))
    
