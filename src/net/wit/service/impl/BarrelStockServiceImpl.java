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

import net.wit.dao.BarrelStockDao;
import net.wit.entity.*;
import net.wit.service.BarrelStockService;

/**
 * @ClassName: BarrelStockDaoImpl
 * @author 降魔战队
 * @date 2018-5-28 15:8:46
 */
 
 
@Service("barrelStockServiceImpl")
public class BarrelStockServiceImpl extends BaseServiceImpl<BarrelStock, Long> implements BarrelStockService {
	@Resource(name = "barrelStockDaoImpl")
	private BarrelStockDao barrelStockDao;

	@Resource(name = "barrelStockDaoImpl")
	public void setBaseDao(BarrelStockDao barrelStockDao) {
		super.setBaseDao(barrelStockDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(BarrelStock barrelStock) {
		super.save(barrelStock);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public BarrelStock update(BarrelStock barrelStock) {
		return super.update(barrelStock);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public BarrelStock update(BarrelStock barrelStock, String... ignoreProperties) {
		return super.update(barrelStock, ignoreProperties);
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
	public void delete(BarrelStock barrelStock) {
		super.delete(barrelStock);
	}

	public Page<BarrelStock> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return barrelStockDao.findPage(beginDate,endDate,pageable);
	}
}