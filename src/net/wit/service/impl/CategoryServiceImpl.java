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

import net.wit.dao.CategoryDao;
import net.wit.entity.*;
import net.wit.service.CategoryService;

/**
 * @ClassName: CategoryDaoImpl
 * @author 降魔战队
 * @date 2017-9-14 19:42:7
 */
 
 
@Service("categoryServiceImpl")
public class CategoryServiceImpl extends BaseServiceImpl<Category, Long> implements CategoryService {
	@Resource(name = "categoryDaoImpl")
	private CategoryDao categoryDao;

	@Resource(name = "categoryDaoImpl")
	public void setBaseDao(CategoryDao categoryDao) {
		super.setBaseDao(categoryDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(Category category) {
		super.save(category);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Category update(Category category) {
		return super.update(category);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Category update(Category category, String... ignoreProperties) {
		return super.update(category, ignoreProperties);
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
	public void delete(Category category) {
		super.delete(category);
	}

	public Page<Category> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return categoryDao.findPage(beginDate,endDate,pageable);
	}
}