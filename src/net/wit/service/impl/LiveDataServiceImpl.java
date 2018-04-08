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

import net.wit.dao.LiveDataDao;
import net.wit.entity.*;
import net.wit.service.LiveDataService;

/**
 * @ClassName: LiveDataDaoImpl
 * @author 降魔战队
 * @date 2018-4-5 18:22:33
 */
 
 
@Service("liveDataServiceImpl")
public class LiveDataServiceImpl extends BaseServiceImpl<LiveData, Long> implements LiveDataService {
	@Resource(name = "liveDataDaoImpl")
	private LiveDataDao liveDataDao;

	@Resource(name = "liveDataDaoImpl")
	public void setBaseDao(LiveDataDao liveDataDao) {
		super.setBaseDao(liveDataDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(LiveData liveData) {
		super.save(liveData);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public LiveData update(LiveData liveData) {
		return super.update(liveData);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public LiveData update(LiveData liveData, String... ignoreProperties) {
		return super.update(liveData, ignoreProperties);
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
	public void delete(LiveData liveData) {
		super.delete(liveData);
	}

	public Page<LiveData> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return liveDataDao.findPage(beginDate,endDate,pageable);
	}
}