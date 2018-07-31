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



	public List<BarrelSummary> summary(Enterprise enterprise, Date beginDate, Date endDate,String type, Pageable pageable) {

		Date b = DateUtils.truncate(beginDate,Calendar.DATE);
		Date e = DateUtils.truncate(endDate,Calendar.DATE);
		e =DateUtils.addDays(e,1);
		Query query = null;
		String jpql = "";

//		if ("owner".equals(type)) {

			jpql = "select ep.member as memberId,barrel.name as name,sum(barrel.quantity) as quantity,sum(barrel.return_quantity) as return_quantity,0 as s_quantity,0 as s_return_quantity " +
					"from wx_shipping_barrel barrel,wx_enterprise ep where barrel.enterprise=ep.id and barrel.seller=? and barrel.create_date>=? and barrel.create_date<?  " +
					"group by ep.member,barrel.name ";
		    jpql = jpql + " union all ";
	    	jpql = jpql + "select barrel.seller as memberId,barrel.name,0 as quantity,0 as return_quantity,sum(barrel.quantity) as s_quantity,sum(barrel.return_quantity) as s_return_quantity  " +
			    	"from wx_shipping_barrel barrel where barrel.enterprise=? and barrel.create_date>=? and barrel.create_date<?  " +
			    	"group by barrel.seller,barrel.name ";
	    	jpql = " select memberId,name,sum(quantity),sum(return_quantity),sum(s_quantity),sum(s_return_quantity) from("+jpql+") as j group by memberId,name order by memberId";

			query = entityManager.createNativeQuery(jpql).
					setFlushMode(FlushModeType.COMMIT).
					setParameter(1, enterprise.getMember()).
					setParameter(2, b).
					setParameter(3, e).
					setParameter(4, enterprise).
					setParameter(5, b).
					setParameter(6, e);

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
				BigDecimal bq = (BigDecimal) row[2];
				rw.setQuantity(bq.intValue());
				BigDecimal br = (BigDecimal) row[3];
				rw.setReturnQuantity(br.intValue());
				BigDecimal Sbq = (BigDecimal) row[4];
				rw.setsQuantity(Sbq.intValue());
				BigDecimal Sbr = (BigDecimal) row[5];
				rw.setsReturnQuantity(Sbr.intValue());
				data.add(rw);
			}
		}
		return data;
	}


	public List<BarrelSummary> summary_barrel(Enterprise enterprise,Date beginDate, Date endDate,String type, Pageable pageable) {
		Date b = DateUtils.truncate(beginDate,Calendar.DATE);
		Date e = DateUtils.truncate(endDate,Calendar.DATE);
		e =DateUtils.addDays(e,1);
		Query query = null;
		String jpql = "";

		jpql = "select barrel.name as name,sum(barrel.quantity) as quantity,sum(barrel.return_quantity) as return_quantity,0 as s_quantity,0 as s_return_quantity " +
				"from wx_shipping_barrel barrel where barrel.seller=? and barrel.create_date>=? and barrel.create_date<?  " +
				"group by barrel.name";
		jpql = jpql + " union all ";
		jpql = jpql + "select barrel.name as name,0 as quantity,0 as return_quantity,sum(barrel.quantity) as s_quantity,sum(barrel.return_quantity) as s_return_quantity " +
				"from wx_shipping_barrel barrel where barrel.enterprise=? and barrel.create_date>=? and barrel.create_date<?  " +
				"group by barrel.name";
		jpql = " select name,sum(quantity),sum(return_quantity),sum(s_quantity),sum(s_return_quantity) from ("+jpql+") j group by name order by name";

		query = entityManager.createNativeQuery(jpql).
							setFlushMode(FlushModeType.COMMIT).
							setParameter(1, enterprise.getMember()).
							setParameter(2, b).
							setParameter(3, e).
					setParameter(4, enterprise).
					setParameter(5, b).
					setParameter(6, e);

		List result = query.getResultList();
		List<BarrelSummary> data = new ArrayList<>();
		for (int i=0;i<result.size();i++) {
			Object[] row = (Object[]) result.get(i);
			BarrelSummary rw = new BarrelSummary();
			rw.setBarrelName((String) row[0]);
			BigDecimal bq = (BigDecimal) row[1];
			rw.setQuantity(bq.intValue());
			BigDecimal br = (BigDecimal) row[2];
			rw.setReturnQuantity(br.intValue());
			BigDecimal Sbq = (BigDecimal) row[3];
			rw.setsQuantity(Sbq.intValue());
			BigDecimal Sbr = (BigDecimal) row[4];
			rw.setsReturnQuantity(Sbr.intValue());
			if (rw.getBarrelName()!=null) {
				data.add(rw);
			}
		}
		return data;
	}
}