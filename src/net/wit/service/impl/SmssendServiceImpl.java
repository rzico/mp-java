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

import net.wit.dao.SmssendDao;
import net.wit.entity.*;
import net.wit.service.SmssendService;

/**
 * @ClassName: SmssendDaoImpl
 * @author 降魔战队
 * @date 2017-9-3 21:55:0
 */
 
 
@Service("smssendServiceImpl")
public class SmssendServiceImpl extends BaseServiceImpl<Smssend, Long> implements SmssendService {
	@Resource(name = "smssendDaoImpl")
	private SmssendDao smssendDao;

	@Resource(name = "smssendDaoImpl")
	public void setBaseDao(SmssendDao smssendDao) {
		super.setBaseDao(smssendDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(Smssend smssend) {
		super.save(smssend);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Smssend update(Smssend smssend) {
		return super.update(smssend);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Smssend update(Smssend smssend, String... ignoreProperties) {
		return super.update(smssend, ignoreProperties);
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
	public void delete(Smssend smssend) {
		super.delete(smssend);
	}

	public Page<Smssend> findPage(Date beginDate,Date endDate, Pageable pageable) {
	  return smssendDao.findPage(beginDate,endDate,pageable);
	}
}