from bs4 import BeautifulSoup
from crawler import Crawler

class CrawlerChinawutong(Crawler):
    def __init__(self):
        Crawler.__init__(self)
        self.HOST = "http://www.chinawutong.com"
        self.prefix = "/103.html?pid="

    def uniform(self, page):
        soup = BeautifulSoup(page, "html.parser")   # lxml
        table = soup.find(attrs = {"class": "mainall"})
        for tr in table.find_all("tr"):
            item = {"site": "chinawutong"}
            ls = tr.find("td").find("div").find("li")
            item["url"] = self.HOST + ls.span.a.get("href")
            if item in self.data or self.exist(item):
                self.window -= 1
                continue
            request = self.url2request(item["url"])
            page = self.get(request)
            if page is None:
                continue
            soup = BeautifulSoup(page, "html.parser")   # lxml
            td = soup.find(attrs = {"class": "infol"}).table.find_all("td")
            item["from"] = td[3].text.strip()
            item["to"] = td[5].text.strip()
            item["title"] = td[7].text.strip()
            item["quality"] = td[9].text.strip()
            item["type"] = td[11].text.strip()
            item["volume"] = td[13].text.strip()
            item["vehicle"] = td[15].text.strip()
            item["attention"] = td[23].text.strip()
            item["others"] = td[25].text.strip()
            deadline = td[19].text.strip().split("-")
            date = soup.find(attrs = {"class": "lll_z"}).i.text.strip().split()[0].split("/")
            item["date"], item["deadline"] = self.lifetime(date, deadline)
            self.data.append(self.good(item))
