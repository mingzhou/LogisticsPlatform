import datetime
from chinawutongcom import CrawlerChinawutong
from mongodb import MongoDB

database = MongoDB()
cs = []
cs.append(CrawlerChinawutong())

for c in cs:
    data = c.crawl()
    for item in reversed(data):
        item["datetime"] = datetime.datetime.now()
        database.insert(item)
