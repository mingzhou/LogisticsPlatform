import datetime
from cn0256 import Crawler0256
from com51yunli import Crawler51yunli
from comchinawutong import CrawlerChinawutong
from net56888 import Crawler56888
from mongodb import MongoDB

database = MongoDB()
crawlers = []
crawlers.append(Crawler0256())
crawlers.append(Crawler51yunli())
crawlers.append(Crawler56888())
crawlers.append(CrawlerChinawutong())
crawlers.append(Crawler51yunli())
N = len(crawlers)
minute = datetime.datetime.now().minute

data = crawlers[minute % N].crawl()
for item in reversed(data):
    item["datetime"] = datetime.datetime.now()
    database.insert(item)
