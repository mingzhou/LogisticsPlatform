from bs4 import BeautifulSoup
from crawler import Crawler

class CrawlerChinawutong(Crawler):
    def __init__(self):
        Crawler.__init__(self)
        self.URL = "http://www.chinawutong.com/103.html?pid="

    def uniform(self, page):
        soup = BeautifulSoup(page, "html.parser")   # lxml
        table = soup.find(attrs={"class": "mainall"})
        for tr in table.find_all("tr"):
            item = {"site": "chinawutong"}
            td = tr.find("td")
            divs = td.find_all("div")
            ls = divs[0].find_all("li")
            s = ls[0].span.a.text   # src -> dst
            pos = s.find('→ ')
            item["from"] = s[:pos].strip()
            item["to"] = s[pos+1:].strip()
            contents = ls[-1].contents
            deadline = contents[-1].strip().split('-')    # 2014-09-30
            ls = divs[2].find_all("li")
            date = ls[0].contents[-1].strip().split('-')  # 10-07
            item["date"], item["deadline"] = self.lifetime(date, deadline)
            item["description"] = td.text
            if item in self.data or self.exist(item):
                self.window -= 1
                continue
            self.data.append(item)
