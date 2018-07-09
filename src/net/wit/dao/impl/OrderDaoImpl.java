package net.wit.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;

import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.wit.Filter;
import net.wit.entity.*;
import net.wit.entity.summary.DepositSummary;
import net.wit.entity.summary.OrderSummary;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.OrderDao;

/**
 * @ClassName: OrderDaoImpl
 * @author 降魔战队
 * @date 2017-9-14 19:42:5
 */

@Repository("orderDaoImpl")
public class OrderDaoImpl extends BaseDaoImpl<Order, Long> implements OrderDao {

	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<Order>
	 */
	public Page<Order> findPage(Date beginDate,Date endDate, String status, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
		Root<Order> root = criteriaQuery.from(Order.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.conjunction();
		if (beginDate!=null) {
			Date b = DateUtils.truncate(beginDate,Calendar.DATE);
			restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("createDate"), b));
		}
		if (endDate!=null) {
			Date e = DateUtils.truncate(endDate,Calendar.DATE);
			e =DateUtils.addDays(e,1);
			restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.lessThan(root.<Date> get("createDate"), e));
		}
		if (status!=null) {
			if ("unpaid".equals(status)) {
				restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.equal(root.<Boolean> get("orderStatus"), Order.OrderStatus.unconfirmed));
				restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.equal(root.<Boolean> get("paymentStatus"), Order.PaymentStatus.unpaid));
			}
			if ("unshipped".equals(status)) {
				restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.equal(root.<Boolean> get("orderStatus"), Order.OrderStatus.confirmed));
				restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.notEqual(root.<Boolean> get("paymentStatus"), Order.PaymentStatus.refunding));
				restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.notEqual(root.<Boolean> get("paymentStatus"), Order.PaymentStatus.refunded));
				restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.equal(root.<Boolean> get("shippingStatus"), Order.ShippingStatus.unshipped));
			}
			if ("shipped".equals(status)) {
				restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.equal(root.<Boolean> get("orderStatus"), Order.OrderStatus.confirmed));
				restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.equal(root.<Boolean> get("shippingStatus"), Order.ShippingStatus.shipped));
			}
			if ("refunding".equals(status)) {
				restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.equal(root.<Boolean> get("orderStatus"), Order.OrderStatus.confirmed));
				restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.or(criteriaBuilder.equal(root.<Boolean> get("shippingStatus"), Order.ShippingStatus.returning),criteriaBuilder.equal(root.<Boolean> get("paymentStatus"), Order.PaymentStatus.refunding)));
			}
			if ("completed".equals(status)) {
				restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.equal(root.<Boolean> get("orderStatus"), Order.OrderStatus.completed));
			}
			if ("cancelled".equals(status)) {
				restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.equal(root.<Boolean> get("orderStatus"), Order.OrderStatus.cancelled));
			}
			if ("pending".equals(status)) {
				restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.or(criteriaBuilder.equal(root.get("orderStatus"), Order.OrderStatus.unconfirmed),criteriaBuilder.equal(root.get("orderStatus"), Order.OrderStatus.confirmed)  ));
			}
		}
		restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.equal(root.<Boolean> get("deleted"), false));
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery,pageable);
	}

	/**
	 * @Title：count
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param filters
	 * @return Page<Order>
	 */
	public Long count(Date beginDate,Date endDate, String status,List<Filter> filters) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
		Root<Order> root = criteriaQuery.from(Order.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.conjunction();
		if (beginDate!=null) {
			Date b = DateUtils.truncate(beginDate,Calendar.DATE);
			restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("createDate"), b));
		}
		if (endDate!=null) {
			Date e = DateUtils.truncate(endDate,Calendar.DATE);
			e =DateUtils.addDays(e,1);
			restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.lessThan(root.<Date> get("createDate"), e));
		}
		if (status!=null) {
			if ("unpaid".equals(status)) {
				restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.equal(root.<Boolean> get("orderStatus"), Order.OrderStatus.unconfirmed));
				restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.equal(root.<Boolean> get("paymentStatus"), Order.PaymentStatus.unpaid));
			}
			if ("unshipped".equals(status)) {
				restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.equal(root.<Boolean> get("orderStatus"), Order.OrderStatus.confirmed));
				restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.notEqual(root.<Boolean> get("paymentStatus"), Order.PaymentStatus.refunding));
				restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.notEqual(root.<Boolean> get("paymentStatus"), Order.PaymentStatus.refunded));
				restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.equal(root.<Boolean> get("shippingStatus"), Order.ShippingStatus.unshipped));
			}
			if ("shipped".equals(status)) {
				restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.equal(root.<Boolean> get("orderStatus"), Order.OrderStatus.confirmed));
				restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.equal(root.<Boolean> get("shippingStatus"), Order.ShippingStatus.shipped));
			}
			if ("refunding".equals(status)) {
				restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.equal(root.<Boolean> get("orderStatus"), Order.OrderStatus.confirmed));
				restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.or(criteriaBuilder.equal(root.<Boolean> get("shippingStatus"), Order.ShippingStatus.returning),criteriaBuilder.equal(root.<Boolean> get("paymentStatus"), Order.PaymentStatus.refunding)));
			}
			if ("completed".equals(status)) {
				restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.equal(root.<Boolean> get("orderStatus"), Order.OrderStatus.completed));
			}
			if ("cancelled".equals(status)) {
				restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.equal(root.<Boolean> get("orderStatus"), Order.OrderStatus.cancelled));
			}
		}
		restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.equal(root.<Boolean> get("deleted"), false));
		criteriaQuery.where(restrictions);
		return super.count(criteriaQuery,filters);
	}



	public Order findBySn(String sn) {
		if (sn == null) {
			return null;
		}
		String jpql = "select orders from Order orders where lower(orders.sn) = lower(:sn)";
		try {
			return entityManager.createQuery(jpql, Order.class).setFlushMode(FlushModeType.COMMIT).setParameter("sn", sn).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public List<OrderSummary> summary(Member member, Date beginDate, Date endDate, Pageable pageable) {
		Date b = DateUtils.truncate(beginDate,Calendar.DATE);
		Date e = DateUtils.truncate(endDate,Calendar.DATE);
		e =DateUtils.addDays(e,1);
		String jpql =
				"select sum(orders.fee) as fee,sum(orders.freight) as freight,sum(orders.point_discount) as pointDiscount,sum(orders.coupon_discount) as couponDiscount,"+
						"sum(orders.exchange_discount) as exchangeDiscount,sum(orders.offset_amount) as offsetAmount,sum(orders.amount_payable) as amountPayable,"+
						"sum(orders.shipping_freight) as shippingFreight,sum(orders.admin_freight) as adminFreight,sum(orders.level_freight) as levelFreight "+
						"from wx_order orders where orders.shipping_date>=? and orders.shipping_date<? and orders.seller=? and orders.shipping_status<>0 "+
						" ";
		Query query = entityManager.createNativeQuery(jpql).
				setFlushMode(FlushModeType.COMMIT).
				setParameter(1, b).
				setParameter(2, e).
				setParameter(3,member);
		List result = query.getResultList();
		List<OrderSummary> data = new ArrayList<>();
		for (int i=0;i<result.size();i++) {
			Object[] row = (Object[]) result.get(i);
			OrderSummary rw = new OrderSummary();
			rw.setFee((BigDecimal) row[0]);
			rw.setFreight((BigDecimal) row[1]);
			rw.setPointDiscount((BigDecimal) row[2]);
			rw.setCouponDiscount((BigDecimal) row[3]);
			rw.setExchangeDiscount((BigDecimal) row[4]);
			rw.setOffsetAmount((BigDecimal) row[5]);
			BigDecimal amountPayable = (BigDecimal) row[6];
			if (amountPayable!=null){
				rw.setPrice(amountPayable.add(rw.getPointDiscount()).add(rw.getCouponDiscount()).add(rw.getExchangeDiscount()).subtract(rw.getOffsetAmount()).subtract(rw.getFreight()));
				rw.setAmount(amountPayable.add(rw.getPointDiscount()).add(rw.getCouponDiscount()).add(rw.getExchangeDiscount()));
				rw.setShippingFreight((BigDecimal) row[7]);
				rw.setProfit(rw.getAmount().subtract(rw.getFee()).subtract(rw.getShippingFreight()));
				data.add(rw);
			}
		}
		return data;

	}


}