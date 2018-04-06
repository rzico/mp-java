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

import net.wit.dao.LiveGroupDao;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.wit.dao.LiveDao;
import net.wit.entity.*;
import net.wit.service.LiveService;

/**
 * @ClassName: LiveDaoImpl
 * @author 降魔战队
 * @date 2018-4-5 18:22:32
 */
 
 
@Service("liveServiceImpl")
public class LiveServiceImpl extends BaseServiceImpl<Live, Long> implements LiveService {
	@Resource(name = "liveDaoImpl")
	private LiveDao liveDao;
	@Resource(name = "liveGroupDaoImpl")
	private LiveGroupDao liveGroupDao;
	@Resource(name = "liveDaoImpl")
	public void setBaseDao(LiveDao liveDao) {
		super.setBaseDao(liveDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(Live live) {
		super.save(live);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Live update(Live live) {
		return super.update(live);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Live update(Live live, String... ignoreProperties) {
		return super.update(live, ignoreProperties);
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
	public void delete(Live live) {
		super.delete(live);
	}

	public Page<Live> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return liveDao.findPage(beginDate,endDate,pageable);
	}

	public Payment create(Live live) throws Exception {
		live.setStatus(Live.Status.success);
        liveDao.persist(live);
        return null;
	}
}