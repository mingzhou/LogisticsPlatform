#/usr/bin/env/python2.7
#encoding=utf-8

from bs4 import BeautifulSoup

from Crawler import Crawler
from utils import *

class ChinawutongCrawler(Crawler):

    HOME_URL = "http://www.chinawutong.com/"
    HOST = "http://www.chinawutong.com/"

    #DBNAME = ""
    
    def __init__(self):
        Crawler.__init__(self)

    def crawl(self, latest = True):
        url = self.HOST+"103.html"
        for i in range(1,10):
            page = self.get(url, {"pid":i})
            data = self.get_items(page)
            self.write_to_mongo(data)

    def get_items(self, page):
        soup = BeautifulSoup(page, fromEncoding="gb18030")
        table = soup.find(class_="mainall")
        data = []
        for tr in table.findAll("tr"):
            d = {}
            td = tr.find("td")
            divs = td.findAll("div")
            lis0 = divs[0].findAll("li")
            d["provider"] = lis0[1].span.a.text
            href = lis0[0].span.a["href"]
            d["source_link"] = self.HOST+href
            d["source"] = "中国物通网"
            d["publish_time"] = divs[2].findAll("li")[0].find("span").nextSibling
            d["detail"] = divs[3].findAll("span")[-1].nextSibling
            
            detail_page = self.get(d["source_link"])
            self.get_details(detail_page,d)

            strip_dict(d)
            print_dict(d)

    def get_details(self, page, d):
        soup = BeautifulSoup(page, fromEncoding="gb18030")
        info = soup.find("div", class_="zxinfo mt10")
        d["title"] = info.find(class_="zxinfo_t").h1.text
        table = info.find("table", class_="guoneihuo")
        inner_table = table.findAll("tr")[1].find("table")
        trs = inner_table.findAll("tr")
        if not d.has_key("goods"):
            d["goods"] = {}
        if not d.has_key("contact"):
            d["contact"] = {}
        d["goods"]["name"] = trs[2].findAll("td")[1].font.text
        d["goods"]["weight"] = trs[2].findAll("td")[3].font.text
        d["goods"]["type"] = trs[3].findAll("td")[1].text
        d["goods"]["vol"] = trs[3].findAll("td")[3].text
        d["tran_type"] = trs[4].findAll("td")[1].text
        d["contact"]["name"] = trs[4].findAll("td")[3].text
        d["validity"] = trs[5].findAll("td")[1].text
        d["contact"]["name"] = trs[5].findAll("td")[3].text
        d["note"] = trs[6].findAll("td")[1].text

        return d





