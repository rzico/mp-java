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
import net.wit.dao.TopicCardDao;
import net.wit.entity.TopicCard;


/**
 * @ClassName: TopicCardDaoImpl
 * @author 降魔战队
 * @date 2017-11-9 12:58:51
 */
 

@Repository("topicCardDaoImpl")
public class TopicCardDaoImpl extends BaseDaoImpl<TopicCard, Long> implements TopicCardDao {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<TopicCard>
	 */
	public Page<TopicCard> findPage(Date beginDate,Date endDate, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<TopicCard> criteriaQuery = criteriaBuilder.createQuery(TopicCard.class);
		Root<TopicCard> root = criteriaQuery.from(TopicCard.class);
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