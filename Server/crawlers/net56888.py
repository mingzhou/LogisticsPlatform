import datetime
from bs4 import BeautifulSoup
from crawler import Crawler

class Crawler56888(Crawler):
    def __init__(self):
        Crawler.__init__(self)
        self.URL = "http://wb.56888.net/OutSourceList.aspx?tendertype=4&"
        self.data = []
        self.goon = True

    def crawl(self):
        count = 1
        while self.goon:
            page = self.get(self.URL,  {"p" : count})
            self.goon = self.uniform(page)
            count += 1
        return self.data

    def uniform(self, page):
        soup = BeautifulSoup(page, "html.parser")
        table = soup.table.find("table")
        for table in table.find_all("table"):
            item = {"site": "56888"}
            s = table.find("span").text
            pos = s.find("->")
            item["from"] = s[:pos].strip()
            item["to"] = s[pos+2:].strip()
            s = table.find_all("td")[4].text    # 10-08至11-07
            pos = s.find("至")
            date = s[:pos].strip().split('-')
            today = datetime.datetime.combine(
                    datetime.date.today(), datetime.time())
            item["date"] = today.replace(
                    today.year, int(date[0]), int(date[-1]))
            date = s[pos+1:].strip().split('-')
            item["deadline"] = today.replace(
                    today.year, int(date[0]), int(date[-1]))
            if self.find(item):
                return False
            item["description"] = table.text
            self.data.append(item)
        return True
