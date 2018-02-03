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

import net.wit.dao.FinanceDao;
import net.wit.entity.*;
import net.wit.service.FinanceService;

/**
 * @ClassName: FinanceDaoImpl
 * @author 降魔战队
 * @date 2018-2-3 21:28:27
 */
 
 
@Service("financeServiceImpl")
public class FinanceServiceImpl extends BaseServiceImpl<Finance, Long> implements FinanceService {
	@Resource(name = "financeDaoImpl")
	private FinanceDao financeDao;

	@Resource(name = "financeDaoImpl")
	public void setBaseDao(FinanceDao financeDao) {
		super.setBaseDao(financeDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(Finance finance) {
		super.save(finance);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Finance update(Finance finance) {
		return super.update(finance);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Finance update(Finance finance, String... ignoreProperties) {
		return super.update(finance, ignoreProperties);
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
	public void delete(Finance finance) {
		super.delete(finance);
	}

	public Page<Finance> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return financeDao.findPage(beginDate,endDate,pageable);
	}
}