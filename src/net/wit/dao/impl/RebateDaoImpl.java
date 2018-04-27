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

import net.wit.entity.*;
import net.wit.entity.summary.PayBillShopSummary;
import net.wit.entity.summary.RebateSummary;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.RebateDao;


/**
 * @ClassName: RebateDaoImpl
 * @author 降魔战队
 * @date 2018-4-24 20:57:48
 */
 

@Repository("rebateDaoImpl")
public class RebateDaoImpl extends BaseDaoImpl<Rebate, Long> implements RebateDao {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<Rebate>
	 */
	public Page<Rebate> findPage(Date beginDate,Date endDate, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Rebate> criteriaQuery = criteriaBuilder.createQuery(Rebate.class);
		Root<Rebate> root = criteriaQuery.from(Rebate.class);
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

	public Page<RebateSummary> sumPage(Date beginDate, Date endDate, Enterprise enterprise, Pageable pageable) {
//		Date b = DateUtils.truncate(beginDate,Calendar.DATE);
//		Date e = DateUtils.truncate(endDate,Calendar.DATE);
//		e = DateUtils.addDays(e,1);

		String jpql =
				"select rebate.member,sum(rebate.direct),sum(rebate.indirect) "+
						"from Rebate rebate where "+
//						"rebate.createDate>=:b and rebate.createDate<:e and "+
						"rebate.enterprise=:enterprise "+
						"group by rebate.member order by rebate.member.id ";

		Query query = entityManager.createQuery(jpql).
				setFlushMode(FlushModeType.COMMIT);
//				setParameter("b", b).
//				setParameter("e", e);

		query.setParameter("enterprise",enterprise);
		query.setFirstResult(pageable.getPageStart());
		query.setMaxResults(pageable.getPageSize());

		List result = query.getResultList();
		List<RebateSummary> data = new ArrayList<>();
		for (int i=0;i<result.size();i++) {
			Object[] row = (Object[]) result.get(i);
			RebateSummary rw = new RebateSummary();
			Member sh = (Member) row[0];
			rw.setMember(sh);
			rw.setDirect((BigDecimal) row[1]);
			rw.setIndirect((BigDecimal) row[2]);
			rw.setRebate(rw.getDirect().add(rw.getIndirect()));
			data.add(rw);
		}

		return new Page<RebateSummary>(data,0,pageable);
	}


	public RebateSummary sum(Date beginDate, Date endDate, Enterprise enterprise, Member member) {
		String jpql =
				"select sum(rebate.direct),sum(rebate.indirect) "+
						"from Rebate rebate where "+
						"rebate.enterprise=:enterprise";
		if (member!=null) {
			jpql = jpql + " and rebate.member=:member ";
		}

		Query query = entityManager.createQuery(jpql).
				setFlushMode(FlushModeType.COMMIT);

		query.setParameter("enterprise",enterprise);
		if (member!=null) {
			query.setParameter("member", member);
		}

		List result = query.getResultList();
		Object[] row = (Object[]) result.get(0);

		RebateSummary summary = new RebateSummary();

		if (row[0]!=null) {
			summary.setDirect((BigDecimal) row[0]);
			summary.setIndirect((BigDecimal) row[1]);
			summary.setRebate(summary.getDirect().add(summary.getIndirect()));
		} else {
			summary.setDirect(BigDecimal.ZERO);
			summary.setIndirect(BigDecimal.ZERO);
			summary.setRebate(BigDecimal.ZERO);
		}
		return summary;
	}
}