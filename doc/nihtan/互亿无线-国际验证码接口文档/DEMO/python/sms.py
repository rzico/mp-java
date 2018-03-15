#接口类型：互亿无线国际短信接口。
#账户注册：请通过该地址开通账户http://sms.ihuyi.com/register.html
#注意事项：
#（1）调试期间，请仔细阅读接口文档；
#（2）请使用APIID（查看APIID请登录用户中心->国际短信->帐户及签名设置->APIID）及 APIkey来调用接口；
#（3）该代码仅供接入互亿无线短信接口参考使用，客户可根据实际需要自行编写；
  
#!/usr/local/bin/python
#-*- coding:utf-8 -*-
import httplib
import urllib

host  = "api.isms.ihuyi.com"
sms_send_uri = "/webservice/isms.php?method=Submit"

# 用户名是登录用户中心->国际短信->产品总览->APIID
account  = "用户名" 
# 查看密码请登录用户中心->国际短信->产品总览->APIKEY
password = "密码"

def send_sms(text, mobile):
    params = urllib.urlencode({'account': account, 'password' : password, 'content': text, 'mobile':mobile,'format':'json' })
    headers = {"Content-type": "application/x-www-form-urlencoded", "Accept": "text/plain"}
    conn = httplib.HTTPConnection(host, port=80, timeout=30)
    conn.request("POST", sms_send_uri, params, headers)
    response = conn.getresponse()
    response_str = response.read()
    conn.close()
    return response_str 

if __name__ == '__main__':

    mobile = "138xxxxxxxx"
    text = "Your verification code is 1125"

    print(send_sms(text, mobile))