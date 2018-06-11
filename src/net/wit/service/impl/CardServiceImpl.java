package net.wit.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
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
import net.wit.service.SmssendService;
import net.wit.service.SnService;
import org.apache.poi.ss.formula.functions.T;
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

	@Resource(name = "receiverDaoImpl")
	private ReceiverDao receiverDao;

	@Resource(name = "cardBillDaoImpl")
	private CardBillDao cardBillDao;

	@Resource(name = "cardPointBillDaoImpl")
	private CardPointBillDao cardPointBillDao;

	@Resource(name = "paymentDaoImpl")
	private PaymentDao paymentDao;

	@Resource(name = "refundsDaoImpl")
	private RefundsDao refundsDao;

	@Resource(name = "smssendServiceImpl")
	private SmssendService smssendService;

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

	public Card find(Member member,Member owner) {
        Card card = null;
		for (Card c:member.getCards()) {
			if (c.getOwner().equals(owner)) {
				card = c;
				break;
			}
		}
		return card;
	}

	public synchronized Card activate(Card card,Member member,Member promoter) {
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
		card.setType(Card.Type.member);
		card.setBonus(BigDecimal.ZERO);
		card.setAmount(BigDecimal.ZERO);
		card.setPromoter(promoter);

		TopicConfig config = card.getOwner().getTopic().getConfig();
		if (config.getPattern().equals(TopicConfig.Pattern.pattern1)) {
			card.setType(Card.Type.team);
		}

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
			card.setType(Card.Type.member);
			card.setBonus(BigDecimal.ZERO);
			card.setStatus(Card.Status.none);
			card.setTopicCard(topicCard);
			card.setBalance(BigDecimal.ZERO);
			card.setAmount(BigDecimal.ZERO);
			card.setPoint(0L);
			if (code == null) {
				topicCardDao.refresh(topicCard, LockModeType.PESSIMISTIC_WRITE);
				Long no = topicCard.getIncrement() + 1L;
				topicCard.setIncrement(no);
				topicCardDao.merge(topicCard);

				if (shop==null) {
					card.setCode("85" + String.valueOf(topicCard.getId() + 100000000L) + String.valueOf(no + 10200L));
				} else {
					card.setCode("86" + String.valueOf(shop.getId() + 100000000L) + String.valueOf(no + 10200L));
				}

			} else {
				card.setCode(code);
			}
			card.setStatus(Card.Status.none);
			card.setShop(shop);
		} else {
			card = myCard;
			if (code!=null) {
				card.setCode(code);
			}
		}
		cardDao.persist(card);
		if (code==null) {
			card.setVip(Card.VIP.vip1);
			card.getMembers().add(member);
			cardDao.persist(card);
			if (!member.getCards().contains(card)) {
				member.getCards().add(card);
				memberDao.merge(member);
			}
		}
		return card;
	}


	//分销关系，创建并激活会员卡
	public synchronized Card createAndActivate(Member member,Member owner,Member promoter,BigDecimal amount,BigDecimal distAmount) {

		Card  card = member.card(owner);
		TopicCard topicCard = null;
		if (owner.getTopic()!=null) {
			topicCard = owner.getTopic().getTopicCard();
		}

		//购买后，即成为本店会员

		if (card==null && topicCard!=null) {
			card = new Card();
			card.setOwner(topicCard.getTopic().getMember());
			card.setVip(Card.VIP.vip1);
			card.setType(Card.Type.member);
			card.setBonus(BigDecimal.ZERO);
			card.setStatus(Card.Status.activate);
			card.setTopicCard(topicCard);
			card.setBalance(BigDecimal.ZERO);
			card.setAmount(BigDecimal.ZERO);
			card.setName(member.getName());
			card.setMobile(member.getMobile());
			card.setPoint(0L);
			card.setPromoter(promoter);

			topicCardDao.refresh(topicCard, LockModeType.PESSIMISTIC_WRITE);
			Long no = topicCard.getIncrement() + 1L;
			topicCard.setIncrement(no);
			topicCardDao.merge(topicCard);

			card.getMembers().add(member);
			cardDao.persist(card);

			member.getCards().add(card);
			memberDao.merge(member);

			// 无法认识店铺，使用企业卡
			card.setCode("85" + String.valueOf(topicCard.getId() + 100000000L) + String.valueOf(no + 10200L));
			cardDao.merge(card);
		}

		if (card==null) {
			return null;
		}

		if (!card.getType().equals(Card.Type.member)) {
			return null;
		}

		card.setAmount(card.getAmount().add(amount));

		TopicConfig config = owner.getTopic().getConfig();
		if (config.getPattern().equals(TopicConfig.Pattern.pattern1)) {
			card.setType(Card.Type.team);

			if (promoter!=null) {
				card.setPromoter(promoter);
			}
		} else
		if (config.getPattern().equals(TopicConfig.Pattern.pattern2)) {
			card.setType(Card.Type.team);

			if (promoter!=null) {
				card.setPromoter(promoter);
			}
		} else
		if (config.getPattern().equals(TopicConfig.Pattern.pattern3) && (distAmount.compareTo(BigDecimal.ZERO)>0)) {
			card.setType(Card.Type.team);

			if (promoter!=null) {
				card.setPromoter(promoter);
			}
		} else
		if (config.getPattern().equals(TopicConfig.Pattern.pattern4) && (amount.compareTo(config.getAmount())>=0)) {
			card.setType(Card.Type.team);

			if (promoter!=null) {
				card.setPromoter(promoter);
			}
		} else
		if (config.getPattern().equals(TopicConfig.Pattern.pattern5) && (card.getAmount().compareTo(config.getAmount())>=0)) {
			card.setType(Card.Type.team);

			if (promoter!=null) {
				card.setPromoter(promoter);
			}
		}
		cardDao.merge(card);

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
			cardDao.flush();
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

			if (card.getMobile()!=null && card.getMobile().length()==11) {
				DecimalFormat df=(DecimalFormat) NumberFormat.getInstance();
				df.setMaximumFractionDigits(2);
				String content =
						card.getTopicCard().getTopic().getName()+",会员卡消费"+df.format(payment.getAmount())+"元,余额:"+df.format(card.getBalance());
				smssendService.send(payment.getPayee(), card.getMobile(),content);
			}

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
			cardDao.flush();
			CardBill bill = new CardBill();
			bill.setBalance(card.getBalance());
			bill.setCard(card);
			bill.setCredit(refunds.getAmount());
			bill.setDebit(BigDecimal.ZERO);
			bill.setDeleted(false);
			bill.setType(CardBill.Type.refunds);
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


			if (card.getMobile()!=null && card.getMobile().length()==11) {
				DecimalFormat df=(DecimalFormat) NumberFormat.getInstance();
				df.setMaximumFractionDigits(2);
				String content =
						card.getTopicCard().getTopic().getName()+",会员卡退款"+df.format(refunds.getAmount())+"元,余额:"+df.format(card.getBalance());
				smssendService.send(refunds.getPayee(), card.getMobile(),content);
			}

		} catch (Exception  e) {
			throw  new RuntimeException("支付失败");
		}
	}

	public void addPoint(Card card,Long point,String memo,Order order) {
		if (point>0) {
			cardDao.refresh(card, LockModeType.PESSIMISTIC_WRITE);
			card.setPoint(card.getPoint() + point);
			cardDao.merge(card);
			cardDao.flush();
			CardPointBill bill = new CardPointBill();
			bill.setBalance(card.getPoint());
			bill.setCard(card);
			bill.setCredit(point);
			bill.setDebit(0L);
			bill.setDeleted(false);
			bill.setMemo(memo);
			bill.setOrder(order);
			bill.setOwner(card.getOwner());
			bill.setMember(card.getMembers().get(0));
			cardPointBillDao.persist(bill);
		}
	}

	public void decPoint(Card card, Long point,String memo,Order order) {
		if (point>0) {
			cardDao.refresh(card, LockModeType.PESSIMISTIC_WRITE);
			card.setPoint(card.getPoint() - point);
			if (card.getPoint().compareTo(0L) < 0) {
				throw new RuntimeException("积分余额不足");
			}
			cardDao.merge(card);
			cardDao.flush();
			CardPointBill bill = new CardPointBill();
			bill.setBalance(card.getPoint());
			bill.setCard(card);
			bill.setCredit(0L);
			bill.setDebit(point);
			bill.setDeleted(false);
			bill.setMemo(memo);
			bill.setOrder(order);
			bill.setOwner(card.getOwner());
			bill.setMember(card.getMembers().get(0));
			cardPointBillDao.persist(bill);
		}
	}



	public synchronized Card createAndMember(Receiver receiver,Member owner) {

		Member member = new Member();
		member.setNickName(receiver.getConsignee());
		member.setLogo(null);
		member.setPoint(0L);
		member.setAmount(BigDecimal.ZERO);
		member.setBalance(BigDecimal.ZERO);
		member.setFreezeBalance(BigDecimal.ZERO);
		member.setVip(Member.VIP.vip1);
		member.setIsEnabled(false);
		member.setIsLocked(false);
		member.setLoginFailureCount(0);
		member.setRegisterIp("127.0.0.1");
		memberDao.persist(member);
		receiver.setMember(member);
		receiverDao.persist(receiver);

		memberDao.lock(owner,LockModeType.PESSIMISTIC_WRITE);

		TopicCard topicCard = owner.getTopic().getTopicCard();
		Card card = new Card();
		card.setOwner(owner.getTopic().getMember());
		card.setVip(Card.VIP.vip1);
		card.setType(Card.Type.member);
		card.setBonus(BigDecimal.ZERO);
		card.setStatus(Card.Status.none);
		card.setTopicCard(owner.getTopic().getTopicCard());
		card.setBalance(BigDecimal.ZERO);
		card.setAmount(BigDecimal.ZERO);
		card.setPoint(0L);
		topicCardDao.refresh(topicCard, LockModeType.PESSIMISTIC_WRITE);
		Long no = topicCard.getIncrement() + 1L;
		topicCard.setIncrement(no);
		topicCardDao.merge(topicCard);
		card.setCode("85" + String.valueOf(topicCard.getId() + 100000000L) + String.valueOf(no + 10200L));
		card.setStatus(Card.Status.none);
		card.setShop(null);
		card.setMember(member);

		cardDao.persist(card);

		card.getMembers().add(member);
		cardDao.persist(card);
		if (!member.getCards().contains(card)) {
			member.getCards().add(card);
			memberDao.merge(member);
		}
		return card;

	}

}