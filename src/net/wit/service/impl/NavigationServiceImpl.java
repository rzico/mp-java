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

import net.wit.dao.NavigationDao;
import net.wit.entity.*;
import net.wit.service.NavigationService;

/**
 * @ClassName: NavigationDaoImpl
 * @author 降魔战队
 * @date 2018-6-12 10:12:13
 */
 
 
@Service("navigationServiceImpl")
public class NavigationServiceImpl extends BaseServiceImpl<Navigation, Long> implements NavigationService {
	@Resource(name = "navigationDaoImpl")
	private NavigationDao navigationDao;

	@Resource(name = "navigationDaoImpl")
	public void setBaseDao(NavigationDao navigationDao) {
		super.setBaseDao(navigationDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(Navigation navigation) {
		super.save(navigation);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Navigation update(Navigation navigation) {
		return super.update(navigation);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Navigation update(Navigation navigation, String... ignoreProperties) {
		return super.update(navigation, ignoreProperties);
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
	public void delete(Navigation navigation) {
		super.delete(navigation);
	}

	public Page<Navigation> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return navigationDao.findPage(beginDate,endDate,pageable);
	}
}