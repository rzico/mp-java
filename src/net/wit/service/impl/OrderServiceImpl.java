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

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.wit.dao.OrderDao;
import net.wit.entity.*;
import net.wit.service.OrderService;

/**
 * @ClassName: OrderDaoImpl
 * @author 降魔战队
 * @date 2017-9-14 19:42:8
 */
 
 
@Service("orderServiceImpl")
public class OrderServiceImpl extends BaseServiceImpl<Order, Long> implements OrderService {
	@Resource(name = "orderDaoImpl")
	private OrderDao orderDao;

	@Resource(name = "orderDaoImpl")
	public void setBaseDao(OrderDao orderDao) {
		super.setBaseDao(orderDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(Order order) {
		super.save(order);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Order update(Order order) {
		return super.update(order);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Order update(Order order, String... ignoreProperties) {
		return super.update(order, ignoreProperties);
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
	public void delete(Order order) {
		super.delete(order);
	}

	public Page<Order> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return orderDao.findPage(beginDate,endDate,pageable);
	}

	/**
	 * 根据订单编号查找订单
	 *
	 * @param sn
	 *            订单编号(忽略大小写)
	 * @return 订单，若不存在则返回null
	 */
	public Order findBySn(String sn) {
		return orderDao.findBySn(sn);
	}

	/**
	 * 释放过期订单库存
	 */
	public void releaseStock() {
		orderDao.releaseStock();
	}

	/**
	 * 生成订单
	 *
	 * @param cart
	 *            购物车
	 * @param receiver
	 *            收货地址
	 * @param couponCode
	 *            优惠码
	 * @param memo
	 *            附言
	 * @return 订单
	 */
	public Order build(Cart cart, Receiver receiver, CouponCode couponCode, String memo) {
       return null;
	}

	/**
	 * 创建订单
	 *
	 * @param cart
	 *            购物车
	 * @param receiver
	 *            收货地址
	 * @param couponCode
	 *            优惠码
	 * @param memo
	 *            附言
	 * @param operator
	 *            操作员
	 * @return 订单
	 */
	public Order create(Cart cart, Receiver receiver, CouponCode couponCode, String memo, Admin operator) {
		return null;
	}

	/**
	 * 更新订单
	 *
	 * @param order
	 *            订单
	 * @param operator
	 *            操作员
	 */
	public void update(Order order, Admin operator) {
		return ;
	}

	/**
	 * 订单确认
	 *
	 * @param order
	 *            订单
	 * @param operator
	 *            操作员
	 */
	public void confirm(Order order, Admin operator) {
		return ;
	}

	/**
	 * 订单完成
	 *
	 * @param order
	 *            订单
	 * @param operator
	 *            操作员
	 */
	public void complete(Order order, Admin operator) {
		return ;
	}

	/**
	 * 订单取消
	 *
	 * @param order
	 *            订单
	 * @param operator
	 *            操作员
	 */
	public void cancel(Order order, Admin operator) {
		return ;
	}

	/**
	 * 订单支付
	 *
	 * @param order
	 *            订单
	 * @param payment
	 *            收款单
	 * @param operator
	 *            操作员
	 */
	public void payment(Order order, Payment payment, Admin operator) {
		return;
	}

	/**
	 * 订单退款
	 *
	 * @param order
	 *            订单
	 * @param refunds
	 *            退款单
	 * @param operator
	 *            操作员
	 */
	public void refunds(Order order, Refunds refunds, Admin operator) {
		return;
	}

	/**
	 * 订单发货
	 *
	 * @param order
	 *            订单
	 * @param operator
	 *            操作员
	 */
	public void shipping(Order order, Admin operator) {
		return;
	}

	/**
	 * 订单退货
	 *
	 * @param order
	 *            订单
	 * @param operator
	 *            操作员
	 */
	public void returns(Order order, Admin operator) {
		return;
	}


}