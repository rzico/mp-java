package net.wit.dao;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.SmallRange;

import java.util.Date;

/**
 * Created by Eric on 2018/4/30.
 */
public interface SmallRangeDao extends BaseDao<SmallRange,Long> {
    Page<SmallRange> findPage(Date beginDate, Date endDate, Pageable pageable);
}
