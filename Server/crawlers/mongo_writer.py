from pymongo import MongoClient

"""
Write data crawled from website to Mongodb
Data Format as json:
{
"p_s_id":int,
"c_s_id":int,
"p_e_id":int,
"c_e_id":int,
"title" :"value",
"provider":"value",
"goods":{
    "name":"value",
    "type":"",
    "weight":"",
    "vol":""
    },
"tran_type":"",
"v_length" :"",
"note"     :"",
"publish_time":"",
"validity" :"",
"detail"   :"",
"contact":{
    "name":"",
    "phone":"",
    "email":""
    },
"source"   :"",
"source_link":""
}
"""

class MongoWriter():
    DB_NAME = "tsinghua"
    COLLECTION = "goods"

    def __init__(self, collection = "goods"):
        client = MongoClient()
        db = client[self.DB_NAME]
        self.collection = db[collection]

    def insert(self, data):
        self.collection.insert(data) 
