import datetime
from mongosupply import MongoSupply

database = MongoSupply()
today = datetime.datetime.combine(datetime.date.today(), datetime.time())
obj = {"deadline": {"$lt": today}}
database.remove(obj)
