package net.wit.dao.impl;

import java.util.Calendar;

import java.util.Date;
import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.wit.entity.Member;
import net.wit.entity.Payment;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.EnterpriseDao;
import net.wit.entity.Enterprise;


/**
 * @ClassName: EnterpriseDaoImpl
 * @author 降魔战队
 * @date 2017-9-14 19:42:4
 */
 

@Repository("enterpriseDaoImpl")
public class EnterpriseDaoImpl extends BaseDaoImpl<Enterprise, Long> implements EnterpriseDao {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<Enterprise>
	 */
	public Page<Enterprise> findPage(Date beginDate,Date endDate, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Enterprise> criteriaQuery = criteriaBuilder.createQuery(Enterprise.class);
		Root<Enterprise> root = criteriaQuery.from(Enterprise.class);
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

	public Enterprise find(Member member) {
		if (member == null) {
			return null;
		}
		String jpql = "select enterprise from Enterprise enterprise where lower(enterprise.member) = lower(:member)";
		try {
			return entityManager.createQuery(jpql, Enterprise.class).setFlushMode(FlushModeType.COMMIT).setParameter("member", member).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

}