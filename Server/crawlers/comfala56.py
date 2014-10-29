import urllib
from bs4 import BeautifulSoup
from crawler import Crawler

class CrawlerFala56(Crawler):
    def __init__(self):
        Crawler.__init__(self)
        self.HOST = "http://fala56.com/"
        self.prefix = "Views/Huoyuan/"
        self.suffix = "/GoodsLandList.aspx?area=-1"

    def generate_url(self, num_page):
        url = self.HOST + self.prefix + self.suffix
        data = {"__EVENTTARGET": "pager"}
        data["__EVENTARGUMENT"] = str(num_page)
        post = urllib.parse.urlencode(data).encode("utf8")
        request = urllib.request.Request(url, post)
        return request

    def uniform(self, page):
        soup = BeautifulSoup(page, "html.parser")   # lxml
        for div in soup.find_all(attrs = {"class": "cy_tab_content"}):
            item = {"site": "fala56"}
            td = div.find_all("span")
            item["from"] = td[0].text.strip()
            item["to"] = td[2].text.strip()
            item["url"] = self.HOST + self.prefix + td[-1].a.get("href")
            date = td[-2].text.strip().split('-')   # 2014-10-29
            item["date"], item["deadline"] = self.lifetime(date)
            item["description"] = str(td)
            if item in self.data or self.exist(item):
                self.window -= 1
                continue
            self.data.append(item)
