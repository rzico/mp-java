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

import net.wit.entity.Enterprise;
import net.wit.entity.Member;
import net.wit.entity.summary.OrderItemSummary;
import net.wit.entity.summary.ShippingItemSummary;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.ShippingItemDao;
import net.wit.entity.ShippingItem;


/**
 * @ClassName: ShippingItemDaoImpl
 * @author 降魔战队
 * @date 2017-9-14 19:42:5
 */
 

@Repository("shippingItemDaoImpl")
public class ShippingItemDaoImpl extends BaseDaoImpl<ShippingItem, Long> implements ShippingItemDao {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<ShippingItem>
	 */
	public Page<ShippingItem> findPage(Date beginDate,Date endDate, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<ShippingItem> criteriaQuery = criteriaBuilder.createQuery(ShippingItem.class);
		Root<ShippingItem> root = criteriaQuery.from(ShippingItem.class);
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


	public List<ShippingItemSummary> summary(Enterprise enterprise,Member seller, Date beginDate, Date endDate, Pageable pageable) {
		Date b = DateUtils.truncate(beginDate,Calendar.DATE);
		Date e = DateUtils.truncate(endDate,Calendar.DATE);
		e =DateUtils.addDays(e,1);
		String jpql =
				"select  shippingItem.product,shippingItem.name,shippingItem.spec,sum(shippingItem.quantity),sum(shippingItem.quantity * shippingItem.price),sum(shippingItem.quantity * shippingItem.cost) "+
						"from wx_shipping_item shippingItem,wx_shipping shipping where shippingItem.shipping=shipping.id and shipping.create_date>=? and shipping.create_date<? and shipping.enterprise=? and shipping.,Member seller=? "+
						"group by shippingItem.product,orderItem.name,shippingItem.spec order by shippingItem.product ";

		Query query = entityManager.createNativeQuery(jpql).
				setFlushMode(FlushModeType.COMMIT).
				setParameter(1, b).
				setParameter(2, e).
				setParameter(3,enterprise).
				setParameter(4,seller);
		query.setFirstResult(pageable.getPageStart());
		query.setMaxResults(pageable.getPageStart()+pageable.getPageSize());
		List result = query.getResultList();
		List<ShippingItemSummary> data = new ArrayList<>();
		for (int i=0;i<result.size();i++) {
			Object[] row = (Object[]) result.get(i);
			ShippingItemSummary rw = new ShippingItemSummary();
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