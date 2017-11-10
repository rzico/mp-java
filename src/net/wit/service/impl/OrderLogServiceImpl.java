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

import net.wit.dao.OrderLogDao;
import net.wit.entity.*;
import net.wit.service.OrderLogService;

/**
 * @ClassName: OrderLogDaoImpl
 * @author 降魔战队
 * @date 2017-9-14 19:42:8
 */
 
 
@Service("orderLogServiceImpl")
public class OrderLogServiceImpl extends BaseServiceImpl<OrderLog, Long> implements OrderLogService {
	@Resource(name = "orderLogDaoImpl")
	private OrderLogDao orderLogDao;

	@Resource(name = "orderLogDaoImpl")
	public void setBaseDao(OrderLogDao orderLogDao) {
		super.setBaseDao(orderLogDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(OrderLog orderLog) {
		super.save(orderLog);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public OrderLog update(OrderLog orderLog) {
		return super.update(orderLog);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public OrderLog update(OrderLog orderLog, String... ignoreProperties) {
		return super.update(orderLog, ignoreProperties);
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
	public void delete(OrderLog orderLog) {
		super.delete(orderLog);
	}

	public Page<OrderLog> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return orderLogDao.findPage(beginDate,endDate,pageable);
	}
}