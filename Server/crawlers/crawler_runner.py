from mongo_writer import MongoWriter
from cwt_crawler import ChinawutongCrawler
#from qq56_crawler import QQ56GoodCrawler

db = MongoWriter()
cs = []
cs.append(ChinawutongCrawler())
#cs.append(QQ56GoodCrawler())
for c in cs:
    c.set_db(db)
    c.crawl()
