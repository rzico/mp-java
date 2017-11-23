/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.plugin.card;

import net.wit.entity.Card;
import net.wit.entity.Member;
import net.wit.entity.Payment;
import net.wit.plugin.PaymentPlugin;
import net.wit.service.CardService;
import net.wit.service.MemberService;
import net.wit.service.RSAService;
import net.wit.util.MD5Utils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Plugin - 余额支付
 * @author rsico Team
 * @version 3.0
 */
@Component("balancePayPlugin")
public class BalancePayPlugin extends PaymentPlugin {

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "rsaServiceImpl")
	private RSAService rsaService;

	@Override
	public String getName() {
		return "余额支付";
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
		if (member!=null) {
			String password = rsaService.decryptParameter("enPassword",request);
			if (member.getPassword()==null) {
				finalpackage.put("return_code", "FAIL");
				finalpackage.put("return_msg", "没有设置密码");
				return finalpackage;
			}
			if (!MD5Utils.getMD5Str(password).equals(member.getPassword())) {
				finalpackage.put("return_code", "FAIL");
				finalpackage.put("return_msg", "密码不正确");
				return finalpackage;
			}
			if (member.getBalance().compareTo(payment.getAmount()) > 0) {
				try {
					memberService.payment(member,payment);
					finalpackage.put("return_code", "SUCCESS");
					finalpackage.put("return_msg", "提交成功");
				} catch (Exception e) {
					finalpackage.put("return_code", "FAIL");
					finalpackage.put("return_msg", e.getMessage());
				}
				return finalpackage;
			} else {
				finalpackage.put("return_code", "FAIL");
				finalpackage.put("return_msg", "卡内余额不足");
				return finalpackage;
			}
		} else {
			finalpackage.put("return_code", "FAIL");
			finalpackage.put("return_msg", "无效会员卡");
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
		if (payment!=null) {
			return "0000";
		} else {
			return "0001";
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