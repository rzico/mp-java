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
import net.wit.dao.ShopDao;
import net.wit.entity.Shop;


/**
 * @ClassName: ShopDaoImpl
 * @author 降魔战队
 * @date 2017-11-4 18:12:25
 */
 

@Repository("shopDaoImpl")
public class ShopDaoImpl extends BaseDaoImpl<Shop, Long> implements ShopDao {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<Shop>
	 */
	public Page<Shop> findPage(Date beginDate,Date endDate, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Shop> criteriaQuery = criteriaBuilder.createQuery(Shop.class);
		Root<Shop> root = criteriaQuery.from(Shop.class);
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


	/**
	 * @Title：findPage
	 * @Description：联盟商家配送点
	 * @param pageable
	 * @return Page<Shop>
	 */
	public Page<Shop> findPage(Member owner, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Shop> criteriaQuery = criteriaBuilder.createQuery(Shop.class);
		Root<Shop> root = criteriaQuery.from(Shop.class);
		criteriaQuery.select(root);

		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.conjunction();

		restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.equal(root.<Boolean> get("deleted"), false));
		restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.equal(root.<Boolean> get("deleted"), false));
		criteriaQuery.where(restrictions);

		return super.findPage(criteriaQuery,pageable);
	}

	public Shop find(String code) {
		if (code == null) {
			return null;
		}
		String jpql = "select shop from Shop shop where lower(shop.code) = lower(:code)";
		try {
			return entityManager.createQuery(jpql, Shop.class).setFlushMode(FlushModeType.COMMIT).setParameter("code", code).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

}