/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.KeyStore;
import java.util.*;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;

import com.sun.org.apache.regexp.internal.RE;
import net.wit.Setting;
import net.wit.controller.weex.BaseController;
import net.wit.entity.*;
import net.wit.service.BindUserService;
import net.wit.service.PaymentService;
import net.wit.service.PluginConfigService;
import net.wit.service.RefundsService;
import net.wit.util.SettingUtils;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Plugin - 支付
 * @author rsico Team
 * @version 3.0
 */
@SuppressWarnings("deprecation")
public abstract class PaymentPlugin implements Comparable<PaymentPlugin> {
	public static Logger logger = LogManager.getLogger(PaymentPlugin.class);

	/** 支付方式名称属性名称 */
	public static final String PAYMENT_NAME_ATTRIBUTE_NAME = "paymentName";

	/** 手续费类型属性名称 */
	public static final String FEE_TYPE_ATTRIBUTE_NAME = "feeType";

	/** 手续费属性名称 */
	public static final String FEE_ATTRIBUTE_NAME = "fee";

	/** LOGO属性名称 */
	public static final String LOGO_ATTRIBUTE_NAME = "logo";

	/** 描述属性名称 */
	public static final String DESCRIPTION_ATTRIBUTE_NAME = "description";

	/**
	 * 手续费类型
	 */
	public enum FeeType {

		/** 按比例收费 */
		scale,

		/** 固定收费 */
		fixed
	}

	/**
	 * 请求方法
	 */
	public enum RequestMethod {

		/** POST */
		post,

		/** GET */
		get
	}

	/**
	 * 通知方法
	 */
	public enum NotifyMethod {

		/** 通用 */
		general,

		/** 同步 */
		sync,

		/** 异步 */
		async
	}

	@Resource(name = "pluginConfigServiceImpl")
	private PluginConfigService pluginConfigService;

	@Resource(name = "paymentServiceImpl")
	public PaymentService paymentService;

	@Resource(name = "refundsServiceImpl")
	public RefundsService refundsService;

	@Resource(name = "bindUserServiceImpl")
	public BindUserService bindUserService;

	/**
	 * 获取ID
	 * @return ID
	 */
	public final String getId() {
		return getClass().getAnnotation(Component.class).value();
	}

	/**
	 * 获取名称
	 * @return 名称
	 */
	public abstract String getName();

	/**
	 * 获取版本
	 * @return 版本
	 */
	public abstract String getVersion();

	/**
	 * 获取设置URL
	 * @return 设置URL
	 */
	public abstract String getSettingUrl();

	/**
	 * 获取是否已安装
	 * @return 是否已安装
	 */
	public boolean getIsInstalled() {
		return pluginConfigService.pluginIdExists(getId());
	}

	/**
	 * 获取插件配置
	 * @return 插件配置
	 */
	public PluginConfig getPluginConfig() {
		return pluginConfigService.findByPluginId(getId());
	}

	/**
	 * 获取是否已启用
	 * @return 是否已启用
	 */
	public boolean getIsEnabled() {
		PluginConfig pluginConfig = getPluginConfig();
		return pluginConfig != null ? pluginConfig.getIsEnabled() : false;
	}

	/**
	 * 获取属性值
	 * @param name 属性名称
	 * @return 属性值
	 */
	public String getAttribute(String name) {
		PluginConfig pluginConfig = getPluginConfig();
		return pluginConfig != null ? pluginConfig.getAttribute(name) : null;
	}

	/**
	 * 获取排序
	 * @return 排序
	 */
	public Integer getOrder() {
		PluginConfig pluginConfig = getPluginConfig();
		return pluginConfig != null ? pluginConfig.getOrders() : null;
	}

	/**
	 * 获取支付方式名称
	 * @return 支付方式名称
	 */
	public String getPaymentName() {
		PluginConfig pluginConfig = getPluginConfig();
		return pluginConfig != null ? pluginConfig.getAttribute(PAYMENT_NAME_ATTRIBUTE_NAME) : null;
	}

	/**
	 * 获取手续费类型
	 * @return 手续费类型
	 */
	public FeeType getFeeType() {
		PluginConfig pluginConfig = getPluginConfig();
		return pluginConfig != null ? FeeType.valueOf(pluginConfig.getAttribute(FEE_TYPE_ATTRIBUTE_NAME)) : null;
	}

	/**
	 * 获取手续费
	 * @return 手续费
	 */
	public BigDecimal getFee() {
		PluginConfig pluginConfig = getPluginConfig();
		return pluginConfig != null ? new BigDecimal(pluginConfig.getAttribute(FEE_ATTRIBUTE_NAME)) : null;
	}

	/**
	 * 获取LOGO
	 * @return LOGO
	 */
	public String getLogo() {
		PluginConfig pluginConfig = getPluginConfig();
		return pluginConfig != null ? pluginConfig.getAttribute(LOGO_ATTRIBUTE_NAME) : null;
	}

	/**
	 * 获取描述
	 * @return 描述
	 */
	public String getDescription() {
		PluginConfig pluginConfig = getPluginConfig();
		return pluginConfig != null ? pluginConfig.getAttribute(DESCRIPTION_ATTRIBUTE_NAME) : null;
	}

	/**
	 * 获取请求URL
	 * @return 请求URL
	 */
	public abstract String getRequestUrl();

	/**
	 * 获取请求方法
	 * @return 请求方法
	 */
	public abstract RequestMethod getRequestMethod();

	/**
	 * 获取请求字符编码
	 * @return 请求字符编码
	 */
	public abstract String getRequestCharset();

	/**
	 * 获取请求参数
	 * @param sn 编号
	 * @param description 描述
	 * @param request httpServletRequest
	 * @return 请求参数
	 */
	public abstract Map<String, Object> getParameterMap(String sn, String description, HttpServletRequest request);
	/**
	 * 验证通知是否合法
	 * @param sn 编号
	 * @param notifyMethod 通知方法
	 * @param request httpServletRequest
	 * @return 通知是否合法
	 */
	public abstract boolean verifyNotify(String sn, NotifyMethod notifyMethod, HttpServletRequest request);

	/**
	 * 获取通知返回消息
	 * @param sn 编号
	 * @param notifyMethod 通知方法
	 * @param request httpServletRequest
	 * @return 通知返回消息
	 */
	public abstract String getNotifyMessage(String sn, NotifyMethod notifyMethod, HttpServletRequest request);

	/**
	 * 提交扫码付
	 */
	public Map<String, Object> submit(Payment payment,String safeKey,HttpServletRequest request) {
		HashMap<String, Object> finalpackage = new HashMap<String, Object>();
		finalpackage.put("result_msg","暂不支持");
		finalpackage.put("return_code","FAIL");
        return finalpackage;
	}

	/**
	 * 获取超时时间
	 * @return 超时时间
	 */
	public abstract Integer getTimeout();

	/**
	 * 计算支付手续费
	 * @param amount 金额
	 * @return 支付手续费
	 */
	public BigDecimal calculateFee(BigDecimal amount) {
		Setting setting = SettingUtils.get();
		BigDecimal fee;
		if (getFeeType() == FeeType.scale) {
			fee = amount.multiply(getFee()).setScale(2, BigDecimal.ROUND_UP);
		} else {
			fee = getFee();
		}
		return setting.setScale(fee);
	}

	/**
	 * 计算支付金额
	 * @param amount 金额
	 * @return 支付金额
	 */
	public BigDecimal calculateAmount(BigDecimal amount) {
		return amount.add(calculateFee(amount)).setScale(2, RoundingMode.UP);
	}

	/**
	 * 根据编号查找收款单
	 * @param sn 编号(忽略大小写)
	 * @return 收款单，若不存在则返回null
	 */
	protected Payment getPayment(String sn) {
		return paymentService.findBySn(sn);
	}


	/**
	 * 查询订单的支付结果  0000成功  9999处理中  其他的失败 
	 */
	public String queryOrder(Payment payment,HttpServletRequest request) throws Exception {
		return "9999";
	}


	/**
	 * 申请退款
	 */
	public Map<String, Object> refunds(Refunds refunds,HttpServletRequest request) {
		HashMap<String, Object> finalpackage = new HashMap<String, Object>();
		finalpackage.put("result_msg","暂不支持");
		finalpackage.put("return_code","FAIL");
		return finalpackage;
	}

	/**
	 * 申请退款
	 */
	public String refundsQuery(Refunds refunds,HttpServletRequest request) throws Exception {
		return "9999";
	}
	/**
	 * 申请通知
	 */
	public String refundsVerify(HttpServletRequest request) {
		return "";
	}

	public BindUser findByUser(Member member,BindUser.Type type) {
		PluginConfig pluginConfig = getPluginConfig();
        return bindUserService.findMember(member,pluginConfig.getAttribute("appId"), type);
	}
	public BindUser findByUser(Member member,String appId,BindUser.Type type) {
		PluginConfig pluginConfig = getPluginConfig();
		return bindUserService.findMember(member,appId, type);
	}
	/**
	 * https双向签名认证，用于支付申请退款
	 *
	 * */
	public String httpsPost(String url,String data,HttpServletRequest request) throws Exception {
		PluginConfig pluginConfig = getPluginConfig();
		String mchId = pluginConfig.getAttribute("partner");
		//指定读取证书格式为PKCS12
		KeyStore keyStore = KeyStore.getInstance("PKCS12");
		String rootPath = request.getSession().getServletContext().getRealPath("/");
		//读取本机存放的PKCS12证书文件
		FileInputStream instream = new FileInputStream(new File(rootPath+"/WEB-INF/classes/cert/apiclient_cert.p12"));
		try {
			//指定PKCS12的密码(商户ID)

			keyStore.load(instream,mchId.toCharArray());
		} finally {
			instream.close();
		}
		SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, mchId.toCharArray()).build();
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
			httpost.setEntity(new StringEntity(data, "UTF-8"));
			CloseableHttpResponse response = httpclient.execute(httpost);
			try {
				HttpEntity entity = response.getEntity();


				String jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
				EntityUtils.consume(entity);
				return jsonStr;
			} finally {
				response.close();
			}
		} finally {
			httpclient.close();
		}
	}


	/**
	 * 获取通知URL
	 * @param sn 编号
	 * @param notifyMethod 通知方法
	 * @return 通知URL
	 */
	public String getNotifyUrl(String sn, NotifyMethod notifyMethod) {
		PluginConfig pluginConfig = getPluginConfig();
		if (notifyMethod == null) {
			return "http://"+pluginConfig.getAttribute("host") + "/payment/notify/" + NotifyMethod.general + "/" + sn + ".jhtml";
		}
		return "http://"+pluginConfig.getAttribute("host") + "/payment/notify/" + notifyMethod + "/" + sn + ".jhtml";
	}

	/**
	 * 获取退款通知URL
	 * @param sn 编号
	 * @param notifyMethod 通知方法
	 * @return 通知URL
	 */
	public String getRefundsNotifyUrl(String sn, NotifyMethod notifyMethod) {
		PluginConfig pluginConfig = getPluginConfig();
		if (notifyMethod == null) {
			return "http://"+pluginConfig.getAttribute("host") + "/refunds/notify/" + NotifyMethod.general + "/" + sn + ".jhtml";
		}
		return "http://"+pluginConfig.getAttribute("host") + "/refunds/notify/" + notifyMethod + "/" + sn + ".jhtml";
	}

	/**
	 * 连接Map键值对
	 * @param map Map
	 * @param prefix 前缀
	 * @param suffix 后缀
	 * @param separator 连接符
	 * @param ignoreEmptyValue 忽略空值
	 * @param ignoreKeys 忽略Key
	 * @return 字符串
	 */
	protected String joinKeyValue(Map<String, Object> map, String prefix, String suffix, String separator, boolean ignoreEmptyValue, String... ignoreKeys) {
		List<String> keys = new ArrayList<String>(map.keySet());
		Collections.sort(keys);

		List<String> list = new ArrayList<String>();
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = ConvertUtils.convert(map.get(key));
			if (StringUtils.isNotEmpty(key) && !ArrayUtils.contains(ignoreKeys, key) && (!ignoreEmptyValue || StringUtils.isNotEmpty(value))) {
				list.add(key + "=" + (value != null ? value : ""));
			}
		}

		return (prefix != null ? prefix : "") + StringUtils.join(list, separator) + (suffix != null ? suffix : "");
	}

	/**
	 * 连接Map值
	 * @param map Map
	 * @param prefix 前缀
	 * @param suffix 后缀
	 * @param separator 连接符
	 * @param ignoreEmptyValue 忽略空值
	 * @param ignoreKeys 忽略Key
	 * @return 字符串
	 */
	protected String joinValue(Map<String, Object> map, String prefix, String suffix, String separator, boolean ignoreEmptyValue, String... ignoreKeys) {
		List<String> keys = new ArrayList<String>(map.keySet());
		Collections.sort(keys);

		List<String> list = new ArrayList<String>();
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = ConvertUtils.convert(map.get(key));
			if (StringUtils.isNotEmpty(key) && !ArrayUtils.contains(ignoreKeys, key) && (!ignoreEmptyValue || StringUtils.isNotEmpty(value))) {
				list.add(value != null ? value : "");
			}
		}
		return (prefix != null ? prefix : "") + StringUtils.join(list, separator) + (suffix != null ? suffix : "");
	}

	/**
	 * POST请求
	 * @param url URL
	 * @param parameterMap 请求参数
	 * @return 返回结果
	 */
	public String post(String url, Map<String, Object> parameterMap) {
		Assert.hasText(url);
		String result = null;
		HttpClient httpClient = new DefaultHttpClient();
		try {
			HttpPost httpPost = new HttpPost(url);
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			if (parameterMap != null) {
				for (Entry<String, Object> entry : parameterMap.entrySet()) {
					String name = entry.getKey();
					String value = ConvertUtils.convert(entry.getValue());
					if (StringUtils.isNotEmpty(name)) {
						nameValuePairs.add(new BasicNameValuePair(name, value));
					}
				}
			}
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			result = EntityUtils.toString(httpEntity);
			EntityUtils.consume(httpEntity);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
		return result;
	}

	/**
	 * GET请求
	 * @param url URL
	 * @param parameterMap 请求参数
	 * @return 返回结果
	 */
	public String get(String url, Map<String, Object> parameterMap) {
		Assert.hasText(url);
		String result = null;
		@SuppressWarnings("resource")
		HttpClient httpClient = new DefaultHttpClient();
		try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			if (parameterMap != null) {
				for (Entry<String, Object> entry : parameterMap.entrySet()) {
					String name = entry.getKey();
					String value = ConvertUtils.convert(entry.getValue());
					if (StringUtils.isNotEmpty(name)) {
						nameValuePairs.add(new BasicNameValuePair(name, value));
					}
				}
			}
			HttpGet httpGet = new HttpGet(url + (StringUtils.contains(url, "?") ? "&" : "?") + EntityUtils.toString(new UrlEncodedFormEntity(nameValuePairs, "UTF-8")));
			HttpResponse httpResponse = httpClient.execute(httpGet);
			HttpEntity httpEntity = httpResponse.getEntity();
			result = EntityUtils.toString(httpEntity, "UTF-8");
			EntityUtils.consume(httpEntity);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		PaymentPlugin other = (PaymentPlugin) obj;
		return new EqualsBuilder().append(getId(), other.getId()).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(getId()).toHashCode();
	}

	public int compareTo(PaymentPlugin paymentPlugin) {
		return new CompareToBuilder().append(getOrder(), paymentPlugin.getOrder()).append(getId(), paymentPlugin.getId()).toComparison();
	}

}