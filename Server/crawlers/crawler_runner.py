from mongo_writer import MongoWriter
from cwt_crawler import ChinawutongCrawler
from qq56_crawler import QQ56Crawler

db = MongoWriter()
cs = []
cs.append(ChinawutongCrawler())
cs.append(QQ56Crawler())

for c in cs:
    c.set_db(db)
    c.crawl()
