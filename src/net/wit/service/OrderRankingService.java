package net.wit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Order;
import net.wit.entity.OrderRanking;
import net.wit.entity.Payment;

/**
 * @ClassName: OrderRankingService
 * @author 降魔战队
 * @date 2018-4-5 18:22:33
 */

public interface OrderRankingService extends BaseService<OrderRanking, Long> {
	Page<OrderRanking> findPage(Date beginDate, Date endDate, Pageable pageable);

	public void add(Order order) throws Exception;
}