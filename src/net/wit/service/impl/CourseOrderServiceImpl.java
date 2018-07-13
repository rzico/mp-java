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

import net.wit.dao.CourseOrderDao;
import net.wit.entity.*;
import net.wit.service.CourseOrderService;

/**
 * @ClassName: CourseOrderDaoImpl
 * @author 降魔战队
 * @date 2018-7-13 14:38:35
 */
 
 
@Service("courseOrderServiceImpl")
public class CourseOrderServiceImpl extends BaseServiceImpl<CourseOrder, Long> implements CourseOrderService {
	@Resource(name = "courseOrderDaoImpl")
	private CourseOrderDao courseOrderDao;

	@Resource(name = "courseOrderDaoImpl")
	public void setBaseDao(CourseOrderDao courseOrderDao) {
		super.setBaseDao(courseOrderDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(CourseOrder courseOrder) {
		super.save(courseOrder);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public CourseOrder update(CourseOrder courseOrder) {
		return super.update(courseOrder);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public CourseOrder update(CourseOrder courseOrder, String... ignoreProperties) {
		return super.update(courseOrder, ignoreProperties);
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
	public void delete(CourseOrder courseOrder) {
		super.delete(courseOrder);
	}

	public Page<CourseOrder> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return courseOrderDao.findPage(beginDate,endDate,pageable);
	}
}