import json
from bson import json_util
from bson.objectid import ObjectId
from mongodb import MongoDB
from pymongo import DESCENDING

NUM_RETURN = 10

class MongoTruck(MongoDB):
    COLLECTION = "truck"
    
    def __init__(self):
        MongoDB.__init__(self)

    def latest(self):
        return self.find_descending(None)

    def previous(self, obj_id):
        obj = {"_id": {"$lt": ObjectId(obj_id)}}
        return self.find_descending(obj)

    def newer(self, obj_id, obj = {}):
        obj.update({"_id": {"$gt": ObjectId(obj_id)}})
        new = self.find(obj)
        if new is not None:
            new = new.count()
        else:
            new = 0
        return new

    def query(self, obj):
        return self.find_descending(obj)

    def find_descending(self, obj = None, count = NUM_RETURN):
        order = [("date", DESCENDING), ("datetime", DESCENDING)]
        top = self.find(obj).sort(order).limit(count)
        data = [item for item in top]
        return json.dumps(data, default = json_util.default)
