package com.uns.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class HttpClientUtils {
	private static Log log = LogFactory.getLog(HttpClientUtils.class);
	
	public static String REpostRequestStrJson(String url, String request) throws Exception{
		log.info("start post request url:" + url +",request param:" + request);
		HttpPost put = new HttpPost(url);
		int statusCode = 200;
		try {
			StringEntity entity = new StringEntity(request,"UTF-8");
			entity.setContentType("application/json");
			put.setEntity(entity);
			HttpClient client = new DefaultHttpClient();
			
			HttpResponse httpRes = client.execute(put);
			statusCode = httpRes.getStatusLine().getStatusCode();
					
			log.info("request end,status : " + statusCode);
			if(statusCode != 200)
				throw new Exception("网络请求应答状态错误：" + statusCode);
			
			String result = EntityUtils.toString(httpRes.getEntity(),"UTF-8");
			return result;
		} catch (Exception e) {
			log.info("网络连接错误："+e.getMessage());
			throw new Exception("网络连接错误：" + e.getMessage());
		}
	}

	
	
	
}
