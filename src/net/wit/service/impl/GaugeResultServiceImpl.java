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

import net.wit.dao.GaugeResultDao;
import net.wit.entity.*;
import net.wit.service.GaugeResultService;

/**
 * @ClassName: GaugeResultDaoImpl
 * @author 降魔战队
 * @date 2018-2-12 21:4:37
 */
 
 
@Service("gaugeResultServiceImpl")
public class GaugeResultServiceImpl extends BaseServiceImpl<GaugeResult, Long> implements GaugeResultService {
	@Resource(name = "gaugeResultDaoImpl")
	private GaugeResultDao gaugeResultDao;

	@Resource(name = "gaugeResultDaoImpl")
	public void setBaseDao(GaugeResultDao gaugeResultDao) {
		super.setBaseDao(gaugeResultDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(GaugeResult gaugeResult) {
		super.save(gaugeResult);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public GaugeResult update(GaugeResult gaugeResult) {
		return super.update(gaugeResult);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public GaugeResult update(GaugeResult gaugeResult, String... ignoreProperties) {
		return super.update(gaugeResult, ignoreProperties);
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
	public void delete(GaugeResult gaugeResult) {
		super.delete(gaugeResult);
	}

	public Page<GaugeResult> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return gaugeResultDao.findPage(beginDate,endDate,pageable);
	}
}