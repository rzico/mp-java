package net.wit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Road;

/**
 * @ClassName: RoadService
 * @author 降魔战队
 * @date 2018-4-28 14:18:48
 */

public interface RoadService extends BaseService<Road, Long> {
	Page<Road> findPage(Date beginDate, Date endDate, Pageable pageable);
}