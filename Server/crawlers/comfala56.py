import urllib
from bs4 import BeautifulSoup
from crawler import Crawler

class CrawlerFala56(Crawler):
    def __init__(self):
        Crawler.__init__(self)
        self.HOST = "http://fala56.com"
        self.prefix = "/Views/Huoyuan"
        self.suffix = "/GoodsLandList.aspx?area=-1"

    def request(self, num_page):
        url = self.HOST + self.prefix + self.suffix
        values = {"__EVENTTARGET": "pager", "__EVENTARGUMENT": str(num_page)}
        data = urllib.parse.urlencode(values).encode("utf8")
        return urllib.request.Request(url, data)

    def uniform(self, page):
        soup = BeautifulSoup(page, "html.parser")   # lxml
        for div in soup.find_all(attrs = {"class": "cy_tab_content"}):
            item = {"site": "fala56"}
            td = div.find_all("span")
            item["url"] = self.HOST + self.prefix + "/" + td[-1].a.get("href")
            if item in self.data or self.exist(item):
                self.window -= 1
                continue
            request = self.url2request(item["url"])
            page = self.get(request)
            if page is None:
                continue
            soup = BeautifulSoup(page, "html.parser")   # lxml
            td = soup.find(attrs = {"class": "cyde_left"}).table.find_all("td")
            item["title"] = td[2].text.strip()
            item["type"] = td[4].text.strip()
            item["quality"] = td[6].text.strip()
            item["packing"] = td[8].text.strip()
            item["vehicle"] = td[10].text.strip()
            item["length"] = td[12].text.strip()
            item["from"] = td[14].text.strip()
            item["to"] = td[16].text.strip()
            item["attention"] = td[18].text.strip()
            date = td[22].text.strip().split('-')
            item["date"], item["deadline"] = self.lifetime(date)
            self.data.append(self.good(item))
