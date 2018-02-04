package net.wit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.CustomService;

/**
 * @ClassName: CustomServiceService
 * @author 降魔战队
 * @date 2018-2-3 21:3:50
 */

public interface CustomServiceService extends BaseService<CustomService, Long> {
	Page<CustomService> findPage(Date beginDate, Date endDate, Pageable pageable);
}