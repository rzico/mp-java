package net.wit.service;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.SmallRange;

import java.util.Date;

/**
 * Created by Eric on 2018/4/30.
 */
public interface SmallRangeService extends BaseService<SmallRange,Long>{
    Page<SmallRange> findPage(Date beginDate, Date endDate, Pageable pageable);
}
