package net.wit.dao;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.OperationRecord;
import net.wit.service.BaseService;

import java.util.Date;

/**
 * Created by Eric on 2018/5/4.
 */
public interface OperationRecordDao extends BaseDao<OperationRecord,Long> {
    Page<OperationRecord> findPage(Date beginDate, Date endDate, Pageable pageable);
}
