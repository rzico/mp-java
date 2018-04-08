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

import net.wit.dao.LiveGiftDao;
import net.wit.entity.*;
import net.wit.service.LiveGiftService;

/**
 * @ClassName: LiveGiftDaoImpl
 * @author 降魔战队
 * @date 2018-4-5 18:22:33
 */
 
 
@Service("liveGiftServiceImpl")
public class LiveGiftServiceImpl extends BaseServiceImpl<LiveGift, Long> implements LiveGiftService {
	@Resource(name = "liveGiftDaoImpl")
	private LiveGiftDao liveGiftDao;

	@Resource(name = "liveGiftDaoImpl")
	public void setBaseDao(LiveGiftDao liveGiftDao) {
		super.setBaseDao(liveGiftDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(LiveGift liveGift) {
		super.save(liveGift);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public LiveGift update(LiveGift liveGift) {
		return super.update(liveGift);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public LiveGift update(LiveGift liveGift, String... ignoreProperties) {
		return super.update(liveGift, ignoreProperties);
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
	public void delete(LiveGift liveGift) {
		super.delete(liveGift);
	}

	public Page<LiveGift> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return liveGiftDao.findPage(beginDate,endDate,pageable);
	}


	public void add(LiveGift gift, Member member, Live live) throws Exception {

	}

}