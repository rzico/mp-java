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
import net.wit.dao.TransferDao;
import net.wit.entity.Transfer;


/**
 * @ClassName: TransferDaoImpl
 * @author 降魔战队
 * @date 2017-10-17 19:48:15
 */
 

@Repository("transferDaoImpl")
public class TransferDaoImpl extends BaseDaoImpl<Transfer, Long> implements TransferDao {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<Transfer>
	 */
	public Page<Transfer> findPage(Date beginDate,Date endDate, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Transfer> criteriaQuery = criteriaBuilder.createQuery(Transfer.class);
		Root<Transfer> root = criteriaQuery.from(Transfer.class);
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
	public Transfer findBySn(String sn) {
		if (sn == null) {
			return null;
		}
		String jpql = "select transfer from Transfer transfer where lower(transfer.sn) = lower(:sn)";
		try {
			return entityManager.createQuery(jpql, Transfer.class).setFlushMode(FlushModeType.COMMIT).setParameter("sn", sn).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	public Transfer findByOrderSn(String sn) {
		if (sn == null) {
			return null;
		}
		String jpql = "select transfer from Transfer transfer where lower(transfer.orderSn) = lower(:sn)";
		try {
			return entityManager.createQuery(jpql, Transfer.class).setFlushMode(FlushModeType.COMMIT).setParameter("sn", sn).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

}