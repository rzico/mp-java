package net.wit.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;

import java.util.Date;
import java.util.List;
import javax.persistence.FlushModeType;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.wit.entity.Enterprise;
import net.wit.entity.Payment;
import net.wit.entity.Shop;
import net.wit.entity.summary.PayBillShopSummary;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Repository;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.PayBillDao;
import net.wit.entity.PayBill;


/**
 * @ClassName: PayBillDaoImpl
 * @author 降魔战队
 * @date 2017-11-4 18:12:25
 */
 

@Repository("payBillDaoImpl")
public class PayBillDaoImpl extends BaseDaoImpl<PayBill, Long> implements PayBillDao {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<PayBill>
	 */
	public Page<PayBill> findPage(Date beginDate,Date endDate, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<PayBill> criteriaQuery = criteriaBuilder.createQuery(PayBill.class);
		Root<PayBill> root = criteriaQuery.from(PayBill.class);
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

	public List<PayBillShopSummary> sumPage(Shop shop, Enterprise enterprise, Date beginDate, Date endDate) {
		Date b = DateUtils.truncate(beginDate,Calendar.DATE);
		Date e = DateUtils.truncate(endDate,Calendar.DATE);
		String shopWhr = "";
		if (shop!=null) {
			shopWhr = " and paybill.shop=:shop ";
		}
		if (enterprise!=null) {
			shopWhr = shopWhr + " and paybill.enterprise=:enterprise ";
		}

		String jpql =
				"select paybill.shop,paybill.type,paybill.paymentPluginId,sum(paybill.amount),sum(paybill.couponDiscount),sum(paybill.cardDiscount),sum(paybill.fee),sum(paybill.cardAmount) "+
				"from PayBill paybill where paybill.status in (1,3,4) and paybill.billDate>=:b and paybill.billDate<=:e "+shopWhr+
				"group by paybill.shop,paybill.type,paybill.paymentPluginId order by paybill.shop,paybill.type,paybill.paymentPluginId ";

		Query query = entityManager.createQuery(jpql).
				setFlushMode(FlushModeType.COMMIT).
				setParameter("b", b).
				setParameter("e", e);
		if (shop!=null) {
			query.setParameter("shop",shop);
		}
		if (enterprise!=null) {
			query.setParameter("enterprise",enterprise);
		}

		List result = query.getResultList();
		List<PayBillShopSummary> data = new ArrayList<>();
		for (int i=0;i<result.size();i++) {
			Object[] row = (Object[]) result.get(i);
			PayBillShopSummary rw = new PayBillShopSummary();
			rw.setType((PayBill.Type) row[1]);
			rw.setPaymentPluginId((String) row[2]);
			rw.setAmount((BigDecimal) row[3]);
			rw.setCardDiscount((BigDecimal) row[5]);
			rw.setCouponDiscount((BigDecimal) row[4]);
			rw.setFee((BigDecimal) row[6]);
			rw.setCardAmount((BigDecimal) row[7]);
			Shop sh = (Shop) row[0];
			rw.setShop(sh);
			data.add(rw);
		}
		return data;

	}

}