package net.wit.dao.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;

import java.util.Date;
import java.util.List;
import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import javafx.scene.shape.StrokeLineJoin;
import net.wit.entity.Member;
import net.wit.entity.summary.OrderItemSummary;
import net.wit.entity.summary.OrderSummary;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.OrderItemDao;
import net.wit.entity.OrderItem;


/**
 * @ClassName: OrderItemDaoImpl
 * @author 降魔战队
 * @date 2017-9-14 19:42:5
 */
 

@Repository("orderItemDaoImpl")
public class OrderItemDaoImpl extends BaseDaoImpl<OrderItem, Long> implements OrderItemDao {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<OrderItem>
	 */
	public Page<OrderItem> findPage(Date beginDate,Date endDate, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<OrderItem> criteriaQuery = criteriaBuilder.createQuery(OrderItem.class);
		Root<OrderItem> root = criteriaQuery.from(OrderItem.class);
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
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery,pageable);
	}


	public List<OrderItemSummary> summary(Member member, Date beginDate, Date endDate, Pageable pageable) {
		Date b = DateUtils.truncate(beginDate,Calendar.DATE);
		Date e = DateUtils.truncate(endDate,Calendar.DATE);
		e =DateUtils.addDays(e,1);
		String jpql =
				"select orderItem.product,orderItem.name,orderItem.spec,sum(orderItem.quantity),sum(orderItem.quantity * orderItem.price),sum(orderItem.quantity * orderItem.cost) "+
						"from wx_order_item orderItem,wx_order orders where orderItem.orders=orders.id and orders.shipping_date>=? and orders.shipping_date<? and orders.seller=? and orders.shipping_status<>0 "+
						"group by orderItem.product,orderItem.name,orderItem.spec order by orderItem.product ";

		Query query = entityManager.createNativeQuery(jpql).
				setFlushMode(FlushModeType.COMMIT).
				setParameter(1, b).
				setParameter(2, e).
				setParameter(3,member);
		query.setFirstResult(pageable.getPageStart());
		query.setMaxResults(pageable.getPageSize());
		List result = query.getResultList();
		List<OrderItemSummary> data = new ArrayList<>();
		for (int i=0;i<result.size();i++) {
			Object[] row = (Object[]) result.get(i);
			OrderItemSummary rw = new OrderItemSummary();
			BigInteger bi = (BigInteger) row[0];
			if (bi!=null) {
				rw.setProduct(bi.longValue());
				rw.setName((String) row[1] + (String) row[2]);
				BigDecimal bd = (BigDecimal) row[3];
				rw.setQuantity(bd.intValue());
				rw.setAmount((BigDecimal) row[4]);
				rw.setCost((BigDecimal) row[5]);
				data.add(rw);
			}
		}
		return data;

	}

}