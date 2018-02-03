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

import net.wit.dao.RechargeDao;
import net.wit.entity.*;
import net.wit.service.RechargeService;

/**
 * @ClassName: RechargeDaoImpl
 * @author 降魔战队
 * @date 2018-2-1 14:1:27
 */


@Service("rechargeServiceImpl")
public class RechargeServiceImpl extends BaseServiceImpl<Recharge, Long> implements RechargeService {
	@Resource(name = "rechargeDaoImpl")
	private RechargeDao rechargeDao;

	@Resource(name = "rechargeDaoImpl")
	public void setBaseDao(RechargeDao rechargeDao) {
		super.setBaseDao(rechargeDao);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(Recharge recharge) {
		super.save(recharge);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Recharge update(Recharge recharge) {
		return super.update(recharge);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Recharge update(Recharge recharge, String... ignoreProperties) {
		return super.update(recharge, ignoreProperties);
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
	public void delete(Recharge recharge) {
		super.delete(recharge);
	}

	public Page<Recharge> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return rechargeDao.findPage(beginDate,endDate,pageable);
	}
}