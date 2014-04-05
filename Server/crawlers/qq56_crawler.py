from bs4 import BeautifulSoup
from crawler import Crawler
from hashlib import sha512
from utils import *

class QQ56Crawler(Crawler):

    HOME_URL = "http://www.56qq.cn"
    HOST = "http://www.56qq.cn"
 
    def __init__(self):
        Crawler.__init__(self)

    def get_item(self, page):
        soup = BeautifulSoup(page)
        table = soup.find_all("div", attrs={"class":"entry"})
        data = []

        for entry in table:
            d = {}
            d["source site"] = "56QQ"
            s = entry.find(attrs={"class":"entry_date"}).span.text
            d["publish date"] = s

            s = entry.find(attrs={"class":"entry_text"})
            others = s.find("b")
            d["others"] = str(others)[3:-4]
            s = str(s.find("strong"))
            pos = s.find('-')
            d["from"] = s[8:pos]
            d["to"] = s[pos+1:-9]
            
            if self.find(d):
                return data
            data.append(d)

        return data

    def crawl(self):
        page = self.get(self.HOME_URL)
        data = self.get_item(page)

        for d in data:
            self.write_to_mongo(d)
        
        print("FIN")
