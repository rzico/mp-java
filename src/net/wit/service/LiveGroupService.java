package net.wit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.LiveGroup;

/**
 * @ClassName: LiveGroupService
 * @author 降魔战队
 * @date 2018-4-5 18:22:33
 */

public interface LiveGroupService extends BaseService<LiveGroup, Long> {
	Page<LiveGroup> findPage(Date beginDate, Date endDate, Pageable pageable);
}