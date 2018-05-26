package net.wit.service;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.OperationRecord;

import java.util.Date;

/**
 * Created by Eric on 2018/5/4.
 */
public interface OperationRecordService extends BaseService<OperationRecord,Long>{
    Page<OperationRecord> findPage(Date beginDate, Date endDate, Pageable pageable);
}
