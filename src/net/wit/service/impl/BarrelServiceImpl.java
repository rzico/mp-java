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

import net.wit.dao.BarrelDao;
import net.wit.entity.*;
import net.wit.service.BarrelService;

/**
 * @ClassName: BarrelDaoImpl
 * @author 降魔战队
 * @date 2018-5-28 15:8:46
 */
 
 
@Service("barrelServiceImpl")
public class BarrelServiceImpl extends BaseServiceImpl<Barrel, Long> implements BarrelService {
	@Resource(name = "barrelDaoImpl")
	private BarrelDao barrelDao;

	@Resource(name = "barrelDaoImpl")
	public void setBaseDao(BarrelDao barrelDao) {
		super.setBaseDao(barrelDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(Barrel barrel) {
		super.save(barrel);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Barrel update(Barrel barrel) {
		return super.update(barrel);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Barrel update(Barrel barrel, String... ignoreProperties) {
		return super.update(barrel, ignoreProperties);
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
	public void delete(Barrel barrel) {
		super.delete(barrel);
	}

	public Page<Barrel> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return barrelDao.findPage(beginDate,endDate,pageable);
	}
}