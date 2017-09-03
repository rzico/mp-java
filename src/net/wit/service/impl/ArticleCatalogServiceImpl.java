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

import net.wit.dao.ArticleCatalogDao;
import net.wit.entity.*;
import net.wit.service.ArticleCatalogService;

/**
 * @ClassName: ArticleCatalogDaoImpl
 * @author 降魔战队
 * @date 2017-9-3 21:54:58
 */
 
 
@Service("articleCatalogServiceImpl")
public class ArticleCatalogServiceImpl extends BaseServiceImpl<ArticleCatalog, Long> implements ArticleCatalogService {
	@Resource(name = "articleCatalogDaoImpl")
	private ArticleCatalogDao articleCatalogDao;

	@Resource(name = "articleCatalogDaoImpl")
	public void setBaseDao(ArticleCatalogDao articleCatalogDao) {
		super.setBaseDao(articleCatalogDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(ArticleCatalog articleCatalog) {
		super.save(articleCatalog);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public ArticleCatalog update(ArticleCatalog articleCatalog) {
		return super.update(articleCatalog);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public ArticleCatalog update(ArticleCatalog articleCatalog, String... ignoreProperties) {
		return super.update(articleCatalog, ignoreProperties);
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
	public void delete(ArticleCatalog articleCatalog) {
		super.delete(articleCatalog);
	}

	public Page<ArticleCatalog> findPage(Date beginDate,Date endDate, Pageable pageable) {
	  return articleCatalogDao.findPage(beginDate,endDate,pageable);
	}
}