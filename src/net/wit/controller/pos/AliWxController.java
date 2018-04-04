/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.pos;

import net.wit.*;
import net.wit.entity.*;
import net.wit.entity.Message;
import net.wit.entity.Payment.Method;
import net.wit.entity.Payment.Status;
import net.wit.entity.Payment.Type;
import net.wit.plugin.PaymentPlugin;
import net.wit.service.*;
import net.wit.util.SettingUtils;
import net.wit.util.StringUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

/**
 * Controller - 移动支付
 * @author rsico Team
 * @version 3.0
 */
@Controller("posAliWxController")
@RequestMapping("/pos/aliwx")
public class AliWxController extends BaseController {

	@Resource(name = "orderServiceImpl")
	private OrderService orderService;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "pluginServiceImpl")
	private PluginService pluginService;

	@Resource(name = "paymentServiceImpl")
	private PaymentService paymentService;

	@Resource(name = "snServiceImpl")
	private SnService snService;

	@Resource(name = "enterpriseServiceImpl")
	private EnterpriseService enterpriseService;

	@Resource(name = "payBillServiceImpl")
	private PayBillService payBillService;

	@Resource(name = "adminServiceImpl")
	private  AdminService adminService;
	@Resource(name = "logServiceImpl")
	private  LogService logService;

	@Resource(name = "shopServiceImpl")
	private ShopService shopService;

	/**
	 * 发起支付
	 * type 1.微信支付 2阿里支付 3会员卡支付
	 * sn 支付单号流水
	 * safeKey 付款安全码 ，扫阿里或微信或会员卡的付款码，没有传入时，代表采用用户找你的二维码方式支付
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping(value = "/submit", method = RequestMethod.POST)
	@ResponseBody
	public DataBlock submit(Long tenantId,String key,String type,String sn,String shopId,String shopName,String safeKey,BigDecimal amount,String operator, HttpServletRequest request) {

		Enterprise tenant = enterpriseService.find(tenantId);
		if (tenant == null) {
			return DataBlock.error("没有开通");
		}
		String myKey = "";
		myKey = DigestUtils.md5Hex(tenantId.toString() + type + sn + "vst@2014-2020$$");
		if (!myKey.equals(key)) {
			return DataBlock.error("通讯密码无效");
		}
		Member member = tenant.getMember();
		String description = "线下代收";
		if (amount == null || amount.compareTo(new BigDecimal(0)) <= 0 || amount.compareTo(new BigDecimal(20000))>0) {
			return DataBlock.error("amount.error");
		}

		Admin admin = adminService.findByUsername(operator);

		if (admin==null) {
			return DataBlock.error("收银员没注册");
		}

		Shop shop = admin.getShop();
		if (shop==null) {
			return DataBlock.error("收银员没登记店铺");
		}

		PayBill payBill = new PayBill();
		payBill.setType(PayBill.Type.cashier);
		payBill.setAmount(amount);
		payBill.setCardAmount(amount);
		payBill.setNoDiscount(BigDecimal.ZERO);
		payBill.setCouponCode(null);
		payBill.setCouponDiscount(BigDecimal.ZERO);
		payBill.setCard(null);
		payBill.setCardDiscount(BigDecimal.ZERO);
		BigDecimal effective = payBill.getEffectiveAmount();
		payBill.setFee(shop.getEnterprise().calcFee(effective));
		payBill.setMethod(PayBill.Method.online);
		payBill.setStatus(PayBill.Status.none);
		payBill.setMember(member);
		payBill.setOwner(shop.getOwner());
		payBill.setShop(shop);
		payBill.setAdmin(admin);
		payBill.setEnterprise(shop.getEnterprise());
		try {
			Payment payment = payBillService.submit(payBill);
			String paymentPluginId  = "";
			switch (type) {
				case "1":
					paymentPluginId  = "weixinPayPlugin";
					break;
				case "2":
					paymentPluginId  = "aliPayPlugin";
					break;
				case "3":
					paymentPluginId  = "cardPayPlugin";
					break;
				case "4":
					paymentPluginId  = "balancePayPlugin";
					break;
			}

			if (payment==null) {
				return DataBlock.error("无效付款单");
			}

			PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(paymentPluginId);
			if (paymentPlugin == null || !paymentPlugin.getIsEnabled()) {
				return DataBlock.error("支付插件无效");
			}

			payment.setMethod(Method.online);
			payment.setPaymentPluginId(paymentPluginId);
			payment.setPaymentMethod(paymentPlugin.getName());
			paymentService.update(payment);

			Map<String, Object> parameters = paymentPlugin.submit(payment,safeKey,request);

			if ("SUCCESS".equals(parameters.get("return_code"))) {
				if ("balancePayPlugin".equals(paymentPluginId) || "cardPayPlugin".equals(paymentPluginId) || "bankPayPlugin".equals(paymentPluginId) || "cashPayPlugin".equals(paymentPluginId)) {
					try {
						paymentService.handle(payment);
					} catch (Exception e) {
						e.printStackTrace();
						//模拟异常通知，通知失败忽略异常，因为也算支付成了，只是通知失败
					}
				}
				return DataBlock.success(parameters, "success");
			} else {
				return DataBlock.error(parameters.get("result_msg").toString());
			}

		} catch (Exception e) {
			return DataBlock.error(e.getMessage());
		}

	}
	/**
	 * 查询状态
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping(value = "/query", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock query(Long tenantId,String key,String sn,Integer vtype, HttpServletRequest request) {
		Enterprise tenant = enterpriseService.find(tenantId);
		if (tenant == null) {
			return DataBlock.error("没有开通");
		}
		String myKey = "";
		myKey = DigestUtils.md5Hex(tenantId.toString() + sn  + "vst@2014-2020$$");
		if (!myKey.equals(key)) {
			return DataBlock.error("通讯密码无效");
		}

		Payment payment = paymentService.findBySn(sn);
		if (payment == null) {
			return DataBlock.error("无效支付单号");
		}
		if (payment.getStatus().equals(Status.success)) {
			return DataBlock.success((Object) "00","支付成功");
		} else
		if (payment.getStatus().equals(Status.failure)) {
			return DataBlock.success((Object) "01","支付失败");
		}
		PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(payment.getPaymentPluginId());
		String resultCode = null;
		try {
			if (paymentPlugin==null) {
				resultCode = "0001";
			} else {
				resultCode = paymentPlugin.queryOrder(payment, request);
			}
		} catch (Exception e) {
			return DataBlock.success("99","success");
		}
		switch (resultCode) {
			case "0000":
				try {

					paymentService.handle(payment);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return DataBlock.success("00","支付成功");
			case "0001":
				try {
					paymentService.close(payment);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return DataBlock.success("01","支付失败");
			default:
				return DataBlock.success("99","支付中");
		}
	}

}