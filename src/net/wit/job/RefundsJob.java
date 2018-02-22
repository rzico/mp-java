package net.wit.job;

import net.wit.service.PaymentService;
import net.wit.service.RefundsService;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Job - 退款单
 */
@Component("refundsJob")
@Lazy(false)
public class RefundsJob {

	@Resource(name = "refundsServiceImpl")
	private RefundsService refundsService;

	/**
	 * 释放过期订单库存
	 */
	@Scheduled(cron = "${job.refunds_query.cron}")
	public void query() {
		refundsService.query();
	}

}