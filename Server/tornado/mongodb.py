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
        return self.collection.find(obj).count()

    def last(self):
        top = self.collection.find().sort("_id", pymongo.DESCENDING).limit(1)
        for item in top:
            del item["description"]
            del item["_id"]
# del item["datetime"]
            del item["date"]
            del item["deadline"]
            return json.dumps(item, default = json_util.default)
