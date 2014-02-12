#/usr/bin/env/python2.7
#encoding=utf-8

from bs4 import BeautifulSoup

from crawler import Crawler
from utils import *

class ChinawutongCrawler(Crawler):
"""
Goods crawler for Chinawutong web 
中国物通网的货源信息在http://www.chinawutong.com/103.html
每页有8条货源信息，点击会有此货源的其他信息
对一条货源信息的提取既要从信息列表的粗略信息(get_item方法)，也要从此信息的详细页面中提取（get_details）
这两个方法都是对页面中静态信息的提取，如果页面有变化，两个方法也要进行修改
"""

    HOME_URL = "http://www.chinawutong.com/"
    HOST = "http://www.chinawutong.com/"

    def __init__(self):
        Crawler.__init__(self)

    def crawl(self, latest = True):
        url = self.HOST+"103.html"
        #取前10个列表，一个列表中有8个货源信息
        for i in range(1,10):
            #获得一页中的信息列表
            page = self.get(url, {"pid":i})
            #分析此列表页面
            data = self.get_items(page)
            for d in data:
                self.write_to_mongo(d)

    def get_items(self, page):
    """
    """
        soup = BeautifulSoup(page, fromEncoding="gb18030")
        table = soup.find(class_="mainall")
        data = []
        for tr in table.findAll("tr"):
            d = {}
            td = tr.find("td")
            divs = td.findAll("div")
            lis0 = divs[0].findAll("li")
            try:
                d["provider"] = lis0[1].span.a.text
            except:
                d["provider"] = ""
            href = lis0[0].span.a["href"]
            d["source_link"] = self.HOST+href
            d["source"] = "中国物通网"
            d["publish_time"] = divs[2].findAll("li")[0].find("span").nextSibling
            #因为详细信息页面里的说明在数字上有问题，因此detail从货源信息列表中提取
            d["detail"] = divs[3].findAll("span")[-1].nextSibling
            
            detail_page = self.get(d["source_link"])
            self.get_details(detail_page,d)

            strip_dict(d)
            print_dict(d)
            print "#################"

            data.append(d)

        return data

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
        d["contact"]["phone"] = trs[5].findAll("td")[3].text
        d["note"] = trs[6].findAll("td")[1].text

        return d


