package com.uns.util;

import net.wit.entity.PluginConfig;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.http.util.TextUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class HttpClientUtils {

	public static String REpostRequestStrJson(String url, String request) throws Exception{
		HttpPost put = new HttpPost(url);
		int statusCode = 200;
		try {

			StringEntity entity = new StringEntity(request,"UTF-8");
			entity.setContentType("application/json");
			put.setEntity(entity);
			HttpClient httpClient = new DefaultHttpClient();
			
			HttpResponse httpRes = httpClient.execute(put);
			statusCode = httpRes.getStatusLine().getStatusCode();
					
			if(statusCode != 200)
				throw new Exception("网络请求应答状态错误：" + statusCode);
			
			String result = EntityUtils.toString(httpRes.getEntity(),"UTF-8");
			return result;
		} catch (Exception e) {
			throw new Exception("网络连接错误：" + e.getMessage());
		}
	}


	public static String doJsonPost(String url, String data) throws Exception {
			CloseableHttpClient httpclient = HttpClients.custom().build();
			try {
				HttpPost httpost = new HttpPost(url); // 设置响应头信息
				httpost.addHeader("Connection", "keep-alive");
				httpost.addHeader("Accept", "*/*");
				httpost.addHeader("Content-Type", "application/json; charset=UTF-8");
				httpost.addHeader("X-Requested-With", "XMLHttpRequest");
				httpost.addHeader("Cache-Control", "max-age=0");
				httpost.addHeader("User-Agent", "");
				httpost.setEntity(new StringEntity(data, "UTF-8"));
				CloseableHttpResponse response = null;
				response = httpclient.execute(httpost);
				try {
					HttpEntity entity = response.getEntity();
					String jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
					EntityUtils.consume(entity);
					return jsonStr;
				} finally {
					response.close();
				}
			} catch (Exception e){
		        e.printStackTrace();
			} finally {
				httpclient.close();
			}
			return  "error";
	}
	
}
