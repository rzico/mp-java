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

import net.wit.dao.OccupationDao;
import net.wit.entity.*;
import net.wit.service.OccupationService;

/**
 * @ClassName: OccupationDaoImpl
 * @author 降魔战队
 * @date 2017-9-14 19:42:8
 */
 
 
@Service("occupationServiceImpl")
public class OccupationServiceImpl extends BaseServiceImpl<Occupation, Long> implements OccupationService {
	@Resource(name = "occupationDaoImpl")
	private OccupationDao occupationDao;

	@Resource(name = "occupationDaoImpl")
	public void setBaseDao(OccupationDao occupationDao) {
		super.setBaseDao(occupationDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(Occupation occupation) {
		super.save(occupation);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Occupation update(Occupation occupation) {
		return super.update(occupation);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Occupation update(Occupation occupation, String... ignoreProperties) {
		return super.update(occupation, ignoreProperties);
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
	public void delete(Occupation occupation) {
		super.delete(occupation);
	}

	public Page<Occupation> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return occupationDao.findPage(beginDate,endDate,pageable);
	}
}