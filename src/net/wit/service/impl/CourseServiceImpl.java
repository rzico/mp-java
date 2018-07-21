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

import net.wit.dao.CourseDao;
import net.wit.entity.*;
import net.wit.service.CourseService;

/**
 * @ClassName: CourseDaoImpl
 * @author 降魔战队
 * @date 2018-7-13 14:38:35
 */
 
 
@Service("courseServiceImpl")
public class CourseServiceImpl extends BaseServiceImpl<Course, Long> implements CourseService {
	@Resource(name = "courseDaoImpl")
	private CourseDao courseDao;

	@Resource(name = "courseDaoImpl")
	public void setBaseDao(CourseDao courseDao) {
		super.setBaseDao(courseDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(Course course) {
		super.save(course);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Course update(Course course) {
		return super.update(course);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Course update(Course course, String... ignoreProperties) {
		return super.update(course, ignoreProperties);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void delete(Long id) {
		Course course = courseDao.find(id);
		this.delete(course);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void delete(Long... ids) {
		for (Long id:ids) {
			this.delete(id);
		}
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void delete(Course course) {
		course.setDeleted(true);
		super.update(course);
	}

	public Page<Course> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return courseDao.findPage(beginDate,endDate,pageable);
	}

	public Page<Course> findPage(Date beginDate,Date endDate, Enterprise enterprise, Pageable pageable) {
		return courseDao.findPage(beginDate,endDate,enterprise,pageable);
	}

}