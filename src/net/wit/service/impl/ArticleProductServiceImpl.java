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

import net.wit.dao.ArticleProductDao;
import net.wit.entity.*;
import net.wit.service.ArticleProductService;

/**
 * @ClassName: ArticleProductDaoImpl
 * @author 降魔战队
 * @date 2017-9-14 19:42:7
 */
 
 
@Service("articleProductServiceImpl")
public class ArticleProductServiceImpl extends BaseServiceImpl<ArticleProduct, Long> implements ArticleProductService {
	@Resource(name = "articleProductDaoImpl")
	private ArticleProductDao articleProductDao;

	@Resource(name = "articleProductDaoImpl")
	public void setBaseDao(ArticleProductDao articleProductDao) {
		super.setBaseDao(articleProductDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(ArticleProduct articleProduct) {
		super.save(articleProduct);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public ArticleProduct update(ArticleProduct articleProduct) {
		return super.update(articleProduct);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public ArticleProduct update(ArticleProduct articleProduct, String... ignoreProperties) {
		return super.update(articleProduct, ignoreProperties);
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
	public void delete(ArticleProduct articleProduct) {
		super.delete(articleProduct);
	}

	public Page<ArticleProduct> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return articleProductDao.findPage(beginDate,endDate,pageable);
	}
}