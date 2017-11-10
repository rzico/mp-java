package net.wit.dao.impl;

import java.util.Calendar;

import java.util.Date;
import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.ArticleLaudDao;
import net.wit.entity.ArticleLaud;


/**
 * @ClassName: ArticleLaudDaoImpl
 * @author 降魔战队
 * @date 2017-9-14 19:42:4
 */
 

@Repository("articleLaudDaoImpl")
public class ArticleLaudDaoImpl extends BaseDaoImpl<ArticleLaud, Long> implements ArticleLaudDao {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<ArticleLaud>
	 */
	public Page<ArticleLaud> findPage(Date beginDate,Date endDate, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<ArticleLaud> criteriaQuery = criteriaBuilder.createQuery(ArticleLaud.class);
		Root<ArticleLaud> root = criteriaQuery.from(ArticleLaud.class);
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
		restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.equal(root.<Boolean> get("isShow"), true));
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery,pageable);
	}
}