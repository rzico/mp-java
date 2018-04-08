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

import net.wit.dao.LiveGiftDataDao;
import net.wit.entity.*;
import net.wit.service.LiveGiftDataService;

/**
 * @ClassName: LiveGiftDataDaoImpl
 * @author 降魔战队
 * @date 2018-4-5 18:22:33
 */
 
 
@Service("liveGiftDataServiceImpl")
public class LiveGiftDataServiceImpl extends BaseServiceImpl<LiveGiftData, Long> implements LiveGiftDataService {
	@Resource(name = "liveGiftDataDaoImpl")
	private LiveGiftDataDao liveGiftDataDao;

	@Resource(name = "liveGiftDataDaoImpl")
	public void setBaseDao(LiveGiftDataDao liveGiftDataDao) {
		super.setBaseDao(liveGiftDataDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(LiveGiftData liveGiftData) {
		super.save(liveGiftData);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public LiveGiftData update(LiveGiftData liveGiftData) {
		return super.update(liveGiftData);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public LiveGiftData update(LiveGiftData liveGiftData, String... ignoreProperties) {
		return super.update(liveGiftData, ignoreProperties);
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
	public void delete(LiveGiftData liveGiftData) {
		super.delete(liveGiftData);
	}

	public Page<LiveGiftData> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return liveGiftDataDao.findPage(beginDate,endDate,pageable);
	}
}