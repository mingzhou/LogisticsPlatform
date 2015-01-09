from pymongo import MongoClient, ReadPreference

class MongoDB():
    DB_HOST = "spider"
    DATABASE = "demo"
    COLLECTION = "NIL"

    def __init__(self):
        client = MongoClient(self.DB_HOST, read_preference = ReadPreference.SECONDARY)
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

    def update(self, spec, document):
        return self.collection.update(spec, document)["updatedExisting"]
