package net.wit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Finance;

/**
 * @ClassName: FinanceService
 * @author 降魔战队
 * @date 2018-2-3 21:28:27
 */

public interface FinanceService extends BaseService<Finance, Long> {
	Page<Finance> findPage(Date beginDate, Date endDate, Pageable pageable);
}