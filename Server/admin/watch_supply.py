import datetime
from mongosupply import MongoSupply

database = MongoSupply()
today = datetime.date.today()
time = datetime.time()
end = datetime.datetime.combine(today, time)
begin = end - datetime.timedelta(weeks = 1)

while begin < end:
    next_hour = begin + datetime.timedelta(hours = 1)
    list_json = [{"datetime": {"$gt": begin}}, {"datetime": {"$lt": next_hour}}]
    obj = {"$and": list_json}
    print(begin, next_hour.time(), database.find(obj).count())
    begin = next_hour
