package net.wit.job;

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

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Job - 签收
 * 
 */
@Component("completedJob")
@Lazy(false)
public class CompletedJob {
	public static Logger logger = LogManager.getLogger(BaseServiceImpl.class);

	@Resource(name = "orderServiceImpl")
	private OrderService orderService;

	/**
	 * 释放过期订单库存
	 */
	@Scheduled(cron = "${job.order_completed.cron}")
	public void completed() {
		List<Filter> filters = new ArrayList<Filter>();
		filters.add(new Filter("orderStatus", Filter.Operator.eq, Order.OrderStatus.confirmed));
		filters.add(new Filter("shippingStatus", Filter.Operator.eq, Order.ShippingStatus.shipped));
		filters.add(new Filter("shippingDate", Filter.Operator.le, DateUtils.addDays(new Date(), -6)));
		List<Order> data = orderService.findList(null, null, filters, null);
		for (Order order : data) {
			try {
				orderService.evictCompleted(order.getId());
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}

//		orderService.evictCompleted();
	}

}