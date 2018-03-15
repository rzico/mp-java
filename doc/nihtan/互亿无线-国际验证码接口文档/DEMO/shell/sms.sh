#接口类型：互亿无线国际短信接口。
#账户注册：请通过该地址开通账户http://sms.ihuyi.com/register.html
#注意事项：
#（1）调试期间，请仔细阅读接口文档；
#（2）请使用APIID（查看APIID请登录用户中心->国际短信->帐户及签名设置->APIID）及 APIkey来调用接口；
#（3）该代码仅供接入互亿无线短信接口参考使用，客户可根据实际需要自行编写；

#!/bin/sh
#用户名是登录用户中心->国际短信>帐户及签名设置->APIID
account="用户名"
# 查看密码请登录用户中心->国际短信->产品总览->APIKEY
password="密码"
#修改为您要发送的手机号
mobile="136xxxxxxxx"
#短信内容
content="Your verification code is 1125"
echo "send sms:"
curl --data "account=$account&password=$password&mobile=$mobile&content=$content&rd=1" "http://api.isms.ihuyi.com/webservice/isms.php?method=Submit"
echo  -e "\n query balance:"
curl --data "account=$account&password=$password" "http://api.isms.ihuyi.com/webservice/isms.php?method=GetNum"