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

import net.wit.dao.GaugeDao;
import net.wit.entity.*;
import net.wit.service.GaugeService;

/**
 * @ClassName: GaugeDaoImpl
 * @author 降魔战队
 * @date 2018-2-12 21:4:37
 */
 
 
@Service("gaugeServiceImpl")
public class GaugeServiceImpl extends BaseServiceImpl<Gauge, Long> implements GaugeService {
	@Resource(name = "gaugeDaoImpl")
	private GaugeDao gaugeDao;

	@Resource(name = "gaugeDaoImpl")
	public void setBaseDao(GaugeDao gaugeDao) {
		super.setBaseDao(gaugeDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(Gauge gauge) {
		super.save(gauge);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Gauge update(Gauge gauge) {
		return super.update(gauge);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Gauge update(Gauge gauge, String... ignoreProperties) {
		return super.update(gauge, ignoreProperties);
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
	public void delete(Gauge gauge) {
		super.delete(gauge);
	}

	public Page<Gauge> findPage(Date beginDate,Date endDate, List<Tag> tags, Pageable pageable) {
		return gaugeDao.findPage(beginDate,endDate,tags,pageable);
	}
}