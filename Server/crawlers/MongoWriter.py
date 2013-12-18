#/usr/bin/env/python2.7
#encoding=utf-8

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
    def __init__(self):
        pass
