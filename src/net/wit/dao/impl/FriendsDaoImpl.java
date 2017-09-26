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
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.FriendsDao;
import net.wit.entity.Friends;


/**
 * @ClassName: FriendsDaoImpl
 * @author 降魔战队
 * @date 2017-9-14 19:42:5
 */
 

@Repository("friendsDaoImpl")
public class FriendsDaoImpl extends BaseDaoImpl<Friends, Long> implements FriendsDao {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<Friends>
	 */
	public Page<Friends> findPage(Date beginDate,Date endDate, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Friends> criteriaQuery = criteriaBuilder.createQuery(Friends.class);
		Root<Friends> root = criteriaQuery.from(Friends.class);
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
	public Friends find(Member member,Member friend) {
		if (member == null) {
			return null;
		}
		if (friend == null) {
			return null;
		}
		try {
			String jpql = "select friends from Friends friends where friends.member=:member and friends.friend = :friend";
			return entityManager.createQuery(jpql, Friends.class).setFlushMode(FlushModeType.COMMIT).setParameter("member", member).setParameter("friend", friend).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
}