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


//接口类型：互亿无线国际短信接口。
//账户注册：请通过该地址开通账户http://sms.ihuyi.com/register.html
//注意事项：
//（1）调试期间，请仔细阅读接口文档；
//（2）请使用APIID（查看APIID请登录用户中心->国际短信->产品总览->APIID）及APIkey来调用接口；
//（3）该代码仅供接入互亿无线短信接口参考使用，客户可根据实际需要自行编写；

public class sendsms {
	
	private static String Url = "http://api.isms.ihuyi.com/webservice/isms.php?method=Submit";

	public static void main(String [] args) {
		
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(Url);

		//client.getParams().setContentCharset("GBK");
		client.getParams().setContentCharset("UTF-8");
		method.setRequestHeader("ContentType","application/x-www-form-urlencoded;charset=UTF-8");

		NameValuePair[] data = {//提交短信
			    new NameValuePair("account", "用户名"),//用户名是登录用户中心->国际短信->产品总览->APIID
			    new NameValuePair("password", "密码"),//查看密码请登录用户中心->国际短信->产品总览->APIKEY
			    new NameValuePair("mobile", "139xxxxxxxx"),//手机号码
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
				System.out.println("短信提交成功");
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