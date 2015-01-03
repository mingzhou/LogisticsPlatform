from bs4 import BeautifulSoup
from crawler_truck import Crawler

class Crawler51yunli(Crawler):
    def __init__(self):
        Crawler.__init__(self)
        self.HOST = "http://www.51yunli.com"
        self.prefix = "/truck/0/0/0/"
        self.MAX_PAGE = 7

    def uniform(self, page):
        soup = BeautifulSoup(page, "html.parser")   # lxml
        div = soup.find(attrs={"class": "line_title1"})
        for li in div.find_all("li"):
            item = {"site": "51yunli"}
            item["url"] = self.HOST + li.a.get("href")
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
            s = td[3].text.strip().split()  # 2015/01/04 10:25:04
            date = s[0].split('/')
            s = td[9].text.strip()   # 2015-01-04
            deadline = s.split('-')
            item["date"], item["deadline"] = self.lifetime(date, deadline)
            info = str(soup.find(attrs={"id": "latlongInfo"}))[46:-4]
            info = eval(info.replace("null", "''"))
            item["from"] = info["StartName"]
            item["to"] = info["DestName"]
            item["quality"] = info["Load"]
            item["vehicle"] = td[13].text.strip()
            item["length"] = info["Length"]
            item["number"] = info["LicensePlate"]
            item["address"] = info["LocationInfo"]
            item["fee"] = td[23].text.strip()
            item["others"] = td[25].text.strip()
            item["mobile"] = info["Phone"]
            item["telephone"] = info["Tel"]
            item["contact"] = info["Contact"]
            self.data.append(self.truck(item))
