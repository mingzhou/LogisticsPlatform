from bs4 import BeautifulSoup
from crawler import Crawler

class Crawler51yunli(Crawler):
    def __init__(self):
        Crawler.__init__(self)
        self.HOST = "http://www.51yunli.com"
        self.prefix = "/goods/0/0/"
        self.suffix = "/0"
        self.MAX_PAGE = 7

    def uniform(self, page):
        soup = BeautifulSoup(page, "html.parser")   # lxml
        div = soup.find(attrs={"class": "line_title1"})
        for ul in div.find_all("ul"):
            item = {"site": "51yunli"}
            item["url"] = self.HOST + ul.find("li").a.get("href")
            if item in self.data or self.exist(item):
                self.window -= 1
                continue
            request = self.url2request(item["url"])
            page = self.get(request)
            if page is None:
                continue
            soup = BeautifulSoup(page, "html.parser")   # lxml
            table = soup.find(attrs={"class": "line_title"}).find_all("table")[2]
            td = table.find_all("td")
            s = td[3].text.strip().split()  # 2014-11-09 23:10
            date = s[0].split('-')
            item["date"], item["deadline"] = self.lifetime(date)
            item["type"] = td[5].text.strip()
            item["vehicle"] = td[7].text.strip()
            item["length"] = td[9].text.strip()
            item["quality"] = td[11].text.strip()
            item["volume"] = td[13].text.strip()
            item["fee"] = td[15].text.strip()
            item["from"] = td[17].text.strip()
            item["to"] = td[19].text.strip()
            item["others"] = td[29].text.strip()
            self.data.append(self.good(item))
