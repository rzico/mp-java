package net.wit.dao.impl;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.SmallRangeDao;
import net.wit.entity.SmallRange;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Eric on 2018/4/30.
 */
@Repository("smallRangeDaoImpl")
public class SmallRangeDaoImpl extends BaseDaoImpl<SmallRange,Long> implements SmallRangeDao {

    public Page<SmallRange> findPage(Date beginDate, Date endDate, Pageable pageable){
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<SmallRange> criteriaQuery = criteriaBuilder.createQuery(SmallRange.class);
        Root<SmallRange> root = criteriaQuery.from(SmallRange.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        restrictions = criteriaBuilder.conjunction();
        if (beginDate!=null) {
            Date b = DateUtils.truncate(beginDate, Calendar.DATE);
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
