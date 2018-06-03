package net.wit.job;

import net.wit.Filter;
import net.wit.entity.Payment;
import net.wit.service.PaymentService;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
		List<Filter> filters = new ArrayList<Filter>();
		filters.add(new Filter("status", Filter.Operator.eq, Payment.Status.waiting));
		filters.add(new Filter("paymentPluginId", Filter.Operator.isNotNull,null));
		filters.add(new Filter("createDate", Filter.Operator.le, DateUtils.addMinutes(new Date(),-30) ));
		List<Payment> data = paymentService.findList(null,null,filters,null);
		for (Payment payment:data) {
			paymentService.query(payment.getId());
		}
	}

}