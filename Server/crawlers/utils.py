#/usr/bin/env/python2.7
#encoding=utf-8

def print_dict(d):
    for k,v in d.items():
        if type(v) is dict:
            for k1,v1 in v.items():
                print k1,":",v1
        else:
            print k,":",v

def strip_dict(d):
    for k,v in d.items():
        if type(v) is dict:
            v = strip_dict(v)
            d[k] = v
        else:
            d[k] = v.strip()
    return d

