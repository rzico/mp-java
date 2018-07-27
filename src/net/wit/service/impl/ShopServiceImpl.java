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

import net.wit.dao.ShopDao;
import net.wit.entity.*;
import net.wit.service.ShopService;

/**
 * @ClassName: ShopDaoImpl
 * @author 降魔战队
 * @date 2017-11-4 18:12:28
 */
 
 
@Service("shopServiceImpl")
public class ShopServiceImpl extends BaseServiceImpl<Shop, Long> implements ShopService {
	@Resource(name = "shopDaoImpl")
	private ShopDao shopDao;

	@Resource(name = "shopDaoImpl")
	public void setBaseDao(ShopDao shopDao) {
		super.setBaseDao(shopDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(Shop shop) {
		super.save(shop);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Shop update(Shop shop) {
		return super.update(shop);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Shop update(Shop shop, String... ignoreProperties) {
		return super.update(shop, ignoreProperties);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void delete(Long id) {
		Shop shop = shopDao.find(id);
		shop.setDeleted(true);
		super.update(shop);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void delete(Long... ids) {
		for (Long id:ids) {
			Shop shop = shopDao.find(id);
			shop.setDeleted(true);
			super.update(shop);
		}
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void delete(Shop shop) {
		shop.setDeleted(true);
		super.update(shop);
	}

	public Page<Shop> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return shopDao.findPage(beginDate,endDate,pageable);
	}


	public Page<Shop> findPage(Member owner, Pageable pageable) {
		return shopDao.findPage(owner,pageable);
	}
	public Shop find(String code) {
		return shopDao.find(code);
	}

}