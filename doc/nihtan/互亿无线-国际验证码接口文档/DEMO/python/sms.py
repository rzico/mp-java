#�ӿ����ͣ��������߹��ʶ��Žӿڡ�
#�˻�ע�᣺��ͨ���õ�ַ��ͨ�˻�http://sms.ihuyi.com/register.html
#ע�����
#��1�������ڼ䣬����ϸ�Ķ��ӿ��ĵ���
#��2����ʹ��APIID���鿴APIID���¼�û�����->���ʶ���->�ʻ���ǩ������->APIID���� APIkey�����ýӿڣ�
#��3���ô���������뻥�����߶��Žӿڲο�ʹ�ã��ͻ��ɸ���ʵ����Ҫ���б�д��
  
#!/usr/local/bin/python
#-*- coding:utf-8 -*-
import httplib
import urllib

host  = "api.isms.ihuyi.com"
sms_send_uri = "/webservice/isms.php?method=Submit"

# �û����ǵ�¼�û�����->���ʶ���->��Ʒ����->APIID
account  = "�û���" 
# �鿴�������¼�û�����->���ʶ���->��Ʒ����->APIKEY
password = "����"

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