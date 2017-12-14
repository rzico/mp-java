/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.plugin.weixin;

import net.wit.entity.Payment;
import net.wit.entity.PluginConfig;
import net.wit.entity.Refunds;
import net.wit.plat.weixin.client.TenpayHttpClient;
import net.wit.plugin.PaymentPlugin;
import net.wit.util.AESUtil;
import net.wit.util.MD5Utils;
import net.wit.plat.weixin.util.WeiXinUtils;

import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.Map;

/**
 * Plugin - 微信公众号支付
 * @author rsico Team
 * @version 3.0
 */
@Component("weixinH5Plugin")
public class WeiXinH5Plugin extends PaymentPlugin {

	public static final String UNIFIED_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	public static final String REFUNDS_ORDER_URL = "https://api.mch.weixin.qq.com/secapi/pay/refund";
	public static final String REFUNDS_QUERY_URL = "https://api.mch.weixin.qq.com/pay/refundquery";

	@Override
	public String getName() {
		return "微信H5支付";
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
				data.put("mweb_url",(String) map.get("mweb_url"));
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
		packageParams.put("spbill_create_ip",request.getRemoteAddr());
		packageParams.put("notify_url", getNotifyUrl(sn, NotifyMethod.async));
		packageParams.put("trade_type", "MWEB");
		//String xapp = request.getHeader("x-app");
		//packageParams.put("scene_info", "{\"h5_info\": {\"type\":\"Wap\",\"wap_url\": \"http://"+pluginConfig.getAttribute("host")+"\",\"wap_name\": \"睿商助手\"}}");
		packageParams.put("scene_info", "{\"h5_info\": {\"type\":\"IOS\",\"app_name\": \"魔篇\",\"package_name\": \"com.rzico.assistant\"}}");

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
				finalpackage.put("signType","MD5");
				try {
					String finalsign = getSign(finalpackage);
					finalpackage.put("mweb_url", data.get("mweb_url"));
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
			logger.error(info.toString());
			map = WeiXinUtils.doXMLParse(info.toString());
			if ("SUCCESS".equals(map.get("result_code").toString())) {
				String sign = getSign(map);
				if (sign.equals(map.get("sign")) && sn.equals(map.get("out_trade_no")) && map.get("appid").equals(pluginConfig.getAttribute("appId"))) {
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
    public String queryOrder(Payment payment,HttpServletRequest request) throws Exception {
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

	//0000 代表申请退款成功，退款结果调查询。
	@Override
	public Map<String, Object> refunds(Refunds refunds,HttpServletRequest request) {
		HashMap<String, Object> finalpackage = new HashMap<>();
		try {
			PluginConfig pluginConfig = getPluginConfig();
			String createNoncestr = WeiXinUtils.CreateNoncestr();
			Payment payment = refunds.getPayment();
			HashMap<String, Object> parameterMap = new HashMap<String, Object>();
			parameterMap.put("appid", pluginConfig.getAttribute("appId"));
			parameterMap.put("mch_id", pluginConfig.getAttribute("partner"));
			parameterMap.put("nonce_str", createNoncestr);
			parameterMap.put("out_refund_no",refunds.getSn());
			parameterMap.put("out_trade_no", payment.getSn());
			parameterMap.put("total_fee", payment.getAmount().multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_HALF_UP).toString());
			parameterMap.put("refund_fee", refunds.getAmount().multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_HALF_UP).toString());
			try {
				String sign = getSign(parameterMap);
				parameterMap.put("sign", sign);
			} catch (Exception e) {
				logger.error(e.getMessage());
				finalpackage.put("return_code", "FAIL");
				finalpackage.put("result_msg", "申请失败");
				return finalpackage;
			}

		    	String xml = WeiXinUtils.getRequestXml(parameterMap);
				String jsonStr = httpsPost(REFUNDS_ORDER_URL,xml,request);

				Map map = WeiXinUtils.doXMLParse(jsonStr);
				String return_code = (String) map.get("return_code");
				if (return_code.equals("SUCCESS") ) {
					if ("SUCCESS".equals((String) map.get("result_code"))) {
						finalpackage.put("return_code", "SUCCESS");
						finalpackage.put("result_msg", "申请成功");
						return finalpackage;
					} else {
						finalpackage.put("return_code", "FAIL");
						finalpackage.put("result_msg", (String) map.get("err_code_des"));
						return finalpackage;
					}
				} else {
					finalpackage.put("return_code", "FAIL");
					finalpackage.put("result_msg", (String) map.get("return_msg"));
					return finalpackage;
				}
		} catch (Exception e) {
			logger.error(e.getMessage());
			finalpackage.put("return_code", "FAIL");
			finalpackage.put("result_msg", "申请失败");
			return finalpackage;
		}


	}

	/**
	 * 申请退款
	 */
	public String refundsQuery(Refunds refunds,HttpServletRequest request)  throws Exception {
		PluginConfig pluginConfig = getPluginConfig();
		String createNoncestr = WeiXinUtils.CreateNoncestr();
		HashMap<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("appid", pluginConfig.getAttribute("appId"));
		parameterMap.put("mch_id", pluginConfig.getAttribute("partner"));
		parameterMap.put("out_refund_no", refunds.getSn());
		parameterMap.put("nonce_str", createNoncestr);
		try {
			parameterMap.put("sign",getSign(parameterMap));
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception("查询失败");
		}

		String xml = WeiXinUtils.getRequestXml(parameterMap);

		HttpClient httpClient = new DefaultHttpClient();
		try {
			HttpPost httpPost = new HttpPost(REFUNDS_QUERY_URL);
			httpPost.setEntity(new StringEntity(xml, "UTF-8"));
			HttpResponse response = httpClient.execute(httpPost);
			String jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
			Map map = WeiXinUtils.doXMLParse(jsonStr);
			String return_code = (String) map.get("return_code");
			if (return_code.equals("SUCCESS") ) {
				if ("SUCCESS".equals((String) map.get("result_code"))) {
					Long n = Long.parseLong(map.get("refund_count").toString());
					String refund_status = (String) map.get("refund_status_" + String.valueOf(n - 1));
					if (refund_status.equals("SUCCESS")) {
						return "0000";
					} else if (refund_status.equals("PROCESSING")) {
						return "9999";
					} else {
						return "0001";
					}
				} else {
					throw new Exception((String) map.get("err_code_des"));
				}
			} else
			if (return_code.equals("FAIL")) {
				throw new Exception((String) map.get("return_msg"));
			} else {
				throw new Exception("查询失败");
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception("查询失败");
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
	}


	/**
	 * 申请通知
	 */
	public String refundsVerify(HttpServletRequest request) {
		try {
			PluginConfig pluginConfig = getPluginConfig();
			HashMap<String, Object> map = new HashMap<String, Object>();

			StringBuffer info = new StringBuffer();
			InputStream in = request.getInputStream();
			BufferedInputStream buf = new BufferedInputStream(in);
			byte[] buffer = new byte[1024];
			int iRead;
			while ((iRead = buf.read(buffer)) != -1) {
				info.append(new String(buffer, 0, iRead, "UTF-8"));
			}
			logger.error(info.toString());
			map = WeiXinUtils.doXMLParse(info.toString());
			if ("SUCCESS".equals(map.get("result_code").toString())) {
				String sign = getSign(map);
				if (sign.equals(map.get("sign"))) {
					try {
						String req_info = map.get("req_info").toString();
                        String bmima =new String(Base64.decode(req_info),"UTF-8");
                        String pwd = MD5Utils.getMD5Str(pluginConfig.getAttribute("key"));
						String decr = AESUtil.decryptData(bmima,pwd);
						Map infoMap = WeiXinUtils.doXMLParse(decr);
						String out_refund_no = infoMap.get("out_refund_no").toString();
						String refund_status = infoMap.get("refund_status").toString();
                        if ("SUCCESS".equals(refund_status)) {
                        	return out_refund_no;
						} else {
                        	return "";
						}
					} catch (Exception e) {
						logger.error(e.getMessage());
					}
				}
			}
			return "";

		} catch (Exception e) {
			logger.error(e.getMessage());
			return "";
		}
	}

}