package net.wit.dao.impl;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.OperationRecordDao;
import net.wit.entity.OperationRecord;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Eric on 2018/5/4.
 */
@Repository("operationRecordDaoImpl")
public class OperationRecordDaoImpl extends BaseDaoImpl<OperationRecord,Long> implements OperationRecordDao {
    public Page<OperationRecord> findPage(Date beginDate, Date endDate, Pageable pageable){
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<OperationRecord> criteriaQuery = criteriaBuilder.createQuery(OperationRecord.class);
        Root<OperationRecord> root = criteriaQuery.from(OperationRecord.class);
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