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

import net.wit.dao.LiveGroupDao;
import net.wit.entity.*;
import net.wit.service.LiveGroupService;

/**
 * @ClassName: LiveGroupDaoImpl
 * @author 降魔战队
 * @date 2018-4-5 18:22:33
 */
 
 
@Service("liveGroupServiceImpl")
public class LiveGroupServiceImpl extends BaseServiceImpl<LiveGroup, Long> implements LiveGroupService {
	@Resource(name = "liveGroupDaoImpl")
	private LiveGroupDao liveGroupDao;

	@Resource(name = "liveGroupDaoImpl")
	public void setBaseDao(LiveGroupDao liveGroupDao) {
		super.setBaseDao(liveGroupDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(LiveGroup liveGroup) {
		super.save(liveGroup);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public LiveGroup update(LiveGroup liveGroup) {
		return super.update(liveGroup);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public LiveGroup update(LiveGroup liveGroup, String... ignoreProperties) {
		return super.update(liveGroup, ignoreProperties);
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
	public void delete(LiveGroup liveGroup) {
		super.delete(liveGroup);
	}

	public Page<LiveGroup> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return liveGroupDao.findPage(beginDate,endDate,pageable);
	}
}