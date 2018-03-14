import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import util.StringUtil;


//�ӿ����ͣ��������߹��ʶ��Žӿڡ�
//�˻�ע�᣺��ͨ���õ�ַ��ͨ�˻�http://sms.ihuyi.com/register.html
//ע�����
//��1�������ڼ䣬����ϸ�Ķ��ӿ��ĵ���
//��2����ʹ��APIID���鿴APIID���¼�û�����->���ʶ���->��Ʒ����->APIID����APIkey�����ýӿڣ�
//��3���ô���������뻥�����߶��Žӿڲο�ʹ�ã��ͻ��ɸ���ʵ����Ҫ���б�д��

public class sendsms {
	
	private static String Url = "http://api.isms.ihuyi.com/webservice/isms.php?method=Submit";

	public static void main(String [] args) {
		
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(Url);

		//client.getParams().setContentCharset("GBK");
		client.getParams().setContentCharset("UTF-8");
		method.setRequestHeader("ContentType","application/x-www-form-urlencoded;charset=UTF-8");

		NameValuePair[] data = {//�ύ����
			    new NameValuePair("account", "�û���"),//�û����ǵ�¼�û�����->���ʶ���->��Ʒ����->APIID
			    new NameValuePair("password", "����"),//�鿴�������¼�û�����->���ʶ���->��Ʒ����->APIKEY
			    new NameValuePair("mobile", "139xxxxxxxx"),//�ֻ�����
			    new NameValuePair("content", "Your verification code is 1125"),
		};

		method.setRequestBody(data);

		try {
			client.executeMethod(method);
			
			String SubmitResult = method.getResponseBodyAsString();

			//System.out.println(SubmitResult);

			Document doc = DocumentHelper.parseText(SubmitResult); 
			Element root = doc.getRootElement();

			String code = root.elementText("code");
			String msg = root.elementText("msg");
			String ismsid = root.elementText("ismsid");

			System.out.println(code);
			System.out.println(msg);
			System.out.println(ismsid);

			 if("2".equals(code)){
				System.out.println("�����ύ�ɹ�");
			}

		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}