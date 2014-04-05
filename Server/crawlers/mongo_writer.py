from pymongo import MongoClient


class MongoWriter():
    DB_NAME = "tsinghua"
    COLLECTION = "goods"

    def __init__(self, collection = "goods"):
        client = MongoClient()
        db = client[self.DB_NAME]
        self.collection = db[collection]

    def insert(self, data):
        self.collection.insert(data) 

    def find(self, obj):
        return self.collection.find(obj).count()
