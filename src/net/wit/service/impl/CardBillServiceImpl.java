package net.wit.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.LockModeType;

import net.wit.Filter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.Principal;
import net.wit.Filter.Operator;

import net.wit.dao.CardDao;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.wit.dao.CardBillDao;
import net.wit.entity.*;
import net.wit.service.CardBillService;

/**
 * @ClassName: CardBillDaoImpl
 * @author 降魔战队
 * @date 2017-11-4 18:12:27
 */
 
 
@Service("cardBillServiceImpl")
public class CardBillServiceImpl extends BaseServiceImpl<CardBill, Long> implements CardBillService {
	@Resource(name = "cardBillDaoImpl")
	private CardBillDao cardBillDao;

	@Resource(name = "cardDaoImpl")
	private CardDao cardDao;

	@Resource(name = "cardBillDaoImpl")
	public void setBaseDao(CardBillDao cardBillDao) {
		super.setBaseDao(cardBillDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(CardBill cardBill) {
		super.save(cardBill);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public CardBill update(CardBill cardBill) {
		return super.update(cardBill);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public CardBill update(CardBill cardBill, String... ignoreProperties) {
		return super.update(cardBill, ignoreProperties);
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
	public void delete(CardBill cardBill) {
		super.delete(cardBill);
	}

	public Page<CardBill> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return cardBillDao.findPage(beginDate,endDate,pageable);
	}

	public void fill(CardBill cardBill) throws Exception {
		Card card = cardBill.getCard();
		cardDao.refresh(card, LockModeType.PESSIMISTIC_WRITE);
		card.setBalance(card.getBalance().add(cardBill.getCredit()));
		cardDao.merge(card);
		cardBill.setType(CardBill.Type.recharge);
		cardBill.setBalance(card.getBalance());
		cardBillDao.persist(cardBill);
	}

	public void refund(CardBill cardBill) throws Exception {
		Card card = cardBill.getCard();
		cardDao.refresh(card, LockModeType.PESSIMISTIC_WRITE);
		if (card.getBalance().compareTo(cardBill.getDebit())<0) {
			throw new RuntimeException("卡余额不足");
		}
		card.setBalance(card.getBalance().subtract(cardBill.getDebit()));
		cardDao.merge(card);
		cardBill.setType(CardBill.Type.recharge);
		cardBill.setBalance(card.getBalance());
		cardBillDao.persist(cardBill);
	}

}