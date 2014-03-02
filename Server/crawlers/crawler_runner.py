#/usr/bin/env/python2.7
#encoding=utf-8

from cwt_crawler import ChinawutongCrawler
from qq56_crawler import QQ56GoodCrawler

from mongo_writer import MongoWriter

db = MongoWriter()
cs = []
#每添加一个网站就生成一个实例添加到cs里，新的网站爬虫要复写crawl()函数
#cs.append(ChinawutongCrawler())
cs.append(QQ56GoodCrawler())
for c in cs:
    c.set_db(db)
    c.crawl()

