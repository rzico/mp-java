/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.plugin.card;

import net.wit.entity.Card;
import net.wit.entity.Member;
import net.wit.entity.Payment;
import net.wit.entity.Refunds;
import net.wit.plugin.PaymentPlugin;
import net.wit.service.CardService;
import net.wit.service.MemberService;
import net.wit.service.RSAService;
import net.wit.util.MD5Utils;
import net.wit.util.ScanUtil;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
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
		return "钱包余额";
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
				finalpackage.put("result_msg", "没有设置密码");
				return finalpackage;
			}
			if (!MD5Utils.getMD5Str(password).equals(member.getPassword())) {
				finalpackage.put("return_code", "FAIL");
				finalpackage.put("result_msg", "密码不正确");
				return finalpackage;
			}
			if (member.getBalance().compareTo(payment.getAmount()) > 0) {
				try {
					memberService.payment(member,payment);
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


		if (!data.get("type").toString().equals("818805")) {
			finalpackage.put("return_code", "FAIL");
			finalpackage.put("result_msg", "无效付款码");
			return finalpackage;
		}
        String code = data.get("code");
		Long id = Long.parseLong(code.substring(0,code.length()-6))-10200;

		Member member = memberService.find(id);
		if (member==null) {
			finalpackage.put("return_code", "FAIL");
			finalpackage.put("result_msg", "无效付款码");
			return finalpackage;
		}

        String sign = code.substring(code.length()-6,code.length());
        if (!sign.equals(member.getSign())) {
			finalpackage.put("return_code", "FAIL");
			finalpackage.put("result_msg", "请重打开付款码");
			return finalpackage;
		}

		if (member.getBalance().compareTo(payment.getAmount()) >= 0) {
			try {
				memberService.payment(member,payment);
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
		if (member==null) {
			finalpackage.put("return_code", "FAIL");
			finalpackage.put("result_msg", "无效退款单");
			return finalpackage;
		}
		try {
			memberService.refunds(member,refunds);
			finalpackage.put("return_code", "SUCCESS");
			finalpackage.put("result_msg", "提交成功");
		} catch (Exception e) {
			finalpackage.put("return_code", "FAIL");
			finalpackage.put("result_msg", e.getMessage());
		}
		return finalpackage;
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