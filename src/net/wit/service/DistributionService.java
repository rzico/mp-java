package net.wit.service;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Distribution;

import java.util.Date;

/**
 * @ClassName: DistributionService
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */

public interface DistributionService extends BaseService<Distribution, Long> {
	Page<Distribution> findPage(Date beginDate, Date endDate, Pageable pageable);
}