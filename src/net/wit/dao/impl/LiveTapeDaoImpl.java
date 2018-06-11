package net.wit.dao.impl;

import java.util.Calendar;

import java.util.Date;
import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.wit.entity.Live;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.LiveTapeDao;
import net.wit.entity.LiveTape;


/**
 * @ClassName: LiveTapeDaoImpl
 * @author 降魔战队
 * @date 2018-4-5 18:22:28
 */
 

@Repository("liveTapeDaoImpl")
public class LiveTapeDaoImpl extends BaseDaoImpl<LiveTape, Long> implements LiveTapeDao {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<LiveTape>
	 */
	public Page<LiveTape> findPage(Date beginDate, Date endDate, Live live, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<LiveTape> criteriaQuery = criteriaBuilder.createQuery(LiveTape.class);
		Root<LiveTape> root = criteriaQuery.from(LiveTape.class);
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
		if (live!=null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("live"), live));
		}
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery,pageable);
	}
}