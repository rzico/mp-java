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
import net.wit.dao.CardDao;
import net.wit.entity.Card;


/**
 * @ClassName: CardDaoImpl
 * @author 降魔战队
 * @date 2017-11-4 18:12:24
 */
 

@Repository("cardDaoImpl")
public class CardDaoImpl extends BaseDaoImpl<Card, Long> implements CardDao {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<Card>
	 */
	public Page<Card> findPage(Date beginDate,Date endDate, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Card> criteriaQuery = criteriaBuilder.createQuery(Card.class);
		Root<Card> root = criteriaQuery.from(Card.class);
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
		if (pageable.getSearchValue()!=null && !"".equals(pageable.getSearchValue())) {
			restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.or(criteriaBuilder.like(root.<String> get("code"), "%"+pageable.getSearchValue()),criteriaBuilder.equal(root.<String> get("mobile"), pageable.getSearchValue())));
		}
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery,pageable);
	}

	public Card find(String code) {
		if (code == null) {
			return null;
		}
		String jpql = "select card from Card card where lower(card.code) = lower(:code)";
		try {
			return entityManager.createQuery(jpql, Card.class).setFlushMode(FlushModeType.COMMIT).setParameter("code", code).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

}