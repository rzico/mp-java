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

import net.wit.dao.ProductStockDao;
import net.wit.entity.*;
import net.wit.service.ProductStockService;

/**
 * @ClassName: ProductStockDaoImpl
 * @author 降魔战队
 * @date 2017-9-14 19:42:8
 */
 
 
@Service("productStockServiceImpl")
public class ProductStockServiceImpl extends BaseServiceImpl<ProductStock, Long> implements ProductStockService {
	@Resource(name = "productStockDaoImpl")
	private ProductStockDao productStockDao;

	@Resource(name = "productStockDaoImpl")
	public void setBaseDao(ProductStockDao productStockDao) {
		super.setBaseDao(productStockDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(ProductStock productStock) {
		super.save(productStock);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public ProductStock update(ProductStock productStock) {
		return super.update(productStock);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public ProductStock update(ProductStock productStock, String... ignoreProperties) {
		return super.update(productStock, ignoreProperties);
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
	public void delete(ProductStock productStock) {
		super.delete(productStock);
	}

	public Page<ProductStock> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return productStockDao.findPage(beginDate,endDate,pageable);
	}
}