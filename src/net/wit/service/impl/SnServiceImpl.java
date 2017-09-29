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

import net.wit.dao.SnDao;
import net.wit.entity.*;
import net.wit.service.SnService;

/**
 * @ClassName: SnDaoImpl
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
 
@Service("snServiceImpl")
public class SnServiceImpl extends BaseServiceImpl<Sn, Long> implements SnService {
	@Resource(name = "snDaoImpl")
	private SnDao snDao;

	@Resource(name = "snDaoImpl")
	public void setBaseDao(SnDao snDao) {
		super.setBaseDao(snDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(Sn sn) {
		super.save(sn);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Sn update(Sn sn) {
		return super.update(sn);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Sn update(Sn sn, String... ignoreProperties) {
		return super.update(sn, ignoreProperties);
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
	public void delete(Sn sn) {
		super.delete(sn);
	}

	@Transactional
	public String generate(Sn.Type type) {
		return snDao.generate(type);
	}

	public Page<Sn> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return snDao.findPage(beginDate,endDate,pageable);
	}


}