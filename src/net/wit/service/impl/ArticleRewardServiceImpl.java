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

import net.wit.dao.PaymentDao;
import net.wit.service.SnService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.wit.dao.ArticleRewardDao;
import net.wit.entity.*;
import net.wit.service.ArticleRewardService;

/**
 * @ClassName: ArticleRewardDaoImpl
 * @author 降魔战队
 * @date 2017-9-14 19:42:7
 */
 
 
@Service("articleRewardServiceImpl")
public class ArticleRewardServiceImpl extends BaseServiceImpl<ArticleReward, Long> implements ArticleRewardService {
	@Resource(name = "snServiceImpl")
	private SnService snService;

	@Resource(name = "paymentDaoImpl")
	private PaymentDao paymentDao;

	@Resource(name = "articleRewardDaoImpl")
	private ArticleRewardDao articleRewardDao;

	@Resource(name = "articleRewardDaoImpl")
	public void setBaseDao(ArticleRewardDao articleRewardDao) {
		super.setBaseDao(articleRewardDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(ArticleReward articleReward) {
		super.save(articleReward);
	}


	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Payment saveAndPayment(ArticleReward articleReward) {
		try {
			articleRewardDao.persist(articleReward);
			Payment payment = new Payment();
			payment.setAmount(articleReward.getAmount());
			payment.setStatus(Payment.Status.waiting);
			payment.setType(Payment.Type.payment);
			payment.setMethod(Payment.Method.online);
			payment.setMember(articleReward.getMember());
			payment.setSn(snService.generate(Sn.Type.payment));
			payment.setMemo("读者打赏");
			payment.setArticleReward(articleReward);
			paymentDao.persist(payment);
			articleReward.getPayments().add(payment);
			articleRewardDao.merge(articleReward);
			return payment;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public ArticleReward update(ArticleReward articleReward) {
		return super.update(articleReward);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public ArticleReward update(ArticleReward articleReward, String... ignoreProperties) {
		return super.update(articleReward, ignoreProperties);
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
	public void delete(ArticleReward articleReward) {
		super.delete(articleReward);
	}

	public Page<ArticleReward> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return articleRewardDao.findPage(beginDate,endDate,pageable);
	}
}