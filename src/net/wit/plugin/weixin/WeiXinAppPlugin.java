
package net.wit.plugin.weixin;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.wit.Setting;
import net.wit.entity.Payment;
import net.wit.entity.PluginConfig;
import net.wit.entity.Refunds;
import net.wit.plugin.PaymentPlugin;
import net.wit.util.MD5Utils;
import net.wit.util.SettingUtils;
import net.wit.plat.weixin.util.WeiXinUtils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

/**
 * Plugin - 微信APP支付
 * @author rsico Team
 * @version 3.0
 */
@Component("weixinAppPlugin")
public class WeiXinAppPlugin extends PaymentPlugin {

	public static final String UNIFIED_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

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
		return "weixin/app/setting.jhtml";
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
			System.out.println(jsonStr);
			String return_code = (String) map.get("return_code");
			if (return_code.equals("SUCCESS")) {
				data.put("return_code", return_code);
				data.put("prepay_id",(String) map.get("prepay_id"));
			} else {
				data.put("return_code", "FAIL");
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
		Setting setting = SettingUtils.get();
		Payment payment = getPayment(sn);

		HashMap<String, Object> packageParams = new HashMap<String, Object>();
		String createNoncestr = WeiXinUtils.CreateNoncestr();

		packageParams.put("appid", pluginConfig.getAttribute("appId"));
		packageParams.put("mch_id", pluginConfig.getAttribute("partner"));
		packageParams.put("nonce_str", createNoncestr);
		packageParams.put("body", description);
		packageParams.put("out_trade_no", payment.getSn());

		// 这里写的金额为1 分到时修改
		packageParams.put("total_fee", payment.getAmount().multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_HALF_UP).toString());
		packageParams.put("spbill_create_ip","106.122.225.134");// request.getRemoteAddr().toString());
		packageParams.put("notify_url", getNotifyUrl(sn, NotifyMethod.async));

		packageParams.put("trade_type", "APP");
			
		try {
			String sign = getSign(packageParams);
			packageParams.put("sign", sign);
		} catch (Exception e) {
			e.printStackTrace();
		}

		String xml = WeiXinUtils.getRequestXml(packageParams);

		String prepay_id = "";
		HashMap<String, Object> finalpackage = new HashMap<String, Object>();
		try {
			Map<String,String> data = getPrepayId(UNIFIED_ORDER_URL, xml);
			if (data.get("return_code").equals("SUCCESS")) {
				prepay_id = data.get("prepay_id");
				String timestamp = WeiXinUtils.getTimeStamp();
				finalpackage.put("appid",pluginConfig.getAttribute("appId"));
				finalpackage.put("partnerid", pluginConfig.getAttribute("partner"));
				finalpackage.put("prepayid", prepay_id);
				finalpackage.put("package", "Sign=WXPay");
				finalpackage.put("noncestr", createNoncestr);
				finalpackage.put("timestamp", timestamp);
				try {
					String finalsign = getSign(finalpackage);
					finalpackage.put("return_code", "SUCCESS");
					finalpackage.put("sign", finalsign);
				} catch (Exception e) {
					finalpackage.put("return_code", "FAIL");
					finalpackage.put("result_msg", "提交失败");
					logger.error(e.getMessage());
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

			StringBuffer info = new java.lang.StringBuffer();
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
}