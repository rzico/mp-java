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

import net.wit.dao.*;
import net.wit.service.SnService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.wit.entity.*;
import net.wit.service.CardService;

/**
 * @ClassName: CardDaoImpl
 * @author 降魔战队
 * @date 2017-11-4 18:12:27
 */
 
 
@Service("cardServiceImpl")
public class CardServiceImpl extends BaseServiceImpl<Card, Long> implements CardService {
	@Resource(name = "cardDaoImpl")
	private CardDao cardDao;

	@Resource(name = "memberDaoImpl")
	private MemberDao memberDao;

	@Resource(name = "cardBillDaoImpl")
	private CardBillDao cardBillDao;

	@Resource(name = "paymentDaoImpl")
	private PaymentDao paymentDao;

	@Resource(name = "refundsDaoImpl")
	private RefundsDao refundsDao;

	@Resource(name = "topicCardDaoImpl")
	private TopicCardDao topicCardDao;

	@Resource(name = "snServiceImpl")
	private SnService snService;

	@Resource(name = "cardDaoImpl")
	public void setBaseDao(CardDao cardDao) {
		super.setBaseDao(cardDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(Card card) {
		super.save(card);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Card update(Card card) {
		return super.update(card);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Card update(Card card, String... ignoreProperties) {
		return super.update(card, ignoreProperties);
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
	public void delete(Card card) {
		super.delete(card);
	}

	public Page<Card> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return cardDao.findPage(beginDate,endDate,pageable);
	}

	public Card find(String code) {
		return cardDao.find(code);
	}

	public synchronized Card activate(Card card,Member member) {
		String name = card.getName();
		String mobile = card.getMobile();
		cardDao.lock(card,LockModeType.PESSIMISTIC_WRITE);
		if (!card.getMembers().contains(member)) {
			card.getMembers().add(member);
		}
		card.setName(name);
		card.setMobile(mobile);
		card.setStatus(Card.Status.activate);
		card.setVip(Card.VIP.vip1);
		cardDao.merge(card);
		if (!member.getCards().contains(card)) {
			member.getCards().add(card);
			memberDao.merge(member);
		}
		return card;
	}

	public synchronized Card create(TopicCard topicCard,Shop shop,String code,Member member) {
		memberDao.lock(member,LockModeType.PESSIMISTIC_WRITE);
		Card myCard = null;
		for (Card c : member.getCards()) {
			if (c.getTopicCard().equals(topicCard)) {
				myCard = c;
				break;
			}
		}
		Card card = null;
		if (myCard==null) {
			card = new Card();
			card.setOwner(topicCard.getTopic().getMember());
			card.setVip(Card.VIP.vip1);
			card.setStatus(Card.Status.none);
			card.setTopicCard(topicCard);
			card.setBalance(BigDecimal.ZERO);
			if (code == null) {
				topicCardDao.refresh(topicCard, LockModeType.PESSIMISTIC_WRITE);
				Long no = topicCard.getIncrement() + 1L;
				topicCard.setIncrement(no);
				topicCardDao.merge(topicCard);
				card.setCode("86" + String.valueOf(shop.getId() + 100000000L) + String.valueOf(no + 10200L));
			} else {
				card.setCode(code);
			}
		} else {
			card = myCard;
			if (code!=null) {
				card.setCode(code);
			}
		}
		cardDao.persist(card);
		if (code==null) {
			card.setStatus(Card.Status.none);
			card.setVip(Card.VIP.vip1);
			card.getMembers().add(member);
			cardDao.persist(card);
			member.getCards().add(card);
			memberDao.merge(member);
		}
		return card;
	}

	//支付插件专用方法
	public void payment(Card card,Payment payment) throws Exception {
		cardDao.refresh(card, LockModeType.PESSIMISTIC_WRITE);
		if (card.getBalance().compareTo(payment.getAmount()) < 0) {
			throw new RuntimeException("卡内余额不足");
		}
		try {
			card.setBalance(card.getBalance().subtract(payment.getAmount()));
			cardDao.merge(card);
			CardBill bill = new CardBill();
			bill.setBalance(card.getBalance());
			bill.setCard(card);
			bill.setCredit(BigDecimal.ZERO);
			bill.setDebit(payment.getAmount());
			bill.setDeleted(false);
			bill.setType(CardBill.Type.consume);
			bill.setMember(payment.getMember());
			PayBill payBill = payment.getPayBill();
			if (payBill != null) {
				bill.setPayBill(payBill);
				bill.setShop(payBill.getShop());
				bill.setOwner(payBill.getOwner());
			} else {
				bill.setOwner(card.getOwner());
			}
			bill.setMethod(CardBill.Method.online);
			bill.setMemo(payment.getMemo());
			bill.setPayment(payment);
			cardBillDao.persist(bill);
			payment.setMember(card.getMembers().get(0));
			payment.setTranSn(payment.getSn());
			payment.setMethod(Payment.Method.card);
			paymentDao.merge(payment);
		} catch (Exception  e) {
			throw  new RuntimeException("支付失败");
		}
	}

	//支付插件专用方法
	public void refunds(Card card,Refunds refunds) throws Exception {
		cardDao.refresh(card, LockModeType.PESSIMISTIC_WRITE);
		try {
			card.setBalance(card.getBalance().add(refunds.getAmount()));
			cardDao.merge(card);
			CardBill bill = new CardBill();
			bill.setBalance(card.getBalance());
			bill.setCard(card);
			bill.setCredit(BigDecimal.ZERO);
			bill.setDebit(refunds.getAmount());
			bill.setDeleted(false);
			bill.setType(CardBill.Type.consume);
			bill.setMember(refunds.getMember());
			PayBill payBill = refunds.getPayBill();
			if (payBill != null) {
				bill.setPayBill(payBill);
				bill.setShop(payBill.getShop());
				bill.setOwner(payBill.getOwner());
			} else {
				bill.setOwner(card.getOwner());
			}
			bill.setMethod(CardBill.Method.online);
			bill.setMemo(refunds.getMemo());
			bill.setRefunds(refunds);
			cardBillDao.persist(bill);
			refunds.setMember(card.getMembers().get(0));
			refunds.setTranSn(refunds.getSn());
			refunds.setMethod(Refunds.Method.card);
			refundsDao.merge(refunds);
		} catch (Exception  e) {
			throw  new RuntimeException("支付失败");
		}
	}

}