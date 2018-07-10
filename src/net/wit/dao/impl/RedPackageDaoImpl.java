package net.wit.dao.impl;

import java.util.Calendar;

import java.util.Date;
import java.util.List;
import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.criteria.*;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.RedPackageDao;
import net.wit.entity.RedPackage;


/**
 * @ClassName: RedPackageDaoImpl
 * @author 降魔战队
 * @date 2018-7-6 10:38:11
 */
 

@Repository("redPackageDaoImpl")
public class RedPackageDaoImpl extends BaseDaoImpl<RedPackage, Long> implements RedPackageDao {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<RedPackage>
	 */
	public Page<RedPackage> findPage(Date beginDate,Date endDate, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<RedPackage> criteriaQuery = criteriaBuilder.createQuery(RedPackage.class);
		Root<RedPackage> root = criteriaQuery.from(RedPackage.class);
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


	public List<RedPackage> findByRedPackage(RedPackage redPackage){
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<RedPackage> criteriaQuery = criteriaBuilder.createQuery(RedPackage.class);
		Root<RedPackage> root = criteriaQuery.from(RedPackage.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if(redPackage != null){
			if(redPackage.getMember() != null){
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.join("member", JoinType.LEFT).<Long> get("id"),redPackage.getMember()));
			}
			if(redPackage.getArticle() != null){
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.join("article", JoinType.LEFT).<Long> get("id"),redPackage.getArticle()));
			}
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.<RedPackage.Status>get("status"), RedPackage.Status.get));
			criteriaQuery.where(restrictions);
			return super.findList(criteriaQuery, null, null, null, null);
		}
		return null;
	}
}