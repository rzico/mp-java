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

import net.wit.dao.LiveTapeDao;
import net.wit.entity.*;
import net.wit.service.LiveTapeService;

/**
 * @ClassName: LiveTapeDaoImpl
 * @author 降魔战队
 * @date 2018-4-5 18:22:33
 */
 
 
@Service("liveTapeServiceImpl")
public class LiveTapeServiceImpl extends BaseServiceImpl<LiveTape, Long> implements LiveTapeService {
	@Resource(name = "liveTapeDaoImpl")
	private LiveTapeDao liveTapeDao;

	@Resource(name = "liveTapeDaoImpl")
	public void setBaseDao(LiveTapeDao liveTapeDao) {
		super.setBaseDao(liveTapeDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(LiveTape liveTape) {
		super.save(liveTape);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public LiveTape update(LiveTape liveTape) {
		return super.update(liveTape);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public LiveTape update(LiveTape liveTape, String... ignoreProperties) {
		return super.update(liveTape, ignoreProperties);
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
	public void delete(LiveTape liveTape) {
		super.delete(liveTape);
	}

	public Page<LiveTape> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return liveTapeDao.findPage(beginDate,endDate,pageable);
	}
}