/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.plugin.card;

import net.wit.entity.Member;
import net.wit.entity.Payment;
import net.wit.entity.Refunds;
import net.wit.plugin.PaymentPlugin;
import net.wit.service.MemberService;
import net.wit.service.RSAService;
import net.wit.util.MD5Utils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Plugin - 现金支付
 * @author rsico Team
 * @version 3.0
 */
@Component("cashPayPlugin")
public class CashPayPlugin extends PaymentPlugin {

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "rsaServiceImpl")
	private RSAService rsaService;

	@Override
	public String getName() {
		return "现金";
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
		try {
			payment.setTranSn(payment.getSn());
			payment.setMethod(Payment.Method.offline);
			paymentService.update(payment);
			finalpackage.put("return_code", "SUCCESS");
			finalpackage.put("result_msg", "提交成功");
		} catch (Exception e) {
			logger.error(e.getMessage());
			finalpackage.put("return_code", "FAIL");
			finalpackage.put("result_msg", "提交失败");
		}
		return finalpackage;
	}
	@Override
	public Map<String, Object> submit(Payment payment,String safeKey,HttpServletRequest request) {
		HashMap<String, Object> finalpackage = new HashMap<>();
		try {
			payment.setTranSn(payment.getSn());
			payment.setMethod(Payment.Method.offline);
			paymentService.update(payment);
			finalpackage.put("return_code", "SUCCESS");
			finalpackage.put("result_msg", "提交成功");
		} catch (Exception e) {
			logger.error(e.getMessage());
			finalpackage.put("return_code", "FAIL");
			finalpackage.put("result_msg", "提交失败");
		}
		return finalpackage;
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
		try {
			refunds.setMethod(Refunds.Method.offline);
			refunds.setTranSn(refunds.getSn());
			refundsService.update(refunds);
			finalpackage.put("return_code", "SUCCESS");
			finalpackage.put("result_msg", "提交成功");
		} catch (Exception e) {
			logger.error(e.getMessage());
			finalpackage.put("return_code", "FAIL");
			finalpackage.put("result_msg", "提交失败");
		}
		return finalpackage;
	}
	/**
	 * 查询退款
	 */
	public String refundsQuery(Refunds refunds,HttpServletRequest request) throws Exception {
		if (refunds.getTranSn()==null) {
			if (DateUtils.addSeconds(refunds.getCreateDate(),30).compareTo(new Date())<0) {
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