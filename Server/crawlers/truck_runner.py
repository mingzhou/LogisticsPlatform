import datetime
from com51yunli_truck import Crawler51yunli
from mongotruck import MongoTruck

database = MongoTruck()
crawlers = [Crawler51yunli]
for c in crawlers:
    data = c().crawl()
    for item in reversed(data):
        item["datetime"] = datetime.datetime.now()
        database.insert(item)
