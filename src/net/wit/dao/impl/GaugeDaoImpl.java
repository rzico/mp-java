package net.wit.dao.impl;

import java.util.Calendar;

import java.util.Date;
import java.util.List;
import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.criteria.*;

import net.wit.entity.Article;
import net.wit.entity.Tag;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.GaugeDao;
import net.wit.entity.Gauge;


/**
 * @ClassName: GaugeDaoImpl
 * @author 降魔战队
 * @date 2018-2-12 21:4:34
 */
 

@Repository("gaugeDaoImpl")
public class GaugeDaoImpl extends BaseDaoImpl<Gauge, Long> implements GaugeDao {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<Gauge>
	 */
	public Page<Gauge> findPage(Date beginDate, Date endDate, List<Tag> tags, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Gauge> criteriaQuery = criteriaBuilder.createQuery(Gauge.class);
		Root<Gauge> root = criteriaQuery.from(Gauge.class);
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
			Subquery<Gauge> subquery = criteriaQuery.subquery(Gauge.class);
			Root<Gauge> subqueryRoot = subquery.from(Gauge.class);
			subquery.select(subqueryRoot);
			subquery.where(criteriaBuilder.equal(subqueryRoot, root), subqueryRoot.join("tags").in(tags));
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(subquery));
		}

		restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.equal(root.<Boolean> get("deleted"), false));
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery,pageable);
	}
}