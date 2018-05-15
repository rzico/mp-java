package net.wit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Enterprise;
import net.wit.entity.Member;
import net.wit.entity.Order;
import net.wit.entity.Rebate;
import net.wit.entity.summary.RebateSummary;

/**
 * @ClassName: RebateService
 * @author 降魔战队
 * @date 2018-4-24 20:57:53
 */

public interface RebateService extends BaseService<Rebate, Long> {
	Page<Rebate> findPage(Date beginDate, Date endDate, Pageable pageable);
	Page<RebateSummary> sumPage(Date beginDate, Date endDate, Enterprise enterprise, Pageable pageable);
	RebateSummary sum(Date beginDate, Date endDate, Enterprise enterprise, Member member);


	public void rebate(BigDecimal amount, Member buyer,Enterprise personal,Enterprise agent,Enterprise operate,Order order) throws Exception;

	public void link(Order order) throws Exception;


	public void link(Member member,Member promoter) throws Exception;


}