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
import net.wit.service.MessageService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.wit.entity.*;
import net.wit.service.OrderRankingService;

/**
 * @ClassName: OrderRankingDaoImpl
 * @author 降魔战队
 * @date 2018-4-5 18:22:33
 */
 
 
@Service("orderRankingServiceImpl")
public class OrderRankingServiceImpl extends BaseServiceImpl<OrderRanking, Long> implements OrderRankingService {
	@Resource(name = "orderRankingDaoImpl")
	private OrderRankingDao orderRankingDao;

	@Resource(name = "goodsDaoImpl")
	private GoodsDao goodsDao;

	@Resource(name = "memberDaoImpl")
	private MemberDao memberDao;

	@Resource(name = "cardDaoImpl")
	private CardDao cardDao;

	@Resource(name = "cardPointBillDaoImpl")
	private CardPointBillDao cardPointBillDao;

	@Resource(name = "depositDaoImpl")
	private DepositDao depositDao;

	@Resource(name = "messageServiceImpl")
	private MessageService messageService;

	@Resource(name = "orderRankingDaoImpl")
	public void setBaseDao(OrderRankingDao orderRankingDao) {
		super.setBaseDao(orderRankingDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(OrderRanking orderRanking) {
		super.save(orderRanking);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public OrderRanking update(OrderRanking orderRanking) {
		return super.update(orderRanking);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public OrderRanking update(OrderRanking orderRanking, String... ignoreProperties) {
		return super.update(orderRanking, ignoreProperties);
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
	public void delete(OrderRanking orderRanking) {
		super.delete(orderRanking);
	}

	public Page<OrderRanking> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return orderRankingDao.findPage(beginDate,endDate,pageable);
	}

	private void calc(Member member,Member seller,BigDecimal amount,Long point,OrderItem orderItem) {
		Goods goods = orderItem.getProduct().getGoods();
		goodsDao.refresh(goods, LockModeType.PESSIMISTIC_WRITE);
		goods.setRanking(goods.getRanking()+1);
		goodsDao.merge(goods);
		goodsDao.flush();
        OrderRanking orderRanking = new OrderRanking();
        orderRanking.setAmount(amount);
        orderRanking.setPoint(point);
        orderRanking.setMember(member);
        orderRanking.setOwner(seller);
        orderRanking.setGoods(orderItem.getProduct().getGoods());
        orderRanking.setName(orderItem.getName());
        orderRanking.setSpec(orderItem.getSpec());
        orderRanking.setOrders(goods.getRanking().intValue());
        orderRankingDao.persist(orderRanking);
        if (goods.getRanking()>2 && ((goods.getRanking()-1) % 2 ==0)) {
           List<Filter> filters = new ArrayList<>();
           filters.add(new Filter("goods",Operator.eq,goods));
           List<OrderRanking> ors = orderRankingDao.findList(null,1,filters,null);
           if (ors.size()>0) {
           	  OrderRanking rk = ors.get(0);
           	  if (rk.getOrders()<goods.getRanking()) {

				  //条件成立，出局一个,商家需有余额
				  Member ow = rk.getOwner();
				  memberDao.refresh(ow,LockModeType.PESSIMISTIC_WRITE);

				  ow.setBalance(ow.getBalance().subtract(ow.getAmount()));
				  if (ow.getBalance().compareTo(BigDecimal.ZERO)<0) {
				  	throw  new RuntimeException("商家余额不足");
				  }
				  memberDao.merge(ow);
				  memberDao.flush();

				  Deposit owdeposit = new Deposit();
				  owdeposit.setBalance(ow.getBalance());
				  owdeposit.setType(Deposit.Type.product);
				  owdeposit.setMemo("利润分红(扣款)");
				  owdeposit.setMember(ow);
				  owdeposit.setCredit(BigDecimal.ZERO.subtract(rk.getAmount()));
				  owdeposit.setDebit(BigDecimal.ZERO);
				  owdeposit.setDeleted(false);
				  owdeposit.setOperator("system");
				  owdeposit.setOrder(orderItem.getOrder());
				  owdeposit.setSeller(seller);
				  depositDao.persist(owdeposit);
				  messageService.depositPushTo(owdeposit);

           	  	  //条件成立，出局一个
                  Member rm = rk.getMember();
                  memberDao.refresh(rm,LockModeType.PESSIMISTIC_WRITE);
                  rm.setBalance(rm.getBalance().add(rk.getAmount()));
                  memberDao.merge(rm);
                  memberDao.flush();

				  Deposit deposit = new Deposit();
				  deposit.setBalance(rm.getBalance());
				  deposit.setType(Deposit.Type.rebate);
				  deposit.setMemo("消费分红");
				  deposit.setMember(rm);
				  deposit.setCredit(rk.getAmount());
				  deposit.setDebit(BigDecimal.ZERO);
				  deposit.setDeleted(false);
				  deposit.setOperator("system");
				  deposit.setOrder(orderItem.getOrder());
				  deposit.setSeller(seller);
				  depositDao.persist(deposit);
				  messageService.depositPushTo(deposit);

                  Card card = rm.card(seller);
                  if (card!=null) {
                  	 cardDao.refresh(card,LockModeType.PESSIMISTIC_WRITE);
                  	 card.setPoint(card.getPoint()+rk.getPoint());
                  	 cardDao.merge(card);
                  	 cardDao.flush();
                  	 CardPointBill pointBill = new CardPointBill();
                  	 pointBill.setBalance(card.getPoint());
                  	 pointBill.setCard(card);
                  	 pointBill.setCredit(rk.getPoint());
                  	 pointBill.setDebit(0L);
                  	 pointBill.setDeleted(false);
                  	 pointBill.setMemo("消费分红");
                  	 pointBill.setOrder(orderItem.getOrder());
                  	 pointBill.setOwner(seller);
                  	 pointBill.setShop(null);
                  	 pointBill.setOperator("system");
                  	 cardPointBillDao.persist(pointBill);
				  }

				  orderRankingDao.remove(rk);
				  orderRankingDao.flush();
			  }

		   }
		}
	}

	public synchronized void add(Order order) throws Exception {

		for (OrderItem orderItem:order.getOrderItems()) {
			Distribution distribution = orderItem.getProduct().getDistribution();
			if (distribution!=null && distribution.getType().equals(Distribution.Type.global)) {

			   int i = Math.round(orderItem.getSubtotal().divide(distribution.getDividend(),0,BigDecimal.ROUND_DOWN).floatValue());

			   int point = Math.round(distribution.getDividend().multiply(distribution.getPercent1().multiply(new BigDecimal("0.01"))).setScale(0,BigDecimal.ROUND_DOWN).floatValue());
			   BigDecimal amount = new BigDecimal(point);

			   for (int r=0;r<i;r++) {
			   	   calc(order.getMember(),order.getSeller(),amount,new Long(point),orderItem);
				}

			}
		}

	}
}