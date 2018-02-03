package net.wit.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Account;


/**
 * @ClassName: AccountDao
 * @author 降魔战队
 * @date 2018-2-3 21:28:22
 */
 

public interface AccountDao extends BaseDao<Account, Long> {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<Account>
	 */
	Page<Account> findPage(Date beginDate, Date endDate, Pageable pageable);
}