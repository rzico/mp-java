/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.plugin.weixin;

import com.uns.common.CommonUtil;
import com.uns.util.HttpClientUtils;
import net.wit.entity.BindUser;
import net.wit.entity.Payment;
import net.wit.entity.PluginConfig;
import net.wit.entity.Refunds;
import net.wit.plat.weixin.util.WeiXinUtils;
import net.wit.plugin.PaymentPlugin;
import net.wit.util.JsonUtils;
import net.wit.util.MD5Utils;
import net.wit.util.SignUtils;
import net.wit.util.XmlUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.CharArrayReader;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Plugin - 银生宝微信支付
 * @author rsico Team
 * @version 3.0
 */
@Component("unsPayPlugin")
public class UnsPayPlugin extends PaymentPlugin {
	@Override
	public String getName() {
		return "银生宝微信支付";
	}

	@Override
	public String getVersion() {
		return "1.0";
	}

	@Override
	public String getSettingUrl() {
		return "weixinpay/unsbpay/setting.jhtml";
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

	@Override
	public Map<String, Object> getParameterMap(String sn, String description, HttpServletRequest request) {
		PluginConfig pluginConfig = getPluginConfig();
		Payment payment = getPayment(sn);
		DecimalFormat decimalFormat = new DecimalFormat("#");
		BigDecimal money = payment.getAmount();
		HashMap<String, Object> finalpackage = new HashMap<String, Object>();

		HashMap<String,Object> body = new HashMap<String,Object>();
		body.put("accountId", pluginConfig.getAttribute("partner"));
		body.put("orderId", payment.getSn());
		body.put("commodity", description);
		body.put("amount", decimalFormat.format(money));
		body.put("responseUrl", getNotifyUrl(sn, NotifyMethod.async));
		body.put("subject", description);
		BindUser bindUser = findByUser(payment.getMember(), BindUser.Type.weixin);
		if (bindUser!=null) {
			body.put("openid", bindUser.getOpenId());
		} else {
			finalpackage.put("result_msg","没有用户信息");
			finalpackage.put("return_code","FAIL");
			return finalpackage;
		}
		try {
			body.put("mac",getSign(body));
		} catch (Exception e) {
			finalpackage.put("result_msg","验签不通过");
			finalpackage.put("return_code","FAIL");
			return finalpackage;
		}

		String reqUrl = "http://180.166.114.156:18082/fixCodePay/applyWXCodePayByOpenId";
		String resp = post(reqUrl,body);
		return finalpackage;
	}

	@Override
	public Map<String, Object> submit(Payment payment,String safeKey,HttpServletRequest request) {
		PluginConfig pluginConfig = getPluginConfig();
		Map<String,Object> data = new HashMap<String,Object>();
		SortedMap<String,String> map = XmlUtils.getParameterMap(request);
		map.put("service", "unified.trade.micropay");
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
//		map.put("notify_url", getNotifyUrl(payment.getSn(),NotifyMethod.async));

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
							if (
									resultMap.get("err_code").toString().equals("SYSTEMERROR") ||
											resultMap.get("err_code").toString().equals("Internal error") ||
											resultMap.get("err_code").toString().equals("BANKERROR") ||
											resultMap.get("err_code").toString().equals("10003") ||
											resultMap.get("err_code").toString().equals("USERPAYING") ||
											resultMap.get("err_code").toString().equals("System error") ||
											resultMap.get("err_code").toString().equals("aop.ACQ.SYSTEM_ERROR") ||
											resultMap.get("err_code").toString().equals("ACQ.SYSTEM_ERROR")
							) {
								data.put("return_code", "SUCCESS");
								data.put("result_msg", "待确定状态");
							} else {
								data.put("return_code", "FAIL");
								data.put("result_msg", resultMap.get("err_msg"));
							}
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
			logger.error(e.getMessage());
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
					if(!SignUtils.checkParam(map, pluginConfig.getAttribute("key"))){
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
    public String queryOrder(Payment payment,HttpServletRequest request) throws Exception {
		PluginConfig pluginConfig = getPluginConfig();

		SortedMap<String, String> map = new TreeMap();
//		SortedMap<String, String> map = XmlUtils.getParameterMap(request);

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

	/**
	 * 申请退款
	 */
	public Map<String, Object> refunds(Refunds refunds,HttpServletRequest request) {
		PluginConfig pluginConfig = getPluginConfig();
		HashMap<String, Object> finalpackage = new HashMap<String, Object>();
		SortedMap<String, String> map = new TreeMap();
//		SortedMap<String, String> map = XmlUtils.getParameterMap(request);
		DecimalFormat decimalFormat = new DecimalFormat("#");
		BigDecimal money = refunds.getAmount().multiply(new BigDecimal(100));
		map.put("service", "unified.trade.refund");
		map.put("mch_id", pluginConfig.getAttribute("partner"));
		map.put("out_trade_no",refunds.getPayment().getSn());
		map.put("out_refund_no",refunds.getSn());
		map.put("total_fee", decimalFormat.format(money));
		map.put("refund_fee", decimalFormat.format(money));
		map.put("op_user_id", pluginConfig.getAttribute("partner"));
		map.put("nonce_str", String.valueOf(new Date().getTime()));

		Map<String,String> params = SignUtils.paraFilter(map);
		StringBuilder buf = new StringBuilder((params.size() +1) * 10);
		SignUtils.buildPayParams(buf,params,false);
		String preStr = buf.toString()+"&key=" + pluginConfig.getAttribute("key");
		String sign = MD5Utils.getMD5Str(preStr);
		map.put("sign", sign);

		String reqUrl = "https://pay.swiftpass.cn/pay/gateway";

		//System.out.println("reqParams:" + XmlUtils.parseXML(map));
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
				if(resultMap.containsKey("sign")){
					if(!SignUtils.checkParam(resultMap, pluginConfig.getAttribute("key"))){
						finalpackage.put("return_code", "FAIL");
						finalpackage.put("result_msg", "验证签名不通过");
						return finalpackage;
					}else{
						if("0".equals(resultMap.get("status")) && "0".equals(resultMap.get("result_code"))){
							finalpackage.put("return_code", "SUCCESS");
							finalpackage.put("result_msg", "提交成功");
							return finalpackage;
						}else{
							finalpackage.put("return_code", "FAIL");
							finalpackage.put("result_msg", resultMap.get("err_code"));
							return finalpackage;
						}
					}
				}else{

					finalpackage.put("return_code", "FAIL");
					finalpackage.put("result_msg", resultMap.get("message"));
					return finalpackage;
				}
			} else {
				finalpackage.put("return_code", "FAIL");
				finalpackage.put("result_msg","提交银行出错");
				return finalpackage;
			}
		}catch (Exception e) {
			logger.error(e.getMessage());
			finalpackage.put("return_code", "FAIL");
			finalpackage.put("result_msg","提交银行出错");
			return finalpackage;
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

	/**
	 * 查询退款
	 */
	public String refundsQuery(Refunds refunds,HttpServletRequest request) throws Exception {
		PluginConfig pluginConfig = getPluginConfig();
		HashMap<String, Object> finalpackage = new HashMap<String, Object>();
		SortedMap<String, String> map = new TreeMap();
//		SortedMap<String, String> map = XmlUtils.getParameterMap(request);

		map.put("mch_id",pluginConfig.getAttribute("partner"));
		map.put("service", "unified.trade.refundquery");
		map.put("out_refund_no", refunds.getSn());
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
							int count = Integer.parseInt(resultMap.get("refund_count"))-1;
							if ("SUCCESS".equals(resultMap.get("refund_status_"+count))) {
								return "0000";
							} else if ("FAIL".equals(resultMap.get("refund_status_"+count))) {
								return "0001";
							} else if("CHANGE".equals(resultMap.get("refund_status_"+count))){
								return "0001";
							} else {
								return "9999";
							}
						} else {
							throw new Exception(resultMap.get("err_msg"));
						}
					}
				} else {
					throw new Exception(resultMap.get("message"));
				}
			} else {
				throw new Exception("提交查询出错");
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception("提交查询出错");
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

	/**
	 * 申请通知
	 */
	public String refundsVerify(HttpServletRequest request) {
		return "";
	}

}