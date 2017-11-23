package net.wit.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Deposit;
import net.wit.entity.Member;


/**
 * @ClassName: DepositDao
 * @author 降魔战队
 * @date 2017-9-14 19:42:4
 */
 

public interface DepositDao extends BaseDao<Deposit, Long> {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<Deposit>
	 */
	Page<Deposit> findPage(Date beginDate,Date endDate, Pageable pageable);
	BigDecimal summary(Deposit.Type type,Member member);
}