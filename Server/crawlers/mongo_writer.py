from pymongo import MongoClient

class MongoWriter():
    DB_HOST = "spider"
    DATABASE = "demo"
    COLLECTION = "supply"
    
    def __init__(self):
        client = MongoClient(self.DB_HOST)
        db = client[self.DATABASE]
        self.collection = db[self.COLLECTION]

    def insert(self, data):
        self.collection.insert(data) 

    def find(self, obj):
        return self.collection.find(obj).count()
