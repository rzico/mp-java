<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import = "java.io.*" %>
<%@ page import = "java.net.*" %>
<%@ page import = "java.util.*" %>
<%@ page import = "java.lang.*" %>
<%
String postUrl = "http://api.isms.ihuyi.com/webservice/isms.php?method=Submit";

//接口类型：互亿无线国际短信接口。
//账户注册：请通过该地址开通账户http://sms.ihuyi.com/register.html
//注意事项：
//（1）调试期间，请仔细阅读接口文档；
//（2）请使用APIID（查看APIID请登录用户中心->国际短信->产品总览->APIID）及APIkey来调用接口；
//（3）该代码仅供接入互亿无线短信接口参考使用，客户可根据实际需要自行编写；

String account = "用户名";//用户名是登录用户中心->国际短信->产品总览->APIID
String password = "密码";//查看密码请登录用户中心->国际短信->产品总览->APIKEY
String mobile = "135xxxxxxxx";//手机号码
String content = "Your verification code is 1125"


try {

	URL url = new URL(postUrl);
	HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	connection.setDoOutput(true);//允许连接提交信息
	connection.setRequestMethod("POST");//网页提交方式“GET”、“POST”
	connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	connection.setRequestProperty("Connection", "Keep-Alive");
	StringBuffer sb = new StringBuffer();
	sb.append("account="+account);
	sb.append("&password="+password);
	sb.append("&mobile="+mobile);
	sb.append("&content="+content);
	OutputStream os = connection.getOutputStream();
	os.write(sb.toString().getBytes());
	os.close();

	String line, result = "";
	BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
	while ((line = in.readLine()) != null) {
		result += line + "\n";
	}
	in.close();
	out.println(result);

} catch (IOException e) {
	e.printStackTrace(System.out);
}

%>