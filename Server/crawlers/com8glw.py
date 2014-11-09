from bs4 import BeautifulSoup
from crawler import Crawler

class Crawler8glw(Crawler):
    def __init__(self):
        Crawler.__init__(self)
        self.HOST = "http://www.8glw.com"
        self.prefix = "/main_info.asp?id=1&page="

    def uniform(self, page):
        soup = BeautifulSoup(page, "html.parser")   # lxml
        for li in soup.find(attrs = {"class": "list"}).find_all("li"):
            item = {"site": "8glw"}
            item["url"] = self.HOST + "/" + li.a.get("href")
            if item in self.data or self.exist(item):
                self.window -= 1
                continue
            request = self.url2request(item["url"])
            page = self.get(request)
            if page is None:
                continue
            soup = BeautifulSoup(page, "html.parser")   # lxml
            tds = soup.find(attrs = {"class": "box"}).table.find_all("tr")
            item["type"] = tds[1].td.text.strip()
            item["attention"] = tds[2].td.text.strip()
            item["length"] = tds[3].td.text.strip()
            item["from"] = tds[4].td.text.strip()
            item["to"] = tds[5].td.text.strip()
            item["quality"] = tds[6].td.text.strip()
            item["contact"] = tds[9].td.text.strip()
            begin = tds[11].td.text.strip().split('-')
            end = tds[10].td.text.strip().split('-')
            item["date"], item["deadline"] = self.lifetime(begin, end)
            item["others"] = tds[13].td.text.strip()
            self.data.append(self.good(item))
