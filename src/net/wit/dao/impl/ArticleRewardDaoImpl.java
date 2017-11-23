package net.wit.dao.impl;

import java.math.BigDecimal;
import java.util.Calendar;

import java.util.Date;
import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.wit.entity.Member;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.ArticleRewardDao;
import net.wit.entity.ArticleReward;


/**
 * @ClassName: ArticleRewardDaoImpl
 * @author 降魔战队
 * @date 2017-9-14 19:42:4
 */
 

@Repository("articleRewardDaoImpl")
public class ArticleRewardDaoImpl extends BaseDaoImpl<ArticleReward, Long> implements ArticleRewardDao {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<ArticleReward>
	 */
	public Page<ArticleReward> findPage(Date beginDate,Date endDate, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<ArticleReward> criteriaQuery = criteriaBuilder.createQuery(ArticleReward.class);
		Root<ArticleReward> root = criteriaQuery.from(ArticleReward.class);
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

	public BigDecimal summary(Member member) {
		String jpql = "select sum(reward.amount) from ArticleReward reward where reward.author = :member";
		try {
			return entityManager.createQuery(jpql,BigDecimal.class)
					.setFlushMode(FlushModeType.COMMIT)
					.setParameter("member", member)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
}