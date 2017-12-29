package net.wit.job;

import net.wit.service.PaymentService;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Job - 付款单
 */
@Component("paymentJob")
@Lazy(false)
public class PaymentJob {

	@Resource(name = "paymentServiceImpl")
	private PaymentService paymentService;

	/**
	 * 释放过期订单库存
	 */
	@Scheduled(cron = "${job.payment_query.cron}")
	public void query() {

		paymentService.query();
	}

}