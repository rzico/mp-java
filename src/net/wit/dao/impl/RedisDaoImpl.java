package net.wit.dao.impl;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.RedisDao;
import net.wit.dao.TagDao;
import net.wit.entity.Redis;
import net.wit.entity.Tag;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Calendar;
import java.util.Date;


/**
 * @ClassName: RedisDaoImpl
 * @author 降魔战队
 * @date 2017-9-14 19:42:6
 */
 

@Repository("redisDaoImpl")
public class RedisDaoImpl extends BaseDaoImpl<Redis, Long> implements RedisDao {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<Tag>
	 */
	public Page<Redis> findPage(Date beginDate,Date endDate, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Redis> criteriaQuery = criteriaBuilder.createQuery(Redis.class);
		Root<Redis> root = criteriaQuery.from(Redis.class);
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
	public Redis findKey(String key) {
		if (key == null) {
			return null;
		}
		try {
			String jpql = "select redis from Redis redis where lower(redis.key) = lower(:key)";
			return entityManager.createQuery(jpql, Redis.class).setLockMode(LockModeType.PESSIMISTIC_WRITE).setFlushMode(FlushModeType.COMMIT).setParameter("key", key).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
}