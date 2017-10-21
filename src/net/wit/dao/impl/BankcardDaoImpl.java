package net.wit.dao.impl;

import java.util.Calendar;

import java.util.Date;
import java.util.List;
import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.wit.entity.BindUser;
import net.wit.entity.Member;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.BankcardDao;
import net.wit.entity.Bankcard;


/**
 * @ClassName: BankcardDaoImpl
 * @author 降魔战队
 * @date 2017-10-20 17:56:0
 */
 

@Repository("bankcardDaoImpl")
public class BankcardDaoImpl extends BaseDaoImpl<Bankcard, Long> implements BankcardDao {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<Bankcard>
	 */
	public Page<Bankcard> findPage(Date beginDate,Date endDate, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Bankcard> criteriaQuery = criteriaBuilder.createQuery(Bankcard.class);
		Root<Bankcard> root = criteriaQuery.from(Bankcard.class);
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


	public Bankcard findDefault(Member member) {
		if (member == null) {
			return null;
		}
		try {
			String jpql = null;
			jpql = "select bankcard from Bankcard bankcard where bankcard.member = :member and bankcard.isDefault=:isDefault";

			List<Bankcard> cards = entityManager.createQuery(jpql, Bankcard.class).setFlushMode(FlushModeType.COMMIT).setParameter("member", member).setParameter("isDefault", true).getResultList();
			if (cards.size()>0) {
				return  cards.get(0);
			} else {
				return  null;
			}
		} catch (NoResultException e) {
			return null;
		}

	}

}