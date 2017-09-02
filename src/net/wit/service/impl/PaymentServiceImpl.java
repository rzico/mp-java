/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.service.impl;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.persistence.LockModeType;
import javax.servlet.http.HttpServletRequest;

import net.wit.Setting;
import net.wit.dao.*;
import net.wit.entity.*;
import net.wit.plugin.PaymentPlugin;
import net.wit.service.*;
import net.wit.util.*;
import net.wit.weixin.pojo.AccessToken;
import net.wit.weixin.util.WeixinUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.time.DateUtils;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.shiro.codec.Base64;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Payment.Status;
import net.wit.entity.Payment.Type;

/**
 * Service - 收款单
 * @author rsico Team
 * @version 3.0
 */
@Service("paymentServiceImpl")
public class PaymentServiceImpl extends BaseServiceImpl<Payment, Long> implements PaymentService {

	@Resource(name = "paymentDaoImpl")
	private PaymentDao paymentDao;

	@Resource(name = "pluginServiceImpl")
	private PluginService pluginService;

	@Resource(name = "snDaoImpl")
	private SnDao snDao;

	@Resource(name = "paymentDaoImpl")
	public void setBaseDao(PaymentDao paymentDao) {
		super.setBaseDao(paymentDao);
	}

	@Transactional(readOnly = true)
	public Payment findBySn(String sn) {
		return paymentDao.findBySn(sn);
	}


	@Transactional
	public synchronized void handle(Payment payment) throws Exception {
		paymentDao.refresh(payment, LockModeType.PESSIMISTIC_WRITE);
		if (payment != null && !payment.getStatus().equals(Status.success)) {
			if (payment.getType() == Type.payment) {
			} else if (payment.getType() == Type.recharge) {
			}
			payment.setPaymentDate(new Date());
			paymentDao.merge(payment);
		}
	}

	@Transactional
	public void close(Payment payment) throws Exception {
		paymentDao.refresh(payment, LockModeType.PESSIMISTIC_WRITE);
		if (payment != null && payment.getStatus() == Status.wait) {
			payment.setMemo("超时关闭");
			payment.setStatus(Status.failure);
			paymentDao.merge(payment);
		};
	}


	@Transactional(readOnly = true)
	public Page<Payment> findPage(Member member, Pageable pageable, Payment.Type type) {
		return paymentDao.findPage(member, pageable, type);
	}

}