package net.wit.job;

import javax.annotation.Resource;


import net.wit.Filter;
import net.wit.entity.Order;
import net.wit.service.OrderService;
import net.wit.service.impl.BaseServiceImpl;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Job - 订单
 * 
 */
@Component("orderJob")
@Lazy(false)
public class OrderJob {
	public static Logger logger = LogManager.getLogger(BaseServiceImpl.class);

	@Resource(name = "orderServiceImpl")
	private OrderService orderService;

	/**
	 * 释放过期订单库存
	 */
	@Scheduled(cron = "${job.order_release_stock.cron}")
	public void releaseStock() {
		List<Filter> filters = new ArrayList<Filter>();
		filters.add(new Filter("isAllocatedStock", Filter.Operator.eq, true));
		filters.add(new Filter("expire", Filter.Operator.isNotNull, null));
		filters.add(new Filter("expire", Filter.Operator.le, DateUtils.addDays(new Date(), -6)));
		List<Order> data = orderService.findList(null, null, filters, null);
		for (Order order : data) {
			try {
				orderService.releaseStock(order.getId());
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
	}

}