package net.wit.job;

import net.wit.service.OrderService;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Job - 签收
 * 
 */
@Component("completedJob")
@Lazy(false)
public class CompletedJob {

	@Resource(name = "orderServiceImpl")
	private OrderService orderService;

	/**
	 * 释放过期订单库存
	 */
	@Scheduled(cron = "${job.order_completed.cron}")
	public void completed() {
		orderService.evictCompleted();
	}

}