import datetime
from cn0256 import Crawler0256
from cn56110 import Crawler56110
from com51yunli import Crawler51yunli
from com8glw import Crawler8glw
from comchinawutong import CrawlerChinawutong
from comfala56 import CrawlerFala56
from net56888 import Crawler56888
from mongodb import MongoDB

database = MongoDB()
frequency = [[Crawler0256, Crawler51yunli], [Crawler56888, CrawlerFala56, 
          CrawlerChinawutong, Crawler56888, Crawler8glw, Crawler56110]]
minute = datetime.datetime.now().minute
crawlers = [item[minute % len(item)] for item in frequency]
for c in crawlers:
    data = c().crawl()
    for item in reversed(data):
        item["datetime"] = datetime.datetime.now()
        database.insert(item)
