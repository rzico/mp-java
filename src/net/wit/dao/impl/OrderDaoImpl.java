package net.wit.dao.impl;

import java.util.Calendar;

import java.util.Date;
import java.util.List;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.wit.Filter;
import net.wit.entity.OrderItem;
import net.wit.entity.Product;
import net.wit.entity.ProductStock;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.OrderDao;
import net.wit.entity.Order;

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

}