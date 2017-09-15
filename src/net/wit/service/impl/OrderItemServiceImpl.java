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

import net.wit.dao.OrderItemDao;
import net.wit.entity.*;
import net.wit.service.OrderItemService;

/**
 * @ClassName: OrderItemDaoImpl
 * @author 降魔战队
 * @date 2017-9-14 19:42:8
 */
 
 
@Service("orderItemServiceImpl")
public class OrderItemServiceImpl extends BaseServiceImpl<OrderItem, Long> implements OrderItemService {
	@Resource(name = "orderItemDaoImpl")
	private OrderItemDao orderItemDao;

	@Resource(name = "orderItemDaoImpl")
	public void setBaseDao(OrderItemDao orderItemDao) {
		super.setBaseDao(orderItemDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(OrderItem orderItem) {
		super.save(orderItem);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public OrderItem update(OrderItem orderItem) {
		return super.update(orderItem);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public OrderItem update(OrderItem orderItem, String... ignoreProperties) {
		return super.update(orderItem, ignoreProperties);
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
	public void delete(OrderItem orderItem) {
		super.delete(orderItem);
	}

	public Page<OrderItem> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return orderItemDao.findPage(beginDate,endDate,pageable);
	}
}