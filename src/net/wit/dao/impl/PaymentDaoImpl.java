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

import net.wit.entity.Member;
import net.wit.entity.summary.OrderItemSummary;
import net.wit.entity.summary.PaymentSummary;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.PaymentDao;
import net.wit.entity.Payment;


/**
 * @ClassName: PaymentDaoImpl
 * @author 降魔战队
 * @date 2017-9-14 19:42:5
 */
 

@Repository("paymentDaoImpl")
public class PaymentDaoImpl extends BaseDaoImpl<Payment, Long> implements PaymentDao {

	public Payment findBySn(String sn) {
		if (sn == null) {
			return null;
		}
		String jpql = "select payment from Payment payment where lower(payment.sn) = lower(:sn)";
		try {
			return entityManager.createQuery(jpql, Payment.class).setFlushMode(FlushModeType.COMMIT).setParameter("sn", sn).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public Page<Payment> findPage(Date beginDate, Date endDate, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Payment> criteriaQuery = criteriaBuilder.createQuery(Payment.class);
		Root<Payment> root = criteriaQuery.from(Payment.class);
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



	public List<PaymentSummary> summary(Member member,Date beginDate, Date endDate, Pageable pageable) {
		Date b = DateUtils.truncate(beginDate,Calendar.DATE);
		Date e = DateUtils.truncate(endDate,Calendar.DATE);
		e =DateUtils.addDays(e,1);
		String jpql =
				"select member,payment_method,sum(amount),sum(refund) from ("+
				"select payment.member,payment.payment_method,payment.amount as amount,0 as refund "+
				"from wx_payment payment where payment.payee=?payee and payment.create_date>=?b and payment.create_date<?e and payment.status in (1,3,4,5) "+
				"union all "+
				"select refunds.member,refunds.payment_method,0 as amount,sum(refunds.amount) as refund "+
				"from wx_refunds refunds where payment.payee=?payee and refunds.create_date>=?b and refunds.create_date<?e and refunds.status = 2 "+
				") j group by member,payment_method order by member";

		Query query = entityManager.createNativeQuery(jpql).
				setFlushMode(FlushModeType.COMMIT).
				setParameter("payee", member).
				setParameter("b", b).
				setParameter("e", e);
		query.setFirstResult(pageable.getPageStart());
		query.setMaxResults(pageable.getPageStart()+pageable.getPageSize());
		List result = query.getResultList();
		List<PaymentSummary> data = new ArrayList<>();
		for (int i=0;i<result.size();i++) {
			Object[] row = (Object[]) result.get(i);
			PaymentSummary rw = new PaymentSummary();
			rw.setMemberId((Long) row[0]);
			rw.setTypeName((String) row[1]);
			rw.setAmount((BigDecimal) row[2]);
			rw.setRefund((BigDecimal) row[3]);
			data.add(rw);
		}
		return data;
	}


	public List<PaymentSummary> summary_method(Member member,Date beginDate, Date endDate, Pageable pageable) {
		Date b = DateUtils.truncate(beginDate,Calendar.DATE);
		Date e = DateUtils.truncate(endDate,Calendar.DATE);
		e =DateUtils.addDays(e,1);
		String jpql =
				"select payment_method,sum(amount),sum(refund) from ("+
						"select payment.payment_method,payment.amount as amount,0 as refund "+
						"from wx_payment payment where payment.payee=?payee and payment.create_date>=?b and payment.create_date<?e and payment.status in (1,3,4,5) "+
						"union all "+
						"select refunds.payment_method,0 as amount,sum(refunds.amount) as refund "+
						"from wx_refunds refunds where payment.payee=?payee and refunds.create_date>=?b and refunds.create_date<?e and refunds.status = 2 "+
						") j group by payment_method order by member";

		Query query = entityManager.createNativeQuery(jpql).
				setFlushMode(FlushModeType.COMMIT).
				setParameter("payee", member).
				setParameter("b", b).
				setParameter("e", e);
		query.setFirstResult(pageable.getPageStart());
		query.setMaxResults(pageable.getPageStart()+pageable.getPageSize());
		List result = query.getResultList();
		List<PaymentSummary> data = new ArrayList<>();
		for (int i=0;i<result.size();i++) {
			Object[] row = (Object[]) result.get(i);
			PaymentSummary rw = new PaymentSummary();
			rw.setTypeName((String) row[0]);
			rw.setAmount((BigDecimal) row[1]);
			rw.setRefund((BigDecimal) row[2]);
			data.add(rw);
		}
		return data;
	}

}