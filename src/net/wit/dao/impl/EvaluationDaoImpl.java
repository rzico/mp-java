package net.wit.dao.impl;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.EvaluationDao;
import net.wit.dao.EvaluationDao;
import net.wit.entity.Evaluation;
import net.wit.entity.Evaluation;
import net.wit.entity.Tag;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.*;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * @ClassName: EvaluationDaoImpl
 * @author 降魔战队
 * @date 2018-2-12 21:4:34
 */
 

@Repository("evaluationDaoImpl")
public class EvaluationDaoImpl extends BaseDaoImpl<Evaluation, Long> implements EvaluationDao {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<Evaluation>
	 */
	public Page<Evaluation> findPage(Date beginDate, Date endDate,Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Evaluation> criteriaQuery = criteriaBuilder.createQuery(Evaluation.class);
		Root<Evaluation> root = criteriaQuery.from(Evaluation.class);
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
}