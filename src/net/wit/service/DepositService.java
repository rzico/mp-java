package net.wit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Deposit;
import net.wit.entity.Member;
import net.wit.entity.summary.DepositSummary;
import net.wit.entity.summary.NihtanDepositSummary;

/**
 * @ClassName: DepositService
 * @author 降魔战队
 * @date 2017-9-14 19:42:8
 */

public interface DepositService extends BaseService<Deposit, Long> {
	Page<Deposit> findPage(Date beginDate,Date endDate, Pageable pageable);
	BigDecimal summary(Deposit.Type type,Member member);
	BigDecimal summary(Deposit.Type type,Member member,Member seller);
	/**
	 */
	List<DepositSummary> sumPage(Member member, Date beginDate, Date endDate);

	List<NihtanDepositSummary> sumNihtanPage(Member member, Date beginDate, Date endDate);

}