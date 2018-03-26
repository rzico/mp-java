package net.wit.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;

import java.util.Date;
import java.util.List;
import javax.persistence.FlushModeType;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.wit.entity.Deposit;
import net.wit.entity.Member;
import net.wit.entity.summary.DepositSummary;
import net.wit.entity.summary.NihtanDepositSummary;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Repository;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.GoldDao;
import net.wit.entity.Gold;


/**
 * @ClassName: GoldDaoImpl
 * @author 降魔战队
 * @date 2018-3-25 14:59:0
 */
 

@Repository("goldDaoImpl")
public class GoldDaoImpl extends BaseDaoImpl<Gold, Long> implements GoldDao {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<GmGold>
	 */
	public Page<Gold> findPage(Date beginDate,Date endDate, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Gold> criteriaQuery = criteriaBuilder.createQuery(Gold.class);
		Root<Gold> root = criteriaQuery.from(Gold.class);
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


	public List<NihtanDepositSummary> sumPage(Member member, Date beginDate, Date endDate) {
		Date b = DateUtils.truncate(beginDate,Calendar.DATE);
		Date e = DateUtils.truncate(endDate,Calendar.DATE);
		e =DateUtils.addDays(e,1);
		String jpql =
				"select deposit.memo,sum(deposit.credit)-sum(deposit.debit) "+
						"from Gold deposit where deposit.createDate>=:b and deposit.createDate<:e and deposit.member=:member "+
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