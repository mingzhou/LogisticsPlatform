from bs4 import BeautifulSoup
from crawler import Crawler

class Crawler0256(Crawler):
    def __init__(self):
        Crawler.__init__(self)
        self.HOST = "http://www.0256.cn"
        self.prefix = "/goods/?PageIndex="

    def uniform(self, page):
        soup = BeautifulSoup(page, "html.parser")   # lxml
        table = soup.find(attrs = {"class": "yuan_lbk"}).table
        for tr in table.find_all("tr")[1:]:
            item = {"site": "0256"}
            td = tr.find_all("td")
            item["url"] = self.HOST + td[0].a.get("href")
            if item in self.data or self.exist(item):
                self.window -= 1
                continue
            request = self.url2request(item["url"])
            page = self.get(request)
            if page is None:
                continue
            soup = BeautifulSoup(page, "html.parser")   # lxml
            value = soup.find_all(attrs = {"class": "h_md"})
            item["title"] = value[0].text.strip()
            item["from"] = value[1].text.strip()
            item["to"] = value[2].text.strip()
            item["type"] = value[3].text.strip()
            item["length"] = value[5].text.strip()
            item["quality"] = value[6].text.strip()
            item["volume"] = value[7].text.strip()
            item["packing"] = value[8].text.strip()
            s = value[10].text.strip().split() # 2014-10-14 21:20
            date = s[0].split('-')
            delta = int(value[-1].text.strip()[:-1])
            item["date"], item["deadline"] = self.lifetime(date, delta)
            self.data.append(self.good(item))
