package net.wit.dao.impl;

import java.math.BigDecimal;
import java.util.Calendar;

import java.util.Date;
import java.util.List;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.wit.entity.Barrel;
import net.wit.entity.Card;
import net.wit.entity.Member;
import net.wit.entity.summary.PaymentSummary;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.BarrelStockDao;
import net.wit.entity.BarrelStock;


/**
 * @ClassName: BarrelStockDaoImpl
 * @author 降魔战队
 * @date 2018-5-28 15:8:41
 */
 

@Repository("barrelStockDaoImpl")
public class BarrelStockDaoImpl extends BaseDaoImpl<BarrelStock, Long> implements BarrelStockDao {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<BarrelStock>
	 */
	public Page<BarrelStock> findPage(Date beginDate,Date endDate, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<BarrelStock> criteriaQuery = criteriaBuilder.createQuery(BarrelStock.class);
		Root<BarrelStock> root = criteriaQuery.from(BarrelStock.class);
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

	public synchronized BarrelStock find(Card card, Barrel barrel) {
		String jpql = "select barrelStock from BarrelStock barrelStock where barrelStock.card = :card and barrelStock.barrel=:barrel";
		try {
			BarrelStock barrelStock = entityManager.createQuery(jpql, BarrelStock.class).setFlushMode(FlushModeType.COMMIT)
					.setParameter("card", card)
					.setParameter("barrel", barrel)
					.getSingleResult();
			return barrelStock;
		} catch (NoResultException e) {
			return null;
		}

	}

	public Long calcStock(Card card) {
		String jpql = "select sum(barrelStock.stock) from wx_barrel_stock barrelStock where barrelStock.card = ?";
		try {
			Query query = entityManager.createNativeQuery(jpql).
					setFlushMode(FlushModeType.COMMIT).
					setParameter(1, card);
			List result = query.getResultList();
			for (int i=0;i<result.size();i++) {
				Object[] row = (Object[]) result.get(i);
				if (row!=null) {
				    Integer dd = (Integer) row[0];
					return dd.longValue();
				} else {
					return 0L;
				}
			}
			return 0L;
		} catch (NoResultException e) {
			return 0L;
		}

	}

}