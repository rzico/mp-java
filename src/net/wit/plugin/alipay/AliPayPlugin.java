
package net.wit.plugin.alipay;

import net.sf.json.JSON;
import net.sf.json.JSONObject;
import net.wit.entity.BindUser;
import net.wit.entity.Member;
import net.wit.entity.Payment;
import net.wit.entity.PluginConfig;
import net.wit.plugin.PaymentPlugin;
import net.wit.util.*;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;
import sun.security.provider.MD5;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.logging.Logger;

/**
 * Plugin - 阿里支付
 * @author rsico Team
 * @version 3.0
 */
@Component("aliPayPlugin_bk")
public class AliPayPlugin extends PaymentPlugin {

	@Override
	public String getName() {
		return "支付宝支付";
	}

	@Override
	public String getVersion() {
		return "1.0";
	}

	@Override
	public String getSettingUrl() {
		return "alipay/cebpay/setting.jhtml";
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

	@Override
	public Map<String, Object> getParameterMap(String sn, String description, HttpServletRequest request) {
		PluginConfig pluginConfig = getPluginConfig();
		HashMap<String, Object> finalpackage = new HashMap<String, Object>();
		ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
		Payment payment = getPayment(sn);
		DecimalFormat decimalFormat = new DecimalFormat("#");
		BigDecimal money = payment.getAmount().multiply(new BigDecimal(100));

		SortedMap<String,String> map = XmlUtils.getParameterMap(request);
		map.put("service", "pay.alipay.jspay");
		map.put("mch_id", pluginConfig.getAttribute("partner"));
		map.put("out_trade_no", payment.getSn());
		map.put("body", description);
		map.put("total_fee", decimalFormat.format(money));
		map.put("mch_create_ip", request.getRemoteAddr());
		map.put("notify_url", this.getNotifyUrl(sn,NotifyMethod.async));
		map.put("nonce_str", String.valueOf(new Date().getTime()));
		BindUser bindUser = findByUser(payment.getMember(), BindUser.Type.alipay);
		if (bindUser!=null) {
			map.put("buyer_id",bindUser.getOpenId());
		}

		Map<String,String> params = SignUtils.paraFilter(map);
		StringBuilder buf = new StringBuilder((params.size() +1) * 10);
		SignUtils.buildPayParams(buf,params,false);
		String preStr = buf.toString()+"&key=" + pluginConfig.getAttribute("key");
		String sign = MD5Utils.getMD5Str(preStr);
		map.put("sign", sign);

		String reqUrl = "https://pay.swiftpass.cn/pay/gateway";

		CloseableHttpResponse response = null;
		CloseableHttpClient client = null;
		String res = null;
		try {
			HttpPost httpPost = new HttpPost(reqUrl);
			StringEntity entityParams = new StringEntity(XmlUtils.parseXML(map),"utf-8");
			httpPost.setEntity(entityParams);
			httpPost.setHeader("Content-Type", "text/xml;charset=utf-8");
			client = HttpClients.createDefault();
			response = client.execute(httpPost);
			if(response != null && response.getEntity() != null){
				Map<String,String> resultMap = XmlUtils.toMap(EntityUtils.toByteArray(response.getEntity()), "utf-8");
				res = XmlUtils.toXml(resultMap);

				if(resultMap.containsKey("sign")){
					if(!SignUtils.checkParam(resultMap, pluginConfig.getAttribute("key"))){
						finalpackage.put("result_msg","验证签名不通过");
						finalpackage.put("return_code","FAIL");
						return finalpackage;
					}else{
						if("0".equals(resultMap.get("status")) && "0".equals(resultMap.get("result_code"))){
							String pay_info = resultMap.get("pay_info");
							Map<String,String> payInfo = new HashMap<>();
							payInfo = JsonUtils.toObject(pay_info,Map.class);
							finalpackage.put("tradeNO", payInfo.get("tradeNO"));
							finalpackage.put("return_code","SUCCESS");
							return finalpackage;
						}else{
							finalpackage.put("return_msg",resultMap.get("err_msg"));
							finalpackage.put("return_code","FAIL");
							return finalpackage;
						}
					}
				}else{
					finalpackage.put("result_msg",resultMap.get("message"));
					finalpackage.put("return_code","FAIL");
					return finalpackage;
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			finalpackage.put("result_msg","验签不通过");
			finalpackage.put("return_code","FAIL");
			return finalpackage;
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					logger.error(e.getMessage());
				}
			}
			if (client != null) {
				try {
					client.close();
				} catch (IOException e) {
					logger.error(e.getMessage());
				}
			}
		}
		finalpackage.put("result_msg","未知错误");
		finalpackage.put("return_code","FAIL");
		return finalpackage;
	}

	@Override
	public Map<String, Object> submit(Payment payment,String safeKey,HttpServletRequest request) {
		PluginConfig pluginConfig = getPluginConfig();
		Map<String,Object> data = new HashMap<String,Object>();
		SortedMap<String,String> map = XmlUtils.getParameterMap(request);
		map.put("service", "pay.weixin.native");
		//safekey64编码
		map.put("auth_code", safeKey);
		DecimalFormat decimalFormat = new DecimalFormat("#");
		BigDecimal money = payment.getAmount().multiply(new BigDecimal(100));
		map.put("mch_id",pluginConfig.getAttribute("partner"));
		map.put("out_trade_no", payment.getSn());
		map.put("body", "扫码收单");
		map.put("total_fee", decimalFormat.format(money));
		map.put("mch_create_ip", request.getRemoteAddr());
		map.put("nonce_str", String.valueOf(new Date().getTime()));

		Map<String,String> params = SignUtils.paraFilter(map);
		StringBuilder buf = new StringBuilder((params.size() +1) * 10);
		SignUtils.buildPayParams(buf,params,false);
		String preStr = buf.toString()+"&key=" + pluginConfig.getAttribute("key");
		String sign = MD5Utils.getMD5Str(preStr);
		map.put("sign", sign);

		String reqUrl = "https://pay.swiftpass.cn/pay/gateway";

		CloseableHttpResponse response = null;
		CloseableHttpClient client = null;
		String res = null;
		try {
			HttpPost httpPost = new HttpPost(reqUrl);
			StringEntity entityParams = new StringEntity(XmlUtils.parseXML(map),"utf-8");
			httpPost.setEntity(entityParams);
			httpPost.setHeader("Content-Type", "text/xml;charset=utf-8");
			client = HttpClients.createDefault();
			response = client.execute(httpPost);
			if(response != null && response.getEntity() != null){
				Map<String,String> resultMap = XmlUtils.toMap(EntityUtils.toByteArray(response.getEntity()), "utf-8");

				if ("0".equals(resultMap.get("status"))) {
					if(!SignUtils.checkParam(resultMap, pluginConfig.getAttribute("key")) ){
						data.put("return_code", "FAIL");
						data.put("result_msg", "验证签名不通过");
						return data;
					}else{
						if("0".equals(resultMap.get("result_code"))){
							data.put("return_code", "SUCCESS");
							data.put("result_msg", "执行成功");
							return data;
						}else{
							data.put("return_code", "FAIL");
							data.put("result_msg", resultMap.get("message"));
							return data;
						}
					}
				}else{
					data.put("return_code", "FAIL");
					data.put("result_msg",resultMap.get("message"));
					return data;
				}
			} else {
				data.put("return_code", "FAIL");
				data.put("result_msg","网络异常");
				return data;
			}
		} catch (Exception e) {
			e.printStackTrace();
			data.put("return_code", "FAIL");
			data.put("result_msg", "提交订单出现异常");
			return data;
		} finally {
			if(response != null){
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(client != null){
				try {
					client.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public boolean verifyNotify(String sn, NotifyMethod notifyMethod, HttpServletRequest request) {
		try {
			PluginConfig pluginConfig = getPluginConfig();
			String resString = XmlUtils.parseRequst(request);
			if(resString != null && !"".equals(resString)){
				Map<String,String> map = XmlUtils.toMap(resString.getBytes(), "utf-8");
				if(map.containsKey("sign")){
					if(!SignUtils.checkParam(map,pluginConfig.getAttribute("key"))){
						return false;
					}else{
						String status = map.get("status");
						if(status != null && "0".equals(status)){
							if("0".equals(map.get("result_code")) && "0".equals(map.get("pay_result"))) {
								Payment payment = getPayment(sn);
								payment.setTranSn(map.get("transaction_id"));
								paymentService.save(payment);
								return true;
							} else {
								return false;
							}
						}else{
							return false;
						}
					}
				}

			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
		return false;
	}

	/**
	 * 查询订单的支付结果  0000成功  9999处理中  其他的失败
	 */
	@Override
    public String queryOrder(Payment payment,HttpServletRequest request)  throws Exception {
		PluginConfig pluginConfig = getPluginConfig();
		SortedMap<String, String> map = XmlUtils.getParameterMap(request);

		map.put("mch_id", pluginConfig.getAttribute("partner"));
		map.put("service", "unified.trade.query");
		map.put("out_trade_no", payment.getSn());
		map.put("nonce_str", String.valueOf(new Date().getTime()));

		Map<String, String> params = SignUtils.paraFilter(map);
		StringBuilder buf = new StringBuilder((params.size() + 1) * 10);
		SignUtils.buildPayParams(buf, params, false);
		String preStr = buf.toString()+"&key=" + pluginConfig.getAttribute("key");
		String sign = MD5Utils.getMD5Str(preStr);
		map.put("sign", sign);

		String reqUrl = "https://pay.swiftpass.cn/pay/gateway";
		CloseableHttpResponse response = null;
		CloseableHttpClient client = null;
		String res = null;
		try {
			HttpPost httpPost = new HttpPost(reqUrl);
			StringEntity entityParams = new StringEntity(XmlUtils.parseXML(map), "utf-8");
			httpPost.setEntity(entityParams);
			client = HttpClients.createDefault();
			response = client.execute(httpPost);
			if (response != null && response.getEntity() != null) {
				Map<String, String> resultMap = XmlUtils.toMap(EntityUtils.toByteArray(response.getEntity()), "utf-8");
				if (resultMap.containsKey("sign")) {
					if (!SignUtils.checkParam(resultMap, pluginConfig.getAttribute("key"))) {
						throw new Exception("签名出错");
					} else {
						if ("0".equals(resultMap.get("status")) && "0".equals(resultMap.get("result_code"))) {
							if ("SUCCESS".equals(resultMap.get("trade_state"))) {
								payment.setTranSn(resultMap.get("transaction_id"));
								paymentService.save(payment);
								return "0000";

							}else if("USERPAYING".equals(resultMap.get("trade_state"))){
								return "9999";
							}else{
								return "0001";
							}
						} else {
							if ("0".equals(resultMap.get("status"))) {
								return "0001";
							} else {
								throw new Exception(resultMap.get("message"));
							}
						}
					}
				} else {
					throw new Exception("签名出错");
				}
			} else {
				throw new Exception("网络异常");
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception(e.getMessage());
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (client != null) {
				try {
					client.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public String getNotifyMessage(String sn, NotifyMethod notifyMethod, HttpServletRequest request) {
		return "SUCCESS";
	}

	@Override
	public Integer getTimeout() {
		return 30;
	}

}