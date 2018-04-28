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

import net.wit.dao.RoadDao;
import net.wit.entity.*;
import net.wit.service.RoadService;

/**
 * @ClassName: RoadDaoImpl
 * @author 降魔战队
 * @date 2018-4-28 14:18:48
 */
 
 
@Service("roadServiceImpl")
public class RoadServiceImpl extends BaseServiceImpl<Road, Long> implements RoadService {
	@Resource(name = "roadDaoImpl")
	private RoadDao roadDao;

	@Resource(name = "roadDaoImpl")
	public void setBaseDao(RoadDao roadDao) {
		super.setBaseDao(roadDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(Road road) {
		super.save(road);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Road update(Road road) {
		return super.update(road);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Road update(Road road, String... ignoreProperties) {
		return super.update(road, ignoreProperties);
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
	public void delete(Road road) {
		super.delete(road);
	}

	public Page<Road> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return roadDao.findPage(beginDate,endDate,pageable);
	}
}