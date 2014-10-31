from mongodb import MongoDB

class MongoUser(MongoDB):
    COLLECTION = "user"
    
    def __init__(self):
        MongoDB.__init__(self)
