package net.wit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Recharge;

/**
 * @ClassName: RechargeService
 * @author 降魔战队
 * @date 2018-2-1 14:1:27
 */

public interface RechargeService extends BaseService<Recharge, Long> {
	Page<Recharge> findPage(Date beginDate, Date endDate, Pageable pageable);
}