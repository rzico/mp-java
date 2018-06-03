package net.wit.job;

import net.wit.Filter;
import net.wit.entity.Refunds;
import net.wit.service.PaymentService;
import net.wit.service.RefundsService;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
		List<Filter> filters = new ArrayList<Filter>();
		filters.add(new Filter("status", Filter.Operator.eq, Refunds.Status.confirmed));
		filters.add(new Filter("createDate", Filter.Operator.le, DateUtils.addMinutes(new Date(),-30) ));
		List<Refunds> data = refundsService.findList(null,null,filters,null);
		for (Refunds refunds:data) {
			refundsService.query(refunds.getId());
		}
	}

}