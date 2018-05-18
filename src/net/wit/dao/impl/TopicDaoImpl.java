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
import net.wit.dao.TopicDao;
import net.wit.entity.Topic;


/**
 * @ClassName: TopicDaoImpl
 * @author 降魔战队
 * @date 2017-9-14 19:42:6
 */
 

@Repository("topicDaoImpl")
public class TopicDaoImpl extends BaseDaoImpl<Topic, Long> implements TopicDao {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<Topic>
	 */
	public Page<Topic> findPage(Date beginDate,Date endDate, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Topic> criteriaQuery = criteriaBuilder.createQuery(Topic.class);
		Root<Topic> root = criteriaQuery.from(Topic.class);
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

	public Topic find(Member member) {
		if (member == null) {
			return null;
		}
		String jpql = "select topic from Topic topic where topic.member = :member";
		try {
			return entityManager.createQuery(jpql, Topic.class).setFlushMode(FlushModeType.COMMIT).setParameter("member", member).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	@Override
	public Topic findByUserName(String userName) {
		if(userName == null || userName.equalsIgnoreCase("")){
			return null;
		}
		try{
			String jpql = "select topic from Topic topic where topic.config.userName = :userName";
			return entityManager.createQuery(jpql, Topic.class).setFlushMode(FlushModeType.COMMIT).setParameter("userName", userName).getSingleResult();
		}catch (NoResultException e){
			return null;
		}
	}

	@Override
	public Topic findByAppid(String appid) {
		if(appid == null || appid.equalsIgnoreCase("")){
			return null;
		}
		try{
			String jpql = "select topic from Topic topic where topic.config.appetAppId = :appetAppId";
			return entityManager.createQuery(jpql, Topic.class).setFlushMode(FlushModeType.COMMIT).setParameter("appetAppId", appid).getSingleResult();
		}catch (NoResultException e){
			return null;
		}
	}
}