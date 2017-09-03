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

import net.wit.dao.RefundsDao;
import net.wit.entity.*;
import net.wit.service.RefundsService;

/**
 * @ClassName: RefundsDaoImpl
 * @author 降魔战队
 * @date 2017-9-3 21:55:0
 */
 
 
@Service("refundsServiceImpl")
public class RefundsServiceImpl extends BaseServiceImpl<Refunds, Long> implements RefundsService {
	@Resource(name = "refundsDaoImpl")
	private RefundsDao refundsDao;

	@Resource(name = "refundsDaoImpl")
	public void setBaseDao(RefundsDao refundsDao) {
		super.setBaseDao(refundsDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(Refunds refunds) {
		super.save(refunds);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Refunds update(Refunds refunds) {
		return super.update(refunds);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Refunds update(Refunds refunds, String... ignoreProperties) {
		return super.update(refunds, ignoreProperties);
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
	public void delete(Refunds refunds) {
		super.delete(refunds);
	}

	public Page<Refunds> findPage(Date beginDate,Date endDate, Pageable pageable) {
	  return refundsDao.findPage(beginDate,endDate,pageable);
	}
}