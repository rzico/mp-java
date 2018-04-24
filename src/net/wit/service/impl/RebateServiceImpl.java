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

import net.wit.entity.summary.RebateSummary;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.wit.dao.RebateDao;
import net.wit.entity.*;
import net.wit.service.RebateService;

/**
 * @ClassName: RebateDaoImpl
 * @author 降魔战队
 * @date 2018-4-24 20:57:53
 */
 
 
@Service("rebateServiceImpl")
public class RebateServiceImpl extends BaseServiceImpl<Rebate, Long> implements RebateService {
	@Resource(name = "rebateDaoImpl")
	private RebateDao rebateDao;

	@Resource(name = "rebateDaoImpl")
	public void setBaseDao(RebateDao rebateDao) {
		super.setBaseDao(rebateDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(Rebate rebate) {
		super.save(rebate);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Rebate update(Rebate rebate) {
		return super.update(rebate);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Rebate update(Rebate rebate, String... ignoreProperties) {
		return super.update(rebate, ignoreProperties);
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
	public void delete(Rebate rebate) {
		super.delete(rebate);
	}

	public Page<Rebate> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return rebateDao.findPage(beginDate,endDate,pageable);
	}

	public Page<RebateSummary> sumPage(Date beginDate, Date endDate, Pageable pageable) {
		return rebateDao.sumPage(beginDate,endDate,pageable);
	}

}