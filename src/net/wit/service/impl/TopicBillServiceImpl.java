package net.wit.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.wit.Filter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.Principal;
import net.wit.Filter.Operator;

import net.wit.dao.MemberDao;
import net.wit.dao.PaymentDao;
import net.wit.plat.im.User;
import net.wit.service.SnService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.wit.dao.TopicBillDao;
import net.wit.entity.*;
import net.wit.service.TopicBillService;

/**
 * @ClassName: TopicBillDaoImpl
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
 
@Service("topicBillServiceImpl")
public class TopicBillServiceImpl extends BaseServiceImpl<TopicBill, Long> implements TopicBillService {
	@Resource(name = "topicBillDaoImpl")
	private TopicBillDao topicBillDao;
	@Resource(name = "memberDaoImpl")
	private MemberDao memberDao;
	@Resource(name = "paymentDaoImpl")
	private PaymentDao paymentDao;

	@Resource(name = "snServiceImpl")
	private SnService snService;

	@Resource(name = "topicBillDaoImpl")
	public void setBaseDao(TopicBillDao topicBillDao) {
		super.setBaseDao(topicBillDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(TopicBill topicBill) {
		super.save(topicBill);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public TopicBill update(TopicBill topicBill) {
		return super.update(topicBill);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public TopicBill update(TopicBill topicBill, String... ignoreProperties) {
		return super.update(topicBill, ignoreProperties);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void delete(Long... ids) {
		super.delete(ids);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void delete(TopicBill topicBill) {
		super.delete(topicBill);
	}

	public Page<TopicBill> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return topicBillDao.findPage(beginDate,endDate,pageable);
	}

	public synchronized Payment activate(TopicBill topicBill) {
		topicBillDao.persist(topicBill);
		String userName = "gm_"+String.valueOf(10200+Message.Type.account.ordinal());
		Member payee = memberDao.findByUsername(userName);
		if (payee==null) {
			payee = new Member();
			payee.setUsername(userName);
			payee.setNickName("账单提醒");
			payee.setLogo("http://cdn.rzico.com/weex/resources/images/"+userName+".png");
//			payee.setPoint(0L);
			payee.setBalance(BigDecimal.ZERO);
			payee.setFreezeBalance(BigDecimal.ZERO);
			payee.setIsEnabled(true);
			payee.setIsLocked(false);
			payee.setLoginFailureCount(0);
			payee.setRegisterIp("127.0.0.1");
			memberDao.persist(payee);
			User.userAttr(payee);
		}
		Payment payment = new Payment();
		payment.setMemo("激活专栏");
		payment.setSn(snService.generate(Sn.Type.topic));
		payment.setMethod(Payment.Method.online);
		payment.setMember(topicBill.getMember());
		payment.setPayee(payee);
		payment.setAmount(topicBill.getAmount());
		payment.setType(Payment.Type.topic);
		payment.setAmount(topicBill.getAmount());
		payment.setTopicBill(topicBill);
		payment.setStatus(Payment.Status.waiting);
		payment.setWay(Payment.Way.yundian);

		paymentDao.persist(payment);
		topicBill.setPayment(payment);
		topicBillDao.merge(topicBill);
        return payment;
	}

}