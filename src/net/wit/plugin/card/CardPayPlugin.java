/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.plugin.card;

import net.wit.entity.*;
import net.wit.plat.weixin.util.WeiXinUtils;
import net.wit.plugin.PaymentPlugin;
import net.wit.service.CardService;
import net.wit.service.PaymentService;
import net.wit.service.RSAService;
import net.wit.service.SmssendService;
import net.wit.util.MD5Utils;
import net.wit.util.ScanUtil;
import org.apache.commons.lang.time.DateUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Plugin - 会员卡支付
 * @author rsico Team
 * @version 3.0
 */
@Component("cardPayPlugin")
public class CardPayPlugin extends PaymentPlugin {
	@Resource(name = "cardServiceImpl")
	private CardService cardService;

	@Resource(name = "rsaServiceImpl")
	private RSAService rsaService;

	@Override
	public String getName() {
		return "会员卡";
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

	@Override
	public Map<String, Object> getParameterMap(String sn, String description, HttpServletRequest request) {
		Payment payment = getPayment(sn);
		HashMap<String, Object> finalpackage = new HashMap<>();
		Member member = payment.getMember();
		Member payee = payment.getPayee();
		Card card = null;
		for (Card c:member.getCards()) {
			if (c.getOwner().equals(payee)) {
				card = c;
				break;
			}
		}
		if (card!=null) {

			if (!card.getOwner().equals(payment.getPayee())) {
				finalpackage.put("return_code", "FAIL");
				finalpackage.put("result_msg", "不是本店会员卡");
				return finalpackage;
			}

			if (card.getBalance().compareTo(payment.getAmount()) >= 0) {
				try {
					if (member.getPassword()==null) {
						finalpackage.put("return_code", "FAIL");
						finalpackage.put("result_msg", "没有设置密码");
						return finalpackage;
					}
					String password = rsaService.decryptParameter("enPassword",request);
					if (MD5Utils.getMD5Str(password).equals(member.getPassword())) {
						finalpackage.put("return_code", "FAIL");
						finalpackage.put("result_msg", "密码不正确");
						return finalpackage;
					}
					cardService.payment(card,payment);
					finalpackage.put("return_code", "SUCCESS");
					finalpackage.put("result_msg", "提交成功");

				} catch (Exception e) {
					finalpackage.put("return_code", "FAIL");
					finalpackage.put("result_msg", e.getMessage());
				}
				return finalpackage;
			} else {
				finalpackage.put("return_code", "FAIL");
				finalpackage.put("result_msg", "卡内余额不足");
				return finalpackage;
			}
		} else {
			finalpackage.put("return_code", "FAIL");
			finalpackage.put("result_msg", "无效会员卡");
			return finalpackage;
		}
	}

	//safeKey 为付款码
	@Override
	public Map<String, Object> submit(Payment payment,String safeKey,HttpServletRequest request) {
		HashMap<String, Object> finalpackage = new HashMap<>();
		if (safeKey==null) {
			finalpackage.put("return_code", "FAIL");
			finalpackage.put("result_msg", "无效付款码");
			return finalpackage;
		}

		Map<String,String> data = ScanUtil.scanParser(safeKey);

		String code = data.get("code");
		String c = code;
		if (data.get("type").toString().equals("818802")) {
			c = code.substring(0,code.length()-6);
		}

		Card card = cardService.find(c);
		if (card==null) {
			finalpackage.put("return_code", "FAIL");
			finalpackage.put("result_msg", "无效付款码");
			return finalpackage;
		}

		if (data.get("type").toString().equals("818801")) {
			if (!code.substring(0,2).equals("88")) {
				finalpackage.put("return_code", "FAIL");
				finalpackage.put("result_msg", "请重打开付款码");
				return finalpackage;
			}
		} else
		if (data.get("type").toString().equals("818802")) {
			String sign = code.substring(code.length() - 6, code.length());
			if (!sign.equals(card.getSign())) {
				finalpackage.put("return_code", "FAIL");
				finalpackage.put("result_msg", "请重打开付款码");
				return finalpackage;
			}
		} else {
			finalpackage.put("return_code", "FAIL");
			finalpackage.put("result_msg", "无效付款码");
			return finalpackage;
		}

		if (!card.getOwner().equals(payment.getPayee())) {
			finalpackage.put("return_code", "FAIL");
			finalpackage.put("result_msg", "不是本店会员卡");
			return finalpackage;
		}

		if (card.getBalance().compareTo(payment.getAmount()) >= 0) {
			try {
				cardService.payment(card,payment);
				finalpackage.put("return_code", "SUCCESS");
				finalpackage.put("result_msg", "提交成功");
			} catch (Exception e) {
				finalpackage.put("return_code", "FAIL");
				finalpackage.put("result_msg", e.getMessage());
			}
			return finalpackage;
		} else {
			finalpackage.put("return_code", "FAIL");
			finalpackage.put("result_msg", "卡内余额不足");
			return finalpackage;
		}
	}

	@Override
	public boolean verifyNotify(String sn, NotifyMethod notifyMethod, HttpServletRequest request) {
		Payment payment = getPayment(sn);
		if (payment!=null) {
			return (payment.getTranSn()!=null);
		} else {
			return false;
		}
	}

	/**
	 * 查询订单的支付结果  0000成功  9999处理中  其他的失败 
	 */
	@Override
    public String queryOrder(Payment payment,HttpServletRequest request)  throws Exception {
		if (payment.getTranSn()==null) {
			if (DateUtils.addSeconds(payment.getCreateDate(),30).compareTo(new Date())<0) {
				return "0001";
			} else {
				return "9999";
			}
		}
		else
		{
			return "0000";
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
	public Map<String, Object> refunds(Refunds refunds, HttpServletRequest request) {
		HashMap<String, Object> finalpackage = new HashMap<>();
		Member member = refunds.getMember();
		Member payee = refunds.getPayee();
		Card card = null;
		for (Card c:member.getCards()) {
			if (c.getOwner().equals(payee)) {
				card = c;
				break;
			}
		}
		if (card!=null) {
				try {
					cardService.refunds(card,refunds);
					finalpackage.put("return_code", "SUCCESS");
					finalpackage.put("result_msg", "提交成功");
				} catch (Exception e) {
					finalpackage.put("return_code", "FAIL");
					finalpackage.put("result_msg", e.getMessage());
				}
				return finalpackage;
		} else {
			finalpackage.put("return_code", "FAIL");
			finalpackage.put("result_msg", "无效会员卡");
			return finalpackage;
		}
	}
	/**
	 * 查询退款
	 */
	public String refundsQuery(Refunds refunds,HttpServletRequest request) throws Exception {
		if (refunds.getTranSn()==null) {
			if (DateUtils.addSeconds(refunds.getCreateDate(),30).compareTo(new Date())>0) {
				return "0001";
			} else {
				return "9999";
			}
		}
		else
		{
			return "0000";
		}
	}

}