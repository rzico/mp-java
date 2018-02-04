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

import net.wit.dao.CustomServiceDao;
import net.wit.entity.*;
import net.wit.service.CustomServiceService;

/**
 * @ClassName: CustomServiceDaoImpl
 * @author 降魔战队
 * @date 2018-2-3 21:3:50
 */
 
 
@Service("customServiceServiceImpl")
public class CustomServiceServiceImpl extends BaseServiceImpl<CustomService, Long> implements CustomServiceService {
	@Resource(name = "customServiceDaoImpl")
	private CustomServiceDao customServiceDao;

	@Resource(name = "customServiceDaoImpl")
	public void setBaseDao(CustomServiceDao customServiceDao) {
		super.setBaseDao(customServiceDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(CustomService customService) {
		super.save(customService);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public CustomService update(CustomService customService) {
		return super.update(customService);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public CustomService update(CustomService customService, String... ignoreProperties) {
		return super.update(customService, ignoreProperties);
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
	public void delete(CustomService customService) {
		super.delete(customService);
	}

	public Page<CustomService> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return customServiceDao.findPage(beginDate,endDate,pageable);
	}
}