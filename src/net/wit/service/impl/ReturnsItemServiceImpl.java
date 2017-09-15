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

import net.wit.dao.ReturnsItemDao;
import net.wit.entity.*;
import net.wit.service.ReturnsItemService;

/**
 * @ClassName: ReturnsItemDaoImpl
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
 
@Service("returnsItemServiceImpl")
public class ReturnsItemServiceImpl extends BaseServiceImpl<ReturnsItem, Long> implements ReturnsItemService {
	@Resource(name = "returnsItemDaoImpl")
	private ReturnsItemDao returnsItemDao;

	@Resource(name = "returnsItemDaoImpl")
	public void setBaseDao(ReturnsItemDao returnsItemDao) {
		super.setBaseDao(returnsItemDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(ReturnsItem returnsItem) {
		super.save(returnsItem);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public ReturnsItem update(ReturnsItem returnsItem) {
		return super.update(returnsItem);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public ReturnsItem update(ReturnsItem returnsItem, String... ignoreProperties) {
		return super.update(returnsItem, ignoreProperties);
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
	public void delete(ReturnsItem returnsItem) {
		super.delete(returnsItem);
	}

	public Page<ReturnsItem> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return returnsItemDao.findPage(beginDate,endDate,pageable);
	}
}