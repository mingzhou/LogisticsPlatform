import datetime
from net56888 import Crawler56888
from chinawutong import CrawlerChinawutong
from mongodb import MongoDB

database = MongoDB()
cs = []
# cs.append(CrawlerChinawutong())
cs.append(Crawler56888())

for c in cs:
    data = c.crawl()
    for item in reversed(data):
        item["datetime"] = datetime.datetime.now()
        database.insert(item)
