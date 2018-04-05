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

import net.wit.dao.OrderRankingDao;
import net.wit.entity.*;
import net.wit.service.OrderRankingService;

/**
 * @ClassName: OrderRankingDaoImpl
 * @author 降魔战队
 * @date 2018-4-5 18:22:33
 */
 
 
@Service("orderRankingServiceImpl")
public class OrderRankingServiceImpl extends BaseServiceImpl<OrderRanking, Long> implements OrderRankingService {
	@Resource(name = "orderRankingDaoImpl")
	private OrderRankingDao orderRankingDao;

	@Resource(name = "orderRankingDaoImpl")
	public void setBaseDao(OrderRankingDao orderRankingDao) {
		super.setBaseDao(orderRankingDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(OrderRanking orderRanking) {
		super.save(orderRanking);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public OrderRanking update(OrderRanking orderRanking) {
		return super.update(orderRanking);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public OrderRanking update(OrderRanking orderRanking, String... ignoreProperties) {
		return super.update(orderRanking, ignoreProperties);
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
	public void delete(OrderRanking orderRanking) {
		super.delete(orderRanking);
	}

	public Page<OrderRanking> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return orderRankingDao.findPage(beginDate,endDate,pageable);
	}
}