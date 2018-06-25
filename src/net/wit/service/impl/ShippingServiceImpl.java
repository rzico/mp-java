package net.wit.service.impl;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.*;

import javax.annotation.Resource;
import javax.persistence.LockModeType;

import net.wit.Filter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.Principal;
import net.wit.Filter.Operator;

import net.wit.dao.*;
import net.wit.service.*;
import org.apache.commons.lang.time.DateUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.wit.entity.*;

/**
 * @ClassName: ShippingDaoImpl
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
 
@Service("shippingServiceImpl")
public class ShippingServiceImpl extends BaseServiceImpl<Shipping, Long> implements ShippingService {

	@Resource(name = "shippingDaoImpl")
	private ShippingDao shippingDao;

	@Resource(name = "memberDaoImpl")
	private MemberDao memberDao;


	@Resource(name = "depositDaoImpl")
	private DepositDao depositDao;

	@Resource(name = "messageServiceImpl")
	private MessageService messageService;

	@Resource(name = "orderServiceImpl")
	private OrderService orderService;

	@Resource(name = "snServiceImpl")
	private SnService snService;

	@Resource(name = "adminServiceImpl")
	private AdminService adminService;

	@Resource(name = "receiverServiceImpl")
	private ReceiverService receiverService;

	@Resource(name = "barrelStockDaoImpl")
	private BarrelStockDao barrelStockDao;

	@Resource(name = "orderLogDaoImpl")
	private OrderLogDao orderLogDao;

	@Resource(name = "shippingDaoImpl")
	public void setBaseDao(ShippingDao shippingDao) {
		super.setBaseDao(shippingDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(Shipping shipping) {
		super.save(shipping);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Shipping update(Shipping shipping) {
		return super.update(shipping);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Shipping update(Shipping shipping, String... ignoreProperties) {
		return super.update(shipping, ignoreProperties);
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
	public void delete(Shipping shipping) {
		super.delete(shipping);
	}

	public Page<Shipping> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return shippingDao.findPage(beginDate,endDate,pageable);
	}


	/**
	 * 根据订单编号查找订单
	 *
	 * @param sn
	 *            订单编号(忽略大小写)
	 * @return 订单，若不存在则返回null
	 */
	public Shipping findBySn(String sn) {
		return shippingDao.findBySn(sn);
	}

	public Shipping create(Order order) {
		
		Shipping shipping = new Shipping();
		shipping.setAddress(order.getAddress());
		shipping.setAreaName(order.getAreaName());
		shipping.setConsignee(order.getConsignee());

		shipping.setMember(order.getMember());
		shipping.setOrder(order);
		shipping.setOrderStatus(Shipping.OrderStatus.unconfirmed);
		shipping.setShippingStatus(Shipping.ShippingStatus.unconfirmed);
		shipping.setPhone(order.getPhone());
		shipping.setSeller(order.getSeller());
		shipping.setZipCode(order.getZipCode());
		shipping.setSn(snService.generate(Sn.Type.shipping));
		shipping.setFreight(BigDecimal.ZERO);
		shipping.setAdminFreight(BigDecimal.ZERO);
		ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
		List<ShippingItem> shippingItems = new ArrayList<>();
		for (OrderItem orderItem:order.getOrderItems()) {
			if (("3".equals(bundle.getString("weex")) || orderItem.getProduct().getType().equals(Product.Type.warehouse))) {
				ShippingItem shippingItem = new ShippingItem();
				shippingItem.setName(orderItem.getName());
				shippingItem.setProduct(orderItem.getProduct());
				shippingItem.setQuantity(orderItem.getQuantity());
				shippingItem.setSn(orderItem.getProduct().getSn());
				shippingItem.setSpec(orderItem.getSpec());
				shippingItem.setThumbnail(orderItem.getThumbnail());
				shippingItem.setShipping(shipping);
				shippingItems.add(shippingItem);
			}
		}

		if (shippingItems.size()==0) {
			return null;
		}

		shipping.setShippingItems(shippingItems);

		shipping.setHopeDate(null);
		if (order.getHopeDate()!=null) {
			if (order.getHopeDate().compareTo(DateUtils.addDays(DateUtils.truncate(new Date(), Calendar.HOUR),-2))>0) {
				shipping.setHopeDate(order.getHopeDate());
			}
		}
		Receiver receiver = receiverService.find(order.getReceiverId());
		if (receiver!=null) {
			shipping.setLevel(receiver.getLevel());
		} else {
			shipping.setLevel(0);
		}

		//结算运费
		shipping.setFreight(
				shipping.calcFreight(receiver)
		);

		shipping.setAdminFreight(
				shipping.calcAdminFreight(receiver)
		);

		if (receiver!=null && receiver.getShop()!=null) {
			shipping.setEnterprise(receiver.getShop().getEnterprise());
			shipping.setShop(receiver.getShop());
			shipping.setAdmin(receiver.getAdmin());
		} else {
			//没有分配，按距离来，选按谁的客户给谁
//			Member member = order.getMember();
//			Card card = member.card(order.getSeller());
//			if (card==null) {
//				card = member.getCards().get(0);
//			}
//			if (card!=null) {
//				Admin admin = adminService.findByMember(card.getOwner());
//				if (admin!=null) {
//					shipping.setEnterprise(admin.getEnterprise());
//					shipping.setShop(admin.getShop());
//				}
//			} else {
				Admin admin = adminService.findByMember(order.getSeller());
				if (admin!=null) {
					shipping.setEnterprise(admin.getEnterprise());
					shipping.setShop(admin.getShop());
				}
//			}
		}

		shippingDao.persist(shipping);
		return shipping;

	}


	public Shipping dispatch(Shipping shipping) throws Exception {

		if (shipping.getAdmin()!=null) {
			shipping.setShippingStatus(Shipping.ShippingStatus.dispatch);
			shipping.setOrderStatus(Shipping.OrderStatus.confirmed);
			OrderLog orderLog = new OrderLog();
			orderLog.setType(OrderLog.Type.shipping);
			orderLog.setOperator("system");
			orderLog.setContent("已指派送货员“"+shipping.getAdmin().getMember().realName()+"”");
			orderLog.setOrder(shipping.getOrder());
			orderLogDao.persist(orderLog);
			messageService.orderMemberPushTo(orderLog);
			messageService.shippingAdminPushTo(shipping,orderLog);
		} else {
			OrderLog orderLog = new OrderLog();
			orderLog.setType(OrderLog.Type.shipping);
			orderLog.setOperator("system");
			orderLog.setContent("订单至"+shipping.getShop().getName()+"");
			orderLog.setOrder(shipping.getOrder());
			orderLogDao.persist(orderLog);
			messageService.shippingPushTo(shipping,orderLog);
		}

		shippingDao.merge(shipping);
		return shipping;
	}

	public Shipping receive(Shipping shipping) throws Exception {
		shipping.setShippingStatus(Shipping.ShippingStatus.receive);
		shipping.setOrderStatus(Shipping.OrderStatus.completed);
		shippingDao.merge(shipping);
		OrderLog orderLog = new OrderLog();
		orderLog.setType(OrderLog.Type.shipping);
		orderLog.setOperator("system");
		orderLog.setContent("您的订单已送达");
		orderLog.setOrder(shipping.getOrder());
		orderLogDao.persist(orderLog);
		messageService.orderMemberPushTo(orderLog);
		return shipping;
	}

	public Shipping completed(Shipping shipping) throws Exception {

			shipping.setShippingStatus(Shipping.ShippingStatus.completed);
			shipping.setOrderStatus(Shipping.OrderStatus.completed);
  			shippingDao.merge(shipping);

  			//记忆楼层和送货点

		    Receiver receiver = receiverService.find(shipping.getOrder().getReceiverId());
		    if (receiver!=null) {
		    	receiver.setLevel(shipping.getLevel());
				receiver.setShop(shipping.getShop());
				receiver.setAdmin(shipping.getAdmin());
				receiverService.update(receiver);
			}

			Member ec = shipping.getEnterprise().getMember();
			for (ShippingBarrel b : shipping.getShippingBarrels()) {
				BarrelStock bs = barrelStockDao.find(ec, b.getBarrel());
				if (bs == null) {
					bs = new BarrelStock();
					bs.setMember(ec);
					bs.setBarrel(b.getBarrel());
					bs.setStock(b.getQuantity() - b.getReturnQuantity());
					bs.setPeriod(0);
					bs.setPledge(BigDecimal.ZERO);
					barrelStockDao.persist(bs);
				} else {
					barrelStockDao.lock(bs, LockModeType.PESSIMISTIC_WRITE);
					bs.setStock(bs.getStock() + b.getQuantity() - b.getReturnQuantity());
					barrelStockDao.merge(bs);
				}
				barrelStockDao.flush();
			}

			//结算配送站运费
		    if (shipping.getEnterprise()!=null) {

		    	//扣销售站
				Member sellerMember = shipping.getSeller();
				memberDao.lock(sellerMember,LockModeType.PESSIMISTIC_WRITE);

				BigDecimal freight = shipping.getFreight();
				sellerMember.setBalance(sellerMember.getBalance().subtract(freight));
				if (sellerMember.getBalance().compareTo(BigDecimal.ZERO)<0) {
					throw new RuntimeException("余额不足不能核销");
				}
				memberDao.merge(sellerMember);

				memberDao.flush();

				Deposit  sellerDeposit = new Deposit();
				sellerDeposit.setBalance(sellerMember.getBalance());
				sellerDeposit.setType(Deposit.Type.freight);
				sellerDeposit.setMemo("支付运费");
				sellerDeposit.setMember(sellerMember);
				sellerDeposit.setCredit(BigDecimal.ZERO.subtract(freight));
				sellerDeposit.setDebit(BigDecimal.ZERO);
				sellerDeposit.setDeleted(false);
				sellerDeposit.setOperator("system");
				sellerDeposit.setOrder(shipping.getOrder());
				sellerDeposit.setSeller(shipping.getSeller());
				depositDao.persist(sellerDeposit);
				messageService.depositPushTo(sellerDeposit);

				//给配送站
				Member shippingMember = shipping.getEnterprise().getMember();
				memberDao.lock(shippingMember,LockModeType.PESSIMISTIC_WRITE);

				shippingMember.setBalance(shippingMember.getBalance().add(freight));
				memberDao.merge(shippingMember);

				memberDao.flush();

				Deposit deposit = new Deposit();
				deposit.setBalance(shippingMember.getBalance());
				deposit.setType(Deposit.Type.freight);
				deposit.setMemo("运费结算");
				deposit.setMember(shippingMember);
				deposit.setCredit(freight);
				deposit.setDebit(BigDecimal.ZERO);
				deposit.setDeleted(false);
				deposit.setOperator("system");
				deposit.setOrder(shipping.getOrder());
				deposit.setSeller(shipping.getSeller());
				depositDao.persist(deposit);
				messageService.depositPushTo(deposit);

				//送水员运费
				if (shipping.getAdmin()!=null) {
					shippingMember.setBalance(shippingMember.getBalance().subtract(shipping.getAdminFreight()));
					if (shippingMember.getBalance().compareTo(BigDecimal.ZERO)<0) {
						throw new RuntimeException("配送站余额不足不能核销");
					}
					memberDao.merge(shippingMember);

					memberDao.flush();

					Deposit adminDeposit = new Deposit();
					adminDeposit.setBalance(shippingMember.getBalance());
					adminDeposit.setType(Deposit.Type.freight);
					adminDeposit.setMemo("支付工资");
					adminDeposit.setMember(shippingMember);
					adminDeposit.setCredit(BigDecimal.ZERO.subtract(shipping.getAdminFreight()));
					adminDeposit.setDebit(BigDecimal.ZERO);
					adminDeposit.setDeleted(false);
					adminDeposit.setOperator("system");
					adminDeposit.setOrder(shipping.getOrder());
					adminDeposit.setSeller(shipping.getSeller());
					depositDao.persist(adminDeposit);
					messageService.depositPushTo(adminDeposit);



					Member adminMember = shipping.getAdmin().getMember();
					memberDao.lock(adminMember,LockModeType.PESSIMISTIC_WRITE);

					adminMember.setBalance(adminMember.getBalance().add(shipping.getAdminFreight()));
					memberDao.merge(adminMember);

					memberDao.flush();

					Deposit wagesDeposit = new Deposit();
					wagesDeposit.setBalance(adminMember.getBalance());
					wagesDeposit.setType(Deposit.Type.wages);
					wagesDeposit.setMemo("送货工资");
					wagesDeposit.setMember(adminMember);
					wagesDeposit.setCredit(shipping.getAdminFreight());
					wagesDeposit.setDebit(BigDecimal.ZERO);
					wagesDeposit.setDeleted(false);
					wagesDeposit.setOperator("system");
					wagesDeposit.setOrder(shipping.getOrder());
					wagesDeposit.setSeller(shipping.getSeller());
					depositDao.persist(wagesDeposit);
					messageService.depositPushTo(wagesDeposit);
				}

			}
		if (shipping.getOrder().equals(Order.OrderStatus.confirmed)) {
			orderService.complete(shipping.getOrder(), null);
		}

		OrderLog orderLog = new OrderLog();
		orderLog.setType(OrderLog.Type.complete);
		orderLog.setOperator("system");
		orderLog.setContent("订单已完成");
		Long d = 0L;
		for (ShippingBarrel b:shipping.getShippingBarrels()) {
			d = d + b.getReturnQuantity();
		}
		if (d>0L) {
			orderLog.setContent(orderLog.getContent()+",回桶数"+String.valueOf(d));
		}
		orderLog.setOrder(shipping.getOrder());
		orderLogDao.persist(orderLog);
		messageService.orderMemberPushTo(orderLog);

		return shipping;

	}

}