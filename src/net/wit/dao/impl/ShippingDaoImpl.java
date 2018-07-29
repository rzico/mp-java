package net.wit.dao.impl;

import java.math.BigDecimal;
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
import net.wit.entity.Order;
import net.wit.entity.summary.OrderSummary;
import net.wit.entity.summary.ShippingSummary;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.ShippingDao;
import net.wit.entity.Shipping;


/**
 * @ClassName: ShippingDaoImpl
 * @author 降魔战队
 * @date 2017-9-14 19:42:5
 */
 

@Repository("shippingDaoImpl")
public class ShippingDaoImpl extends BaseDaoImpl<Shipping, Long> implements ShippingDao {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<Shipping>
	 */
	public Page<Shipping> findPage(Date beginDate,Date endDate, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Shipping> criteriaQuery = criteriaBuilder.createQuery(Shipping.class);
		Root<Shipping> root = criteriaQuery.from(Shipping.class);
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



	public Shipping findBySn(String sn) {
		if (sn == null) {
			return null;
		}
		String jpql = "select shippings from Shipping shippings where lower(shippings.sn) = lower(:sn)";
		try {
			return entityManager.createQuery(jpql, Shipping.class).setFlushMode(FlushModeType.COMMIT).setParameter("sn", sn).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}


	public List<ShippingSummary> summary(Enterprise enterprise, Date beginDate, Date endDate,String type,  Pageable pageable) {
		Date b = DateUtils.truncate(beginDate,Calendar.DATE);
		Date e = DateUtils.truncate(endDate,Calendar.DATE);
		e =DateUtils.addDays(e,1);
		String jpql = "";
		Query query = null;
		    if ("owner".equals(type)) {
				jpql = "select sum(shipping.cost) as cost," +
						"sum(shipping.shipping_freight) as shippingFreight,sum(shipping.admin_freight) as adminFreight,sum(shipping.level_freight) as levelFreight " +
						"from wx_shipping shipping where shipping.create_date>=? and shipping.create_date<? and shipping.seller=? " +
						"  ";
				query = entityManager.createNativeQuery(jpql).
						setFlushMode(FlushModeType.COMMIT).
						setParameter(1, b).
						setParameter(2, e).
						setParameter(3,enterprise.getMember());
			} else {
				jpql = "select sum(shipping.cost) as cost," +
						"sum(shipping.shipping_freight) as shippingFreight,sum(shipping.admin_freight) as adminFreight,sum(shipping.level_freight) as levelFreight " +
						"from wx_shipping shipping where shipping.create_date>=? and shipping.create_date<? and shipping.enterprise=? " +
						"  ";
				query = entityManager.createNativeQuery(jpql).
						setFlushMode(FlushModeType.COMMIT).
						setParameter(1, b).
						setParameter(2, e).
						setParameter(3,enterprise);
			}
		List result = query.getResultList();
		List<ShippingSummary> data = new ArrayList<>();
		for (int i=0;i<result.size();i++) {
			Object[] row = (Object[]) result.get(i);
			ShippingSummary rw = new ShippingSummary();
			rw.setCost((BigDecimal) row[0]);
			rw.setShippingFreight((BigDecimal) row[1]);
			rw.setAdminFreight((BigDecimal) row[2]);
			rw.setLevelFreight((BigDecimal) row[3]);
			if (rw.getAdminFreight()!=null){
				rw.setAdminFreight(rw.getAdminFreight().subtract(rw.getLevelFreight()));
				rw.setProfit(rw.getShippingFreight().subtract(rw.getAdminFreight()).subtract(rw.getLevelFreight()));
				data.add(rw);
			}
		}
		return data;

	}

}