/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.plugin.weixin;

import java.io.*;
import java.math.BigDecimal;
import java.security.KeyStore;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;

import net.wit.entity.BindUser;
import net.wit.entity.Payment;
import net.wit.entity.PluginConfig;
import net.wit.entity.Refunds;
import net.wit.plugin.PaymentPlugin;
import net.wit.util.MD5Utils;
import net.wit.plat.weixin.util.WeiXinUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

/**
 * Plugin - 微信公众号支付
 * @author rsico Team
 * @version 3.0
 */
@Component("weixinOcPayPlugin")
public class WeiXinPayPlugin extends PaymentPlugin {

	public static final String UNIFIED_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	public static final String REFUNDS_ORDER_URL = "https://api.mch.weixin.qq.com/secapi/pay/refund";

	@Override
	public String getName() {
		return "微信钱包";
	}

	@Override
	public String getVersion() {
		return "1.0";
	}

	@Override
	public String getSettingUrl() {
		return "weixinpay/setting.jhtml";
	}

	@Override
	public String getRequestUrl() {
		return "";
	}

	@Override
	public RequestMethod getRequestMethod() {
		return RequestMethod.post;
	}

	@Override
	public String getRequestCharset() {
		return "UTF-8";
	}

	/**
	 * 作用：生成签名
	 */
	public String getSign(HashMap<String, Object> params) throws Exception {
		PluginConfig pluginConfig = getPluginConfig();
		// 签名步骤一：按字典序排序参数
		String str = WeiXinUtils.FormatBizQueryParaMap(params, false);
		// 签名步骤二：在string后加入KEY
		str += "&key=" + pluginConfig.getAttribute("key");
		// 签名步骤三：MD5加密 签名步骤四：所有字符转为大写
		str = MD5Utils.getMD5Str(str).toUpperCase();
		return str;
	}

	public Map<String,String> getPrepayId(String url, String xmlParam) {
		Map<String,String> data = new HashMap<String,String>();
		HttpClient httpClient = new DefaultHttpClient();
		try {
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(new StringEntity(xmlParam, "UTF-8"));
			HttpResponse response = httpClient.execute(httpPost);
			String jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");

			Map map = WeiXinUtils.doXMLParse(jsonStr);
			String return_code = (String) map.get("return_code");
			if (return_code.equals("SUCCESS")) {
				data.put("return_code", return_code);
				data.put("prepay_id",(String) map.get("prepay_id"));
				data.put("code_url",(String) map.get("code_url"));
			} else {
				data.put("return_code", "FAIL");
				logger.error((String) map.get("return_msg"));
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			data.put("return_code", "FAIL");
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
		return data;
	}

	@Override
	public Map<String, Object> getParameterMap(String sn, String description, HttpServletRequest request) {
		PluginConfig pluginConfig = getPluginConfig();
		Payment payment = getPayment(sn);

		HashMap<String, Object> packageParams = new HashMap<>();
		String createNoncestr = WeiXinUtils.CreateNoncestr();

		packageParams.put("appid", pluginConfig.getAttribute("appId"));
		packageParams.put("mch_id", pluginConfig.getAttribute("partner"));
		packageParams.put("nonce_str", createNoncestr);
		packageParams.put("body", description);
		packageParams.put("out_trade_no", payment.getSn());

		// 这里写的金额为1 分到时修改
		packageParams.put("total_fee", payment.getAmount().multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_HALF_UP).toString());
		packageParams.put("spbill_create_ip", request.getRemoteAddr());
		packageParams.put("notify_url", getNotifyUrl(sn, NotifyMethod.async));
		packageParams.put("trade_type", "JSAPI");

		if (request.getHeader("x-app")!=null && "applet".equals(request.getHeader("x-app"))) {
			BindUser bindUser = findByUser(payment.getMember(),pluginConfig.getAttribute("applet"), BindUser.Type.weixin);
			if (bindUser!=null) {
				packageParams.put("openid",bindUser.getOpenId());
			}
		} else {
			BindUser bindUser = findByUser(payment.getMember(), BindUser.Type.weixin);
			if (bindUser!=null) {
				packageParams.put("openid",bindUser.getOpenId());
			}
		}

		try {
			String sign = getSign(packageParams);
			packageParams.put("sign", sign);
		} catch (Exception e) {
			e.printStackTrace();
		}

		String xml = WeiXinUtils.getRequestXml(packageParams);
		HashMap<String, Object> finalpackage = new HashMap<>();

		String prepay_id = "";
		try {
			Map<String,String> data = getPrepayId(UNIFIED_ORDER_URL, xml);
			if (data.get("return_code").equals("SUCCESS")) {
				prepay_id = data.get("prepay_id");
				String timestamp = WeiXinUtils.getTimeStamp();
				String packages = "prepay_id=" + prepay_id;
				finalpackage.put("appId",pluginConfig.getAttribute("appId"));
				finalpackage.put("timeStamp", timestamp);
				finalpackage.put("nonceStr", createNoncestr);
				finalpackage.put("package", packages);
				finalpackage.put("signType","MD5");
				try {
					String finalsign = getSign(finalpackage);
					finalpackage.put("paySign", finalsign);
					finalpackage.put("return_code", "SUCCESS");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					finalpackage.put("return_code", "FAIL");
					finalpackage.put("result_msg", "提交失败");
				}
			}
			else {
				logger.error("统一支付接口获取预支付订单出错");
				finalpackage.put("return_code", "FAIL");
				finalpackage.put("result_msg", "提交失败");
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			finalpackage.put("return_code", "FAIL");
			finalpackage.put("result_msg", "提交失败");
		}

		return finalpackage;
	}

	@Override
	public boolean verifyNotify(String sn, NotifyMethod notifyMethod, HttpServletRequest request) {
		try {
			PluginConfig pluginConfig = getPluginConfig();
			Payment payment = getPayment(sn);

			HashMap<String, Object> map = new HashMap<String, Object>();

			StringBuffer info = new StringBuffer();
			InputStream in = request.getInputStream();
			BufferedInputStream buf = new BufferedInputStream(in);
			byte[] buffer = new byte[1024];
			int iRead;
			while ((iRead = buf.read(buffer)) != -1) {
				info.append(new String(buffer, 0, iRead, "UTF-8"));
			}
			map = WeiXinUtils.doXMLParse(info.toString());
			if (map.get("result_code").toString().equals("SUCCESS")) {
				String sign = getSign(map);
				if (sign.equals(map.get("sign")) && sn.equals(map.get("out_trade_no")) && map.get("appid").equals(pluginConfig.getAttribute("appId"))
						&& payment.getAmount().multiply(new BigDecimal(100)).compareTo(new BigDecimal((String) map.get("total_fee"))) == 0) {
					try {
						return true;
					} catch (Exception e) {
						logger.error(e.getMessage());
					}
				}
			}
			return false;

		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
	}

	/**
	 * 查询订单的支付结果  0000成功  9999处理中  其他的失败 
	 */
	@Override
    public String queryOrder(Payment payment,HttpServletRequest request)  throws Exception {
		PluginConfig pluginConfig = getPluginConfig();
		String createNoncestr = WeiXinUtils.CreateNoncestr();
		HashMap<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("appid", pluginConfig.getAttribute("appId"));
		parameterMap.put("mch_id", pluginConfig.getAttribute("partner"));
		parameterMap.put("out_trade_no", payment.getSn());
		parameterMap.put("nonce_str", createNoncestr);
		try {
			parameterMap.put("sign",getSign(parameterMap));
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception("查询出错");
		}

		String xml = WeiXinUtils.getRequestXml(parameterMap);

		HttpClient httpClient = new DefaultHttpClient();
		try {
			HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/pay/orderquery");
			httpPost.setEntity(new StringEntity(xml, "UTF-8"));
			HttpResponse response = httpClient.execute(httpPost);
			String jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
			Map map = WeiXinUtils.doXMLParse(jsonStr);
			String return_code = (String) map.get("return_code");
			if (return_code.equals("SUCCESS")) {
				String result = (String) map.get("result_code");
				if ("SUCCESS".equals(result)) {
					String status = (String) map.get("trade_state");
					if (status.equals("SUCCESS")) {
						return "0000";
					} else if (status.equals("USERPAYING")) {
						return "9999";
					} else {
						return "0001";
					}
				} else  {
					throw new Exception((String) map.get("err_code_des"));
				}
			} else
			{
				throw new Exception((String) map.get("return_msg"));
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception("查询出错");
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
	}
	
	@Override
	public String getNotifyMessage(String sn, NotifyMethod notifyMethod, HttpServletRequest request) {
		return "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
	}

	@Override
	public Integer getTimeout() {
		return 30;
	}

	/**
	 * https双向签名认证，用于支付申请退款
	 *
	 * */
	public String payHttps(String url,String xml,HttpServletRequest request) throws Exception {
		PluginConfig pluginConfig = getPluginConfig();
		//指定读取证书格式为PKCS12
		KeyStore keyStore = KeyStore.getInstance("PKCS12");
		String rootPath = request.getSession().getServletContext().getRealPath("/");
		String path = rootPath+"/cert/weixin_cert.p12";
		//读取本机存放的PKCS12证书文件
		FileInputStream instream = new FileInputStream(new File(path));
		try {
			//指定PKCS12的密码(商户ID)
			keyStore.load(instream,pluginConfig.getAttribute("partner").toCharArray());
		} finally {
			instream.close();
		}
		SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore,pluginConfig.getAttribute("partner").toCharArray()).build();
		//指定TLS版本
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
				sslcontext,new String[] { "TLSv1" },null,
				SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
		//设置httpclient的SSLSocketFactory
		CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
		try {
			HttpPost httpost = new HttpPost(url); // 设置响应头信息
			httpost.addHeader("Connection", "keep-alive");
			httpost.addHeader("Accept", "*/*");
			httpost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			httpost.addHeader("Host", "api.mch.weixin.qq.com");
			httpost.addHeader("X-Requested-With", "XMLHttpRequest");
			httpost.addHeader("Cache-Control", "max-age=0");
			httpost.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
			httpost.setEntity(new StringEntity(xml, "UTF-8"));
			CloseableHttpResponse response = httpclient.execute(httpost);
			try {
				HttpEntity entity = response.getEntity();
				String jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
				EntityUtils.consume(entity);
				return jsonStr;
			}finally {
				response.close();
			}
		}finally {
			httpclient.close();
		}
	}


	/**
	 * 申请退款
	 */
	public Map<String, Object> refunds(Refunds refunds,HttpServletRequest request) {
		PluginConfig pluginConfig = getPluginConfig();
		HashMap<String, Object> finalpackage = new HashMap<String, Object>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		DecimalFormat decimalFormat = new DecimalFormat("#");
		BigDecimal money = refunds.getAmount().multiply(new BigDecimal(100));
		map.put("appid", pluginConfig.getAttribute("appId"));
		map.put("mch_id", pluginConfig.getAttribute("partner"));
		map.put("nonce_str", String.valueOf(new Date().getTime()));
		map.put("out_trade_no",refunds.getPayment().getSn());
		map.put("out_refund_no",refunds.getSn());
		map.put("total_fee", decimalFormat.format(money));
		map.put("refund_fee", decimalFormat.format(money));
		try {
			map.put("sign",getSign(map));
		} catch (Exception e) {
			finalpackage.put("return_code", "FAIL");
			finalpackage.put("result_msg","签名出错");
			return finalpackage;
		}

		String reqUrl = "https://api.mch.weixin.qq.com/secapi/pay/refund";

		try {
			String xml = WeiXinUtils.getRequestXml(map);
			String jsonStr = payHttps(reqUrl,xml,request);

				HashMap<String,Object> resultMap = WeiXinUtils.doXMLParse(jsonStr);

				if(resultMap.containsKey("sign")){
					String sign = getSign(resultMap);
					if(!sign.equals(resultMap.get("sign"))){
						finalpackage.put("return_code", "FAIL");
						finalpackage.put("result_msg", "验证签名不通过");
						return finalpackage;
					}else{
						if("SUCCESS".equals(resultMap.get("return_code").toString()) && "SUCCESS".equals(resultMap.get("result_code").toString())){
							finalpackage.put("return_code", "SUCCESS");
							finalpackage.put("result_msg", "提交成功");
							return finalpackage;
						}else{
							finalpackage.put("return_code", "FAIL");
							finalpackage.put("result_msg", resultMap.get("err_code_des").toString());
							return finalpackage;
						}
					}
				}else{

					finalpackage.put("return_code", "FAIL");
					finalpackage.put("result_msg", resultMap.get("return_msg").toString());
					return finalpackage;
				}
		}catch (Exception e) {
			logger.error(e.getMessage());
			finalpackage.put("return_code", "FAIL");
			finalpackage.put("result_msg","提交银行出错");
			return finalpackage;
		}
	}

	/**
	 * 查询退款
	 */
	public String refundsQuery(Refunds refunds,HttpServletRequest request) throws Exception {
		PluginConfig pluginConfig = getPluginConfig();
		HashMap<String, Object> map = new HashMap<String, Object>();
		HashMap<String, Object> finalpackage = new HashMap<String, Object>();
		map.put("appid", pluginConfig.getAttribute("appId"));
		map.put("mch_id", pluginConfig.getAttribute("partner"));
		map.put("out_refund_no", refunds.getSn());
		map.put("nonce_str", String.valueOf(new Date().getTime()));
		try {
			map.put("sign",getSign(map));
		} catch (Exception e) {
			throw new Exception("签名出错");
		}

		String reqUrl = "https://api.mch.weixin.qq.com/pay/refundquery";
		try {
			String xml = WeiXinUtils.getRequestXml(map);
			String jsonStr = payHttps(reqUrl,xml,request);

				HashMap<String,Object> resultMap = WeiXinUtils.doXMLParse(jsonStr);
				if (resultMap.containsKey("sign")) {
					String sign = getSign(resultMap);
					if(!sign.equals(resultMap.get("sign").toString())){
						throw new Exception("签名出错");
					} else {
						if ("SUCCESS".equals(resultMap.get("return_code").toString()) && "SUCCESS".equals(resultMap.get("result_code").toString())) {
							int count = Integer.parseInt(resultMap.get("refund_count").toString())-1;
							if ("SUCCESS".equals(resultMap.get("refund_status_"+count).toString())) {
								return "0000";
							} else if ("REFUNDCLOSE".equals(resultMap.get("refund_status_"+count).toString())) {
								return "0001";
							} else if("CHANGE".equals(resultMap.get("refund_status_"+count).toString())){
								return "0001";
							} else {
								return "9999";
							}
						} else {
							throw new Exception(resultMap.get("err_code_des").toString());
						}
					}
				} else {
					throw new Exception(resultMap.get("err_code_des").toString());
				}
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception("提交查询出错");
		}
	}

	/**
	 * 申请通知
	 */
	public String refundsVerify(HttpServletRequest request) {
		return "";
	}


}