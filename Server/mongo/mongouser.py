from mongodb import MongoDB

class MongoUser(MongoDB):
    COLLECTION = "user"
    
    def __init__(self):
        MongoDB.__init__(self)

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
