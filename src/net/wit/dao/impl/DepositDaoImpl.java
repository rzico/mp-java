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
import net.wit.entity.PayBill;
import net.wit.entity.Shop;
import net.wit.entity.summary.DepositSummary;
import net.wit.entity.summary.NihtanDepositSummary;
import net.wit.entity.summary.PayBillShopSummary;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.DepositDao;
import net.wit.entity.Deposit;


/**
 * @ClassName: DepositDaoImpl
 * @author 降魔战队
 * @date 2017-9-14 19:42:4
 */
 

@Repository("depositDaoImpl")
public class DepositDaoImpl extends BaseDaoImpl<Deposit, Long> implements DepositDao {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<Deposit>
	 */
	public Page<Deposit> findPage(Date beginDate,Date endDate, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Deposit> criteriaQuery = criteriaBuilder.createQuery(Deposit.class);
		Root<Deposit> root = criteriaQuery.from(Deposit.class);
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
		restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.equal(root.<Boolean> get("deleted"), false));
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery,pageable);
	}
	public BigDecimal summary(Deposit.Type type,Member member) {
		String jpql = "select sum(deposit.credit)-sum(deposit.debit) from Deposit deposit where  deposit.type =:type and deposit.member = :member";
		try {
			return entityManager.createQuery(jpql,BigDecimal.class)
					.setFlushMode(FlushModeType.COMMIT)
					.setParameter("member", member)
					.setParameter("type", type)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	public BigDecimal summary(Deposit.Type type,Member member,Member seller) {
		String jpql = "select sum(deposit.credit)-sum(deposit.debit) from Deposit deposit where  deposit.type =:type and deposit.member = :member and deposit.seller=:seller";
		try {
			return entityManager.createQuery(jpql,BigDecimal.class)
					.setFlushMode(FlushModeType.COMMIT)
					.setParameter("member", member)
					.setParameter("type", type)
					.setParameter("seller", seller)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public List<DepositSummary> sumPage(Member member, Date beginDate, Date endDate) {
		Date b = DateUtils.truncate(beginDate,Calendar.DATE);
		Date e = DateUtils.truncate(endDate,Calendar.DATE);
		e =DateUtils.addDays(e,1);
		String jpql =
				"select deposit.type,sum(deposit.credit)-sum(deposit.debit) "+
						"from Deposit deposit where deposit.createDate>=:b and deposit.createDate<:e and deposit.member=:member "+
						"group by deposit.type order by deposit.type ";

		Query query = entityManager.createQuery(jpql).
				setFlushMode(FlushModeType.COMMIT).
				setParameter("b", b).
				setParameter("e", e).
				setParameter("member",member);

		List result = query.getResultList();
		List<DepositSummary> data = new ArrayList<>();
		for (int i=0;i<result.size();i++) {
			Object[] row = (Object[]) result.get(i);
			DepositSummary rw = new DepositSummary();
			rw.setType((Deposit.Type) row[0]);
			rw.setAmount((BigDecimal) row[1]);
			data.add(rw);
		}
		return data;

	}

	public List<NihtanDepositSummary> sumNihtanPage(Member member, Date beginDate, Date endDate) {
		Date b = DateUtils.truncate(beginDate,Calendar.DATE);
		Date e = DateUtils.truncate(endDate,Calendar.DATE);
		e =DateUtils.addDays(e,1);
		String jpql =
				"select deposit.memo,sum(deposit.credit)-sum(deposit.debit) "+
						"from Deposit deposit where deposit.createDate>=:b and deposit.createDate<:e and deposit.member=:member "+
						"group by deposit.memo order by deposit.memo ";

		Query query = entityManager.createQuery(jpql).
				setFlushMode(FlushModeType.COMMIT).
				setParameter("b", b).
				setParameter("e", e).
				setParameter("member",member);

		List result = query.getResultList();
		List<NihtanDepositSummary> data = new ArrayList<>();
		for (int i=0;i<result.size();i++) {
			Object[] row = (Object[]) result.get(i);
			NihtanDepositSummary rw = new NihtanDepositSummary();
			rw.setType((String) row[0]);
			rw.setAmount((BigDecimal) row[1]);
			data.add(rw);
		}
		return data;

	}


}