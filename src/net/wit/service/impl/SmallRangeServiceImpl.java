package net.wit.service.impl;


import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.SmallRangeDao;
import net.wit.entity.Shop;
import net.wit.entity.SmallRange;
import net.wit.service.SmallRangeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by Eric on 2018/4/30.
 */
@Service("smallRangeServiceImpl")
public class SmallRangeServiceImpl extends BaseServiceImpl<SmallRange,Long> implements SmallRangeService{

    @Resource(name = "smallRangeDaoImpl")
    private SmallRangeDao smallRangeDao;

    @Resource(name = "smallRangeDaoImpl")
    public void setBaseDao(SmallRangeDao smallRangeDao) {
        super.setBaseDao(smallRangeDao);
    }

    @Override
    @Transactional
    //@CacheEvict(value = "authorization", allEntries = true)
    public SmallRange update(SmallRange smallRange) {
        return super.update(smallRange);
    }

    @Override
    @Transactional
    //@CacheEvict(value = "authorization", allEntries = true)
    public void save(SmallRange smallRange) {
        super.save(smallRange);
    }

    @Override
    @Transactional
    //@CacheEvict(value = "authorization", allEntries = true)
    public void delete(Long id) {
        SmallRange smallRange = smallRangeDao.find(id);
        super.delete(smallRange);
    }

    public Page<SmallRange> findPage(Date beginDate, Date endDate, Pageable pageable) {
        return smallRangeDao.findPage(beginDate,endDate,pageable);
    }
}
