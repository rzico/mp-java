package net.wit.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.wit.Filter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.Principal;
import net.wit.Filter.Operator;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.wit.dao.AccountDao;
import net.wit.entity.*;
import net.wit.service.AccountService;

/**
 * @ClassName: AccountDaoImpl
 * @author 降魔战队
 * @date 2018-2-3 21:28:26
 */
 
 
@Service("accountServiceImpl")
public class AccountServiceImpl extends BaseServiceImpl<Account, Long> implements AccountService {
	@Resource(name = "accountDaoImpl")
	private AccountDao accountDao;

	@Resource(name = "accountDaoImpl")
	public void setBaseDao(AccountDao accountDao) {
		super.setBaseDao(accountDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(Account account) {
		super.save(account);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Account update(Account account) {
		return super.update(account);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Account update(Account account, String... ignoreProperties) {
		return super.update(account, ignoreProperties);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void delete(Long... ids) {
		super.delete(ids);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void delete(Account account) {
		super.delete(account);
	}

	public Page<Account> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return accountDao.findPage(beginDate,endDate,pageable);
	}
}