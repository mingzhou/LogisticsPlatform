from bs4 import BeautifulSoup
from crawler import Crawler
from utils import *
from hashlib import sha512

class ChinawutongCrawler(Crawler):
    """
    Goods crawler for Chinawutong web 
    中国物通网的货源信息在http://www.chinawutong.com/103.html
    每页有8条货源信息，点击会有此货源的其他信息
    对一条货源信息的提取既要从信息列表的信息
    对页面中静态信息的提取
    如果页面有变化，两个方法也要进行修改
    """

    HOME_URL = "http://www.chinawutong.com/"
    HOST = "http://www.chinawutong.com/"

    def __init__(self):
        Crawler.__init__(self)

    def crawl(self):
        url = self.HOST + "103.html"
        for i in range(7):                         #   取前10个列表，一个列表中有8个货源信息
            page = self.get(url + "?", {"pid" : i + 1})   #   获得一页中的信息列表
            data, ok = self.get_items(page)         #   分析此列表页面
            for d in data:
                self.write_to_mongo(d)
            if ok:
                break
        print("FIN")

    def get_items(self, page):
        soup = BeautifulSoup(page, "html.parser")
        table = soup.find(attrs={"class":"mainall"})
        data = []

        for tr in table.find_all("tr"):
            d = {}
            d["source site"] = "cwt"
            td = tr.find("td")
            divs = td.find_all("div")
            '''
            div[0]
        
            <div class="nname fl">
            <ul>
            <li class="biaotitext lh25">
            <span class="pl10">
            <a class="fontsize14 fontweight" href="/203/22081891.html" onclick="ClickGNline('22081891','1359476','11')" target="_blank">江苏省 扬州市 市辖区 → 安徽省 合肥市 市辖区</a>
            </span>
            </li>
            <li class="lh25">
            <span class="fontsize14 huoorange pl10"><a href="http://yzhs88.chinawutong.com" onclick="ClickGNline('22081891','1359476','11')" target="_blank">扬州恒顺物流有限公司</a></span>
        
        (配货信息部)
            </li>
            <li class="lh20">
            <span class="huohui pl10">货物名称：</span>电器
            <span class="huohui pl8">运输类型：</span>不限
            <span class="huohui pl8">信息有效期：</span>2014-04-09
            </li>
            </ul>
            </div>
            '''
            ls = divs[0].find_all("li")
            s = ls[0].span.a.text   # src -> dst
            pos = s.find('→ ')
            d["from"] = s[:pos].strip()
            d["to"] = s[pos+1:].strip()
            d["provider"] = ls[1].span.string
            contents = ls[2].contents
            d["goods type"] = contents[2].strip()
            d["vehicle type"] = contents[4].strip()
            d["deadline"] = contents[6].strip()
        #    for span in ls[2].find_all("span"):
        #        s += (span.string + contents[2 + cnt * 2].strip() + "\t")
        #        cnt += 1
        
            '''
            divs[1]
        
            <div class="ninfor1 fl">
            <ul>
            <li><span class="huohui">运价：</span>面议</li>
            <li><span class="huohui">重量：</span>7 吨</li>
            </ul>
            </div>
            '''
            ls = divs[1].find_all("li")
            d["price"] = ls[0].contents[-1].strip()
            d["weight"] = ls[1].contents[-1].strip()
        
            '''
            <div class="ninfor2 fl">
            <ul>
            <li>
            <span class="huohui">发布时间：</span>04-02
        
            </li>
            <li><span class="huohui">体积：</span>详谈</li>
            </ul>
            </div>
        
            '''
            ls = divs[2].find_all("li")
            d["publish date"] = ls[0].contents[-1].strip()
            d["volume"] = ls[1].contents[-1].strip()
        
            '''
            divs[3]
        
            <div class="nexplanation fl lh20">
            <span class="fr chakanmor">
            <a href="/203/22081891.html" onclick="ClickGNline('22081891','1359476','11')" rel="nofollow" target="_blank">查看详细信息</a>
            </span>
            <span class="huohui pl10">说明：</span>求车源
        
            <a href="http://yzhs88.chinawutong.com" rel="nofollow" target="_blank">
            <img alt="物信通" class="chuizhijuzhong" src="http://www.chinawutong.com/images/wxthy_2012.gif" title="第三方诚信认证机构权威认证，建议您优先选择物信通会员"/>
            </a>
        
             <a href="http://www.chinawutong.com/subject/wuliubi/" rel="nofollow" target="_blank"><img alt="特别推荐" src="http://www.chinawutong.com/images/wxt_2012.gif" title="特别推荐，用物流币参与竞价，排名靠前"/></a>
            </div>
            '''
            d["others"] = divs[3].contents[4].strip()

            if self.find(d):
                return data, True
            data.append(d)
        return data, False
