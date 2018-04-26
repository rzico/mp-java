package net.wit.dao.impl;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.NoticeDao;
import net.wit.entity.Notice;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Calendar;
import java.util.Date;


/**
 * @ClassName: NoticeDaoImpl
 * @author 降魔战队
 * @date 2018-4-5 18:22:28
 */
 

@Repository("noticeDaoImpl")
public class NoticeDaoImpl extends BaseDaoImpl<Notice, Long> implements NoticeDao {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<LiveGroup>
	 */
	public Page<Notice> findPage(Date beginDate,Date endDate, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Notice> criteriaQuery = criteriaBuilder.createQuery(Notice.class);
		Root<Notice> root = criteriaQuery.from(Notice.class);
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