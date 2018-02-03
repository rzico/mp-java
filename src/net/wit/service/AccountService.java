package net.wit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Account;

/**
 * @ClassName: AccountService
 * @author 降魔战队
 * @date 2018-2-3 21:28:26
 */

public interface AccountService extends BaseService<Account, Long> {
	Page<Account> findPage(Date beginDate, Date endDate, Pageable pageable);
}