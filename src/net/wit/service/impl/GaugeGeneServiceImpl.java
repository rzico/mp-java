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

import net.wit.dao.GaugeGeneDao;
import net.wit.entity.*;
import net.wit.service.GaugeGeneService;

/**
 * @ClassName: GaugeGeneDaoImpl
 * @author 降魔战队
 * @date 2018-2-12 21:4:37
 */
 
 
@Service("gaugeGeneServiceImpl")
public class GaugeGeneServiceImpl extends BaseServiceImpl<GaugeGene, Long> implements GaugeGeneService {
	@Resource(name = "gaugeGeneDaoImpl")
	private GaugeGeneDao gaugeGeneDao;

	@Resource(name = "gaugeGeneDaoImpl")
	public void setBaseDao(GaugeGeneDao gaugeGeneDao) {
		super.setBaseDao(gaugeGeneDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(GaugeGene gaugeGene) {
		super.save(gaugeGene);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public GaugeGene update(GaugeGene gaugeGene) {
		return super.update(gaugeGene);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public GaugeGene update(GaugeGene gaugeGene, String... ignoreProperties) {
		return super.update(gaugeGene, ignoreProperties);
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
	public void delete(GaugeGene gaugeGene) {
		super.delete(gaugeGene);
	}

	public Page<GaugeGene> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return gaugeGeneDao.findPage(beginDate,endDate,pageable);
	}
}