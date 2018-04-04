package net.wit.dao.impl;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.HostDao;
import net.wit.dao.VipDao;
import net.wit.entity.Host;
import net.wit.entity.Vip;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Calendar;
import java.util.Date;


/**
 * @ClassName: HostDaoImpl
 * @author 降魔战队
 * @date 2017-9-14 19:42:6
 */
 

@Repository("hostDaoImpl")
public class HostDaoImpl extends BaseDaoImpl<Host, Long> implements HostDao {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<Vip>
	 */
	public Page<Host> findPage(Date beginDate,Date endDate, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Host> criteriaQuery = criteriaBuilder.createQuery(Host.class);
		Root<Host> root = criteriaQuery.from(Host.class);
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
}