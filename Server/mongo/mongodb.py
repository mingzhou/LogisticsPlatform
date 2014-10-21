import json
import pymongo
from bson import json_util

class MongoDB():
    DB_HOST = "spider"
    DATABASE = "demo"
    COLLECTION = "supply"
    
    def __init__(self):
        client = pymongo.MongoClient(self.DB_HOST)
        database = client[self.DATABASE]
        self.collection = database[self.COLLECTION]

    def insert(self, data):
        self.collection.insert(data) 

    def find(self, obj):
        return self.collection.find(obj)

    def find_one(self, obj):
        return self.collection.find_one(obj)

    def remove(self, obj):
        return self.collection.remove(obj)

    def last(self, count = 10):
        data = []
        top = self.find(None).sort("_id", pymongo.DESCENDING).limit(count)
        for item in top:
# del item["_id"]
            del item["description"]
# del item["datetime"]
            del item["date"]
            del item["deadline"]
            data.append(item)
        return json.dumps(data, default = json_util.default)
