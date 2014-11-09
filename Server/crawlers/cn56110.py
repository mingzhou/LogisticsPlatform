from bs4 import BeautifulSoup
from crawler import Crawler

class Crawler56110(Crawler):
    def __init__(self):
        Crawler.__init__(self)
        self.HOST = "http://56110.cn"
        self.suffix = "/Huo/list.html"

    def request(self, num_page):
        url = self.HOST + self.suffix
        return self.url2request(url)

    def uniform(self, page):
        soup = BeautifulSoup(page, "html.parser")   # lxml
        table = soup.find(attrs = {"class": "pager_box"})
        a = table.find("a", style = "float:right;")
        self.suffix = a.get("href")
        table = soup.find(attrs = {"class": "content_con"})
        for ul in table.find_all("ul"):
            item = {"site": "56110"}
            li = ul.find_all("li")
            item["url"] = self.HOST + li[0].a.get("href")
            if item in self.data or self.exist(item):
                self.window -= 1
                continue
            s = li[0].a.text  # 河南洛阳栾川县发货到山东淄博淄川区货源信息
            pos = s.find("发货到")
            item["from"] = s[ : pos].strip()
            item["to"] = s[pos + 3 : -4].strip()
            date = li[-1].text.strip().split('-')
            item["date"], item["deadline"] = self.lifetime(date)
            item["others"] = li[0].a.get("title")
            self.data.append(self.good(item))
