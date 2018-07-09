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

import net.wit.dao.GaugeQuestionDao;
import net.wit.entity.*;
import net.wit.service.GaugeQuestionService;

/**
 * @ClassName: GaugeQuestionDaoImpl
 * @author 降魔战队
 * @date 2018-2-12 21:4:37
 */
 
 
@Service("gaugeQuestionServiceImpl")
public class GaugeQuestionServiceImpl extends BaseServiceImpl<GaugeQuestion, Long> implements GaugeQuestionService {
	@Resource(name = "gaugeQuestionDaoImpl")
	private GaugeQuestionDao gaugeQuestionDao;

	@Resource(name = "gaugeQuestionDaoImpl")
	public void setBaseDao(GaugeQuestionDao gaugeQuestionDao) {
		super.setBaseDao(gaugeQuestionDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(GaugeQuestion gaugeQuestion) {
		super.save(gaugeQuestion);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public GaugeQuestion update(GaugeQuestion gaugeQuestion) {
		return super.update(gaugeQuestion);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public GaugeQuestion update(GaugeQuestion gaugeQuestion, String... ignoreProperties) {
		return super.update(gaugeQuestion, ignoreProperties);
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
	public void delete(GaugeQuestion gaugeQuestion) {
		super.delete(gaugeQuestion);
	}

	public Page<GaugeQuestion> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return gaugeQuestionDao.findPage(beginDate,endDate,pageable);
	}
}