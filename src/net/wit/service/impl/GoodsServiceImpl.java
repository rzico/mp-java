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

import net.wit.dao.GoodsDao;
import net.wit.entity.*;
import net.wit.service.GoodsService;

/**
 * @ClassName: GoodsDaoImpl
 * @author 降魔战队
 * @date 2017-9-14 19:42:8
 */
 
 
@Service("goodsServiceImpl")
public class GoodsServiceImpl extends BaseServiceImpl<Goods, Long> implements GoodsService {
	@Resource(name = "goodsDaoImpl")
	private GoodsDao goodsDao;

	@Resource(name = "goodsDaoImpl")
	public void setBaseDao(GoodsDao goodsDao) {
		super.setBaseDao(goodsDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(Goods goods) {
		super.save(goods);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Goods update(Goods goods) {
		return super.update(goods);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Goods update(Goods goods, String... ignoreProperties) {
		return super.update(goods, ignoreProperties);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void delete(Long id) {
		Goods goods = goodsDao.find(id);
		for (Product product:goods.getProducts()) {
			product.setDeleted(true);
		}
		super.update(goods);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void delete(Long... ids) {
		for (Long id:ids) {
			this.delete(id);
		}
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void delete(Goods goods) {
		for (Product product:goods.getProducts()) {
			product.setDeleted(true);
		}
		super.update(goods);
	}

	public Page<Goods> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return goodsDao.findPage(beginDate,endDate,pageable);
	}
}