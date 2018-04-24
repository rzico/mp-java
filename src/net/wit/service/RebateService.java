package net.wit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Rebate;
import net.wit.entity.summary.RebateSummary;

/**
 * @ClassName: RebateService
 * @author 降魔战队
 * @date 2018-4-24 20:57:53
 */

public interface RebateService extends BaseService<Rebate, Long> {
	Page<Rebate> findPage(Date beginDate, Date endDate, Pageable pageable);
	Page<RebateSummary> sumPage(Date beginDate, Date endDate, Pageable pageable);
}