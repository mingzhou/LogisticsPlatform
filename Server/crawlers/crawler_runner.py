import datetime
from com51yunli import Crawler51yunli
from comchinawutong import CrawlerChinawutong
from net56888 import Crawler56888
from mongodb import MongoDB

database = MongoDB()
cs = []
cs.append(Crawler51yunli())
cs.append(CrawlerChinawutong())
cs.append(Crawler56888())

for c in cs:
    data = c.crawl()
    for item in reversed(data):
        item["datetime"] = datetime.datetime.now()
        database.insert(item)
