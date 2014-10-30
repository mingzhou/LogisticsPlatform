import datetime
from cn0256 import Crawler0256
from cn56110 import Crawler56110
from com51yunli import Crawler51yunli
from com8glw import Crawler8glw
from comchinawutong import CrawlerChinawutong
from comfala56 import Crawlerfala56
from net56888 import Crawler56888
from mongodb import MongoDB

database = MongoDB()
crawlers = []
crawlers.append(Crawler0256())
crawlers.append(Crawler56110())
crawlers.append(Crawler51yunli())
crawlers.append(Crawler8glw())
crawlers.append(CrawlerChinawutong())
crawlers.append(Crawlerfala56())
crawlers.append(Crawler56888())
N = len(crawlers)
minute = datetime.datetime.now().minute

data = crawlers[minute % N].crawl()
for item in reversed(data):
    item["datetime"] = datetime.datetime.now()
    database.insert(item)
