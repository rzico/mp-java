package net.wit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.GoldExchange;

/**
 * @ClassName: GmGoldExchangeService
 * @author 降魔战队
 * @date 2018-3-25 14:59:5
 */

public interface GoldExchangeService extends BaseService<GoldExchange, Long> {
	Page<GoldExchange> findPage(Date beginDate, Date endDate, Pageable pageable);
}