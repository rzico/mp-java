package net.wit.dao.impl;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.EvaluationDao;
import net.wit.dao.EvaluationDao;
import net.wit.entity.*;
import net.wit.entity.Evaluation;
import net.wit.entity.summary.DepositSummary;
import net.wit.entity.summary.EvaluationSummary;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.FlushModeType;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.util.ArrayList;
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

	/**
	 */
	public List<EvaluationSummary> sumPromoter(Gauge gauge,Date beginDate, Date endDate) {
//		Date b = null;
//		if (beginDate!=null)  {
//			b = DateUtils.truncate(beginDate,Calendar.DATE);
//		}
//		Date e = null;
//		if (endDate!=null) {
//			e = DateUtils.truncate(endDate,Calendar.DATE);
//			e =DateUtils.addDays(e,1);
//		}
		String jpql =
				"select evaluation.promoter,count(evaluation.id) as count "+
						"from Evaluation evaluation where evaluation.gauge=:gauge and evaluation.promoter is not null "+
						"group by evaluation.promoter order by count(evaluation.id) desc ";

		Query query = entityManager.createQuery(jpql).
				setFlushMode(FlushModeType.COMMIT).
				setParameter("gauge", gauge);

		List result = query.getResultList();
		List<EvaluationSummary> data = new ArrayList<>();
		for (int i=0;i<result.size();i++) {
			Object[] row = (Object[]) result.get(i);
			EvaluationSummary rw = new EvaluationSummary();
			rw.setMember((Member) row[0]);
			rw.setCount((Long) row[1]);
			data.add(rw);
		}
		return data;
	}

}