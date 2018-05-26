package net.wit.service.impl;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.OperationRecordDao;
import net.wit.entity.OperationRecord;
import net.wit.service.OperationRecordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by Eric on 2018/5/4.
 */
@Service("operationRecordServiceImpl")
public class OperationRecordServiceImpl extends BaseServiceImpl<OperationRecord,Long> implements OperationRecordService {
    @Resource(name = "operationRecordDaoImpl")
    private OperationRecordDao operationRecordDao;

    @Resource(name = "operationRecordDaoImpl")
    public void setBaseDao(OperationRecordDao operationRecordDao) {
        super.setBaseDao(operationRecordDao);
    }

    @Override
    @Transactional
    //@CacheEvict(value = "authorization", allEntries = true)
    public OperationRecord update(OperationRecord operationRecord) {
        return super.update(operationRecord);
    }

    @Override
    @Transactional
    //@CacheEvict(value = "authorization", allEntries = true)
    public void save(OperationRecord operationRecord) {
        super.save(operationRecord);
    }

    @Override
    @Transactional
    //@CacheEvict(value = "authorization", allEntries = true)
    public void delete(Long id) {
        OperationRecord smallRange = operationRecordDao.find(id);
        super.delete(smallRange);
    }

    public Page<OperationRecord> findPage(Date beginDate, Date endDate, Pageable pageable) {
        return operationRecordDao.findPage(beginDate,endDate,pageable);
    }
}
