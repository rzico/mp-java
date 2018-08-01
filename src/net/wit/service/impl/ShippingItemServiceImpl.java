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

import net.wit.entity.summary.ShippingItemSummary;
import net.wit.entity.summary.ShippingSummary;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.wit.dao.ShippingItemDao;
import net.wit.entity.*;
import net.wit.service.ShippingItemService;

/**
 * @ClassName: ShippingItemDaoImpl
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
 
@Service("shippingItemServiceImpl")
public class ShippingItemServiceImpl extends BaseServiceImpl<ShippingItem, Long> implements ShippingItemService {
	@Resource(name = "shippingItemDaoImpl")
	private ShippingItemDao shippingItemDao;

	@Resource(name = "shippingItemDaoImpl")
	public void setBaseDao(ShippingItemDao shippingItemDao) {
		super.setBaseDao(shippingItemDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(ShippingItem shippingItem) {
		super.save(shippingItem);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public ShippingItem update(ShippingItem shippingItem) {
		return super.update(shippingItem);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public ShippingItem update(ShippingItem shippingItem, String... ignoreProperties) {
		return super.update(shippingItem, ignoreProperties);
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
	public void delete(ShippingItem shippingItem) {
		super.delete(shippingItem);
	}

	public Page<ShippingItem> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return shippingItemDao.findPage(beginDate,endDate,pageable);
	}

	public List<ShippingItemSummary> summary(Enterprise enterprise, Date beginDate, Date endDate,String type, Pageable pageable) {
		return shippingItemDao.summary(enterprise,beginDate,endDate,type,pageable);
	}

}