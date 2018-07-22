package net.wit.dao.impl;

import java.util.Calendar;

import java.util.Date;
import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.criteria.*;

import net.wit.entity.Article;
import net.wit.entity.Card;
import net.wit.entity.Member;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.ReceiverDao;
import net.wit.entity.Receiver;


/**
 * @ClassName: ReceiverDaoImpl
 * @author 降魔战队
 * @date 2017-9-14 19:42:5
 */
 

@Repository("receiverDaoImpl")
public class ReceiverDaoImpl extends BaseDaoImpl<Receiver, Long> implements ReceiverDao {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<Receiver>
	 */
	public Page<Receiver> findPage(Date beginDate,Date endDate, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Receiver> criteriaQuery = criteriaBuilder.createQuery(Receiver.class);
		Root<Receiver> root = criteriaQuery.from(Receiver.class);
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
		if (pageable.getSearchValue()!=null) {
			restrictions = criteriaBuilder.and(restrictions,
					criteriaBuilder.or(
							criteriaBuilder.like(root.<String>get("consignee"),"%"+pageable.getSearchValue()+"%"),
							criteriaBuilder.like(root.<String>get("phone"),"%"+pageable.getSearchValue()+"%"),
							criteriaBuilder.like(root.<String>get("areaName"),"%"+pageable.getSearchValue()+"%"),
							criteriaBuilder.like(root.<String>get("address"),"%"+pageable.getSearchValue()+"%")
					)
			);
		}
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery,pageable);
	}


	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<Receiver>
	 */
	public Page<Receiver> findPage(Date beginDate, Date endDate, Member owner, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Receiver> criteriaQuery = criteriaBuilder.createQuery(Receiver.class);
		Root<Receiver> root = criteriaQuery.from(Receiver.class);
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
		if (pageable.getSearchValue()!=null) {
			restrictions = criteriaBuilder.and(restrictions,
					criteriaBuilder.or(
							criteriaBuilder.like(root.<String>get("consignee"),"%"+pageable.getSearchValue()+"%"),
							criteriaBuilder.like(root.<String>get("phone"),"%"+pageable.getSearchValue()+"%"),
							criteriaBuilder.like(root.<String>get("areaName"),"%"+pageable.getSearchValue()+"%"),
							criteriaBuilder.like(root.<String>get("address"),"%"+pageable.getSearchValue()+"%")
					)
			);
		}
		Subquery<Card> subquery = criteriaQuery.subquery(Card.class);
		Root<Card> subqueryRoot = subquery.from(Card.class);
		subquery.select(subqueryRoot);
		subquery.where(criteriaBuilder.equal(subqueryRoot.get("member"), root.get("member")),criteriaBuilder.equal(subqueryRoot.get("owner"),owner));
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(subquery));
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery,pageable);
	}
}