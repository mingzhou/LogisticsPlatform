import datetime
from bs4 import BeautifulSoup
from crawler import Crawler

class Crawler51yunli(Crawler):
    def __init__(self):
        Crawler.__init__(self)
        self.URL = "http://www.51yunli.com/goods/0/0/"
        self.suffix = "/0"

    def uniform(self, page):
        soup = BeautifulSoup(page, "html.parser")   # lxml
        div = soup.find(attrs={"class": "line_title1"})
        for ul in div.find_all("ul"):
            item = {"site": "51yunli"}
            s = ul.find_all("strong")
            item["from"] = s[0].text
            item["to"] = s[1].text
            date = ul.find_all("span")[-1].text.strip().split('-')  # 2014-10-12
            today = datetime.datetime.combine(
                    datetime.date.today(), datetime.time())
            item["date"] = today.replace(
                    int(date[0]), int(date[1]), int(date[-1]))
            item["deadline"] = item["date"] + datetime.timedelta(27)
            item["description"] = ul.text
            if item in self.data or self.find(item):
                return False
            self.data.append(item)
        return True
