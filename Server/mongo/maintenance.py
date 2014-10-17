import datetime
from mongodb import MongoDB

database = MongoDB()
today = datetime.datetime.combine(datetime.date.today(), datetime.time())
obj = {"deadline": {"$lt": today}}
database.remove(obj)
