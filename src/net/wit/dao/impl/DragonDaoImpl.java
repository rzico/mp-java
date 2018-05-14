package net.wit.dao.impl;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.DragonDao;
import net.wit.entity.Article;
import net.wit.entity.Dragon;
import net.wit.entity.Member;
import net.wit.entity.Product;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Calendar;
import java.util.Date;


/**
 * @ClassName: DragonDaoImpl
 * @author 降魔战队
 * @date 2017-9-14 19:42:6
 */
 

@Repository("dragonDaoImpl")
public class DragonDaoImpl extends BaseDaoImpl<Dragon, Long> implements DragonDao {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<Vip>
	 */
	public Page<Dragon> findPage(Date beginDate,Date endDate, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Dragon> criteriaQuery = criteriaBuilder.createQuery(Dragon.class);
		Root<Dragon> root = criteriaQuery.from(Dragon.class);
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
		restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.equal(root.<Boolean> get("deleted"), false));
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery,pageable);
	}

	public Dragon find(Article article, Member member) {
		String jpql = "select dragon from Dragon dragon where dragon.member = :member and dragon.article = :article";
		try {
			return entityManager.createQuery(jpql, Dragon.class).setFlushMode(FlushModeType.COMMIT)
					.setParameter("member",member)
					.setParameter("article", article).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

}