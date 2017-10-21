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

import net.wit.dao.BankcardDao;
import net.wit.entity.*;
import net.wit.service.BankcardService;

/**
 * @ClassName: BankcardDaoImpl
 * @author 降魔战队
 * @date 2017-10-20 17:56:4
 */
 
 
@Service("bankcardServiceImpl")
public class BankcardServiceImpl extends BaseServiceImpl<Bankcard, Long> implements BankcardService {
	@Resource(name = "bankcardDaoImpl")
	private BankcardDao bankcardDao;

	@Resource(name = "bankcardDaoImpl")
	public void setBaseDao(BankcardDao bankcardDao) {
		super.setBaseDao(bankcardDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(Bankcard bankcard) {
		super.save(bankcard);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Bankcard update(Bankcard bankcard) {
		return super.update(bankcard);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Bankcard update(Bankcard bankcard, String... ignoreProperties) {
		return super.update(bankcard, ignoreProperties);
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
	public void delete(Bankcard bankcard) {
		super.delete(bankcard);
	}

	public Page<Bankcard> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return bankcardDao.findPage(beginDate,endDate,pageable);
	}

	public Bankcard findDefault(Member member) {
		return bankcardDao.findDefault(member);
	}
}