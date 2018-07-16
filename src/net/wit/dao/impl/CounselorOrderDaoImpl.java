package net.wit.dao.impl;

import java.util.Calendar;

import java.util.Date;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.wit.entity.CounselorOrder;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Repository;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.CounselorOrderDao;


/**
 * @ClassName: CounselorOrderDaoImpl
 * @author 降魔战队
 * @date 2018-7-13 14:38:31
 */
 

@Repository("counselorOrderDaoImpl")
public class CounselorOrderDaoImpl extends BaseDaoImpl<CounselorOrder, Long> implements CounselorOrderDao {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<Subscribe>
	 */
	public Page<CounselorOrder> findPage(Date beginDate, Date endDate, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<CounselorOrder> criteriaQuery = criteriaBuilder.createQuery(CounselorOrder.class);
		Root<CounselorOrder> root = criteriaQuery.from(CounselorOrder.class);
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
}