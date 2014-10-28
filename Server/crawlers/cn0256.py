from bs4 import BeautifulSoup
from crawler import Crawler

class Crawler0256(Crawler):
    def __init__(self):
        Crawler.__init__(self)
        self.HOST = "http://www.0256.cn/"
        self.prefix = "/goods/?PageIndex="

    def uniform(self, page):
        soup = BeautifulSoup(page, "html.parser")   # lxml
        table = soup.find(attrs = {"class": "yuan_lbk"}).table
        for tr in table.find_all("tr")[1:]:
            item = {"site": "0256"}
            td = tr.find_all("td")
            s = td[0].a.text.strip()    # 广东省佛山市→湖南省株洲市
            pos = s.find("→")
            item["from"] = s[:pos]
            item["to"] = s[pos + 1:]
            s = td[-1].text.strip().split() # 2014-10-14 21:20
            date = s[0].split('-')
            item["date"], item["deadline"] = self.lifetime(date, 3)
            item["description"] = tr.text
            if item in self.data or self.exist(item):
                self.window -= 1
                continue
            self.data.append(item)
