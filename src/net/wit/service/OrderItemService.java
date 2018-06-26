package net.wit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Member;
import net.wit.entity.OrderItem;
import net.wit.entity.summary.OrderItemSummary;
import net.wit.entity.summary.OrderSummary;

/**
 * @ClassName: OrderItemService
 * @author 降魔战队
 * @date 2017-9-14 19:42:8
 */

public interface OrderItemService extends BaseService<OrderItem, Long> {
	Page<OrderItem> findPage(Date beginDate,Date endDate, Pageable pageable);

	List<OrderItemSummary> summary(Member member, Date beginDate, Date endDate, Pageable pageable);
}