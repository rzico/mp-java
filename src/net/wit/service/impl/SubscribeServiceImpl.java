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

import net.wit.dao.SubscribeDao;
import net.wit.entity.*;
import net.wit.service.SubscribeService;

/**
 * @ClassName: SubscribeDaoImpl
 * @author 降魔战队
 * @date 2018-7-13 14:38:37
 */
 
 
@Service("subscribeServiceImpl")
public class SubscribeServiceImpl extends BaseServiceImpl<Subscribe, Long> implements SubscribeService {
	@Resource(name = "subscribeDaoImpl")
	private SubscribeDao subscribeDao;

	@Resource(name = "subscribeDaoImpl")
	public void setBaseDao(SubscribeDao subscribeDao) {
		super.setBaseDao(subscribeDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(Subscribe subscribe) {
		super.save(subscribe);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Subscribe update(Subscribe subscribe) {
		return super.update(subscribe);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Subscribe update(Subscribe subscribe, String... ignoreProperties) {
		return super.update(subscribe, ignoreProperties);
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
	public void delete(Subscribe subscribe) {
		super.delete(subscribe);
	}

	public Page<Subscribe> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return subscribeDao.findPage(beginDate,endDate,pageable);
	}
}