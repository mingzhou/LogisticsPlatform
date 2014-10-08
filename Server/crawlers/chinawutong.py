from bs4 import BeautifulSoup
from crawler import Crawler

class CrawlerChinawutong(Crawler):
    def __init__(self):
        Crawler.__init__(self)
        self.URL = "http://www.chinawutong.com/103.html"
        self.data = []
        self.goon = True

    def crawl(self):
        count = 1
        while self.goon:
            page = self.get(self.URL + "?", {"pid" : count})
            self.goon = self.uniform(page)
            count += 1
        return self.data

    def uniform(self, page):
        soup = BeautifulSoup(page, "html.parser")   # lxml
        table = soup.find(attrs={"class":"mainall"})
        for tr in table.find_all("tr"):
            item = {"site": "chinawutong"}
            td = tr.find("td")
            divs = td.find_all("div")
            ls = divs[0].find_all("li")
            s = ls[0].span.a.text   # src -> dst
            pos = s.find('→ ')
            item["from"] = s[:pos].strip()
            item["to"] = s[pos+1:].strip()
# contents = ls[-1].contents
# item["deadline"] = contents[-1].strip()    # 2014-09-30
            ls = divs[2].find_all("li")
            item["date"] = ls[0].contents[-1].strip()  # 10-07
            if self.find(item):
                return False
            item["description"] = td.text
            self.data.append(item)
        return True
