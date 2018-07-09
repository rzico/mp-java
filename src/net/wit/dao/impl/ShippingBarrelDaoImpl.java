package net.wit.dao.impl;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.BarrelStockDao;
import net.wit.dao.ShippingBarrelDao;
import net.wit.entity.BarrelStock;
import net.wit.entity.Enterprise;
import net.wit.entity.Member;
import net.wit.entity.ShippingBarrel;
import net.wit.entity.summary.BarrelSummary;
import net.wit.entity.summary.PaymentSummary;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.FlushModeType;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * @ClassName: ShippingBarrelDaoImpl
 * @author 降魔战队
 * @date 2018-5-28 15:8:41
 */
 

@Repository("shippingBarrelDaoImpl")
public class ShippingBarrelDaoImpl extends BaseDaoImpl<ShippingBarrel, Long> implements ShippingBarrelDao {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<BarrelStock>
	 */
	public Page<ShippingBarrel> findPage(Date beginDate,Date endDate, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<ShippingBarrel> criteriaQuery = criteriaBuilder.createQuery(ShippingBarrel.class);
		Root<ShippingBarrel> root = criteriaQuery.from(ShippingBarrel.class);
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



	public List<BarrelSummary> summary(Enterprise enterprise, Date beginDate, Date endDate, Pageable pageable) {
		Date b = DateUtils.truncate(beginDate,Calendar.DATE);
		Date e = DateUtils.truncate(endDate,Calendar.DATE);
		e =DateUtils.addDays(e,1);
		String jpql =	"select barrel.seller,barrel.name,sum(barrel.quantity),sum(barrel.return_quantity) "+
						"from wx_shipping_barrel barrel where barrel.enterprise=? and barrel.create_date>=? and barrel.create_date<?  "+
						"group by barrel.seller,barrel.name order by barrel.seller";

		Query query = entityManager.createNativeQuery(jpql).
				setFlushMode(FlushModeType.COMMIT).
				setParameter(1, enterprise).
				setParameter(2, b).
				setParameter(3, e);
		query.setFirstResult(pageable.getPageStart());
		query.setMaxResults(pageable.getPageStart()+pageable.getPageSize());
		List result = query.getResultList();
		List<BarrelSummary> data = new ArrayList<>();
		for (int i=0;i<result.size();i++) {
			Object[] row = (Object[]) result.get(i);
			BarrelSummary rw = new BarrelSummary();
			BigInteger bi = (BigInteger) row[0];
			if (bi!=null) {
				rw.setSellerId(bi.longValue());
				rw.setBarrelName((String) row[1]);
				rw.setQuantity((Integer) row[2]);
				rw.setReturnQuantity((Integer) row[3]);
				data.add(rw);
			}
		}
		return data;
	}


	public List<BarrelSummary> summary_barrel(Enterprise enterprise,Date beginDate, Date endDate, Pageable pageable) {
		Date b = DateUtils.truncate(beginDate,Calendar.DATE);
		Date e = DateUtils.truncate(endDate,Calendar.DATE);
		e =DateUtils.addDays(e,1);
		String jpql =
				"select barrel.name,sum(barrel.quantity),sum(barrel.return_quantity) "+
				"from wx_shipping_barrel barrel where barrel.enterprise=? and barrel.create_date>=? and barrel.create_date<?  "+
				"group by barrel.name order by barrel.name";

		Query query = entityManager.createNativeQuery(jpql).
				setFlushMode(FlushModeType.COMMIT).
				setParameter(1, enterprise).
				setParameter(2, b).
				setParameter(3, e);
		List result = query.getResultList();
		List<BarrelSummary> data = new ArrayList<>();
		for (int i=0;i<result.size();i++) {
			Object[] row = (Object[]) result.get(i);
			BarrelSummary rw = new BarrelSummary();
			rw.setBarrelName((String) row[0]);
			rw.setQuantity((Integer) row[1]);
			rw.setReturnQuantity((Integer) row[2]);
			if (rw.getBarrelName()!=null) {
				data.add(rw);
			}
		}
		return data;
	}
}