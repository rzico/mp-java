package net.wit.dao.impl;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.AgentGaugeDao;
import net.wit.entity.AgentGauge;
import net.wit.entity.Gauge;
import net.wit.entity.Tag;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import javax.persistence.FlushModeType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.*;


/**
 * @ClassName: AgentGaugeDaoImpl
 * @author 降魔战队
 * @date 2017-9-14 19:42:5
 */
 

@Repository("agentGaugeDaoImpl")
public class AgentGaugeDaoImpl extends BaseDaoImpl<AgentGauge, Long> implements AgentGaugeDao {

	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<agentCategory>
	 */
	public Page<AgentGauge> findPage(Date beginDate, Date endDate, List<Tag> tags, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<AgentGauge> criteriaQuery = criteriaBuilder.createQuery(AgentGauge.class);
		Root<AgentGauge> root = criteriaQuery.from(AgentGauge.class);
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

		if (tags != null && !tags.isEmpty()) {
			Subquery<AgentGauge> subquery = criteriaQuery.subquery(AgentGauge.class);
			Root<AgentGauge> subqueryRoot = subquery.from(AgentGauge.class);
			subquery.select(subqueryRoot);
			subquery.where(criteriaBuilder.equal(subqueryRoot, root), subqueryRoot.join("tags").in(tags));
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(subquery));
		}

		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery,pageable);
	}

}