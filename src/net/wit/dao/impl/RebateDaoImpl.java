package net.wit.dao.impl;

import java.util.Calendar;

import java.util.Date;
import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.wit.entity.summary.RebateSummary;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.RebateDao;
import net.wit.entity.Rebate;


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

	public Page<RebateSummary> sumPage(Date beginDate, Date endDate, Pageable pageable) {
		return new Page<>();
//		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
//		CriteriaQuery<Rebate> criteriaQuery = criteriaBuilder.createQuery(Rebate.class);
//		Root<Rebate> root = criteriaQuery.from(Rebate.class);
//		criteriaQuery.select(root);
//		Predicate restrictions = criteriaBuilder.conjunction();
//		restrictions = criteriaBuilder.conjunction();
//		if (beginDate!=null) {
//			Date b = DateUtils.truncate(beginDate,Calendar.DATE);
//			restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("createDate"), b));
//		}
//		if (endDate!=null) {
//			Date e = DateUtils.truncate(endDate,Calendar.DATE);
//			e =DateUtils.addDays(e,1);
//			restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.lessThan(root.<Date> get("createDate"), e));
//		}
//		criteriaQuery.where(restrictions);
//		return super.findPage(criteriaQuery,pageable);
	}
}