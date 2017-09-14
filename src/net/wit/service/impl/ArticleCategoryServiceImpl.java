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

import net.wit.dao.ArticleCategoryDao;
import net.wit.entity.*;
import net.wit.service.ArticleCategoryService;

/**
 * @ClassName: ArticleCategoryDaoImpl
 * @author 降魔战队
 * @date 2017-9-14 19:42:7
 */
 
 
@Service("articleCategoryServiceImpl")
public class ArticleCategoryServiceImpl extends BaseServiceImpl<ArticleCategory, Long> implements ArticleCategoryService {
	@Resource(name = "articleCategoryDaoImpl")
	private ArticleCategoryDao articleCategoryDao;

	@Resource(name = "articleCategoryDaoImpl")
	public void setBaseDao(ArticleCategoryDao articleCategoryDao) {
		super.setBaseDao(articleCategoryDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(ArticleCategory articleCategory) {
		super.save(articleCategory);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public ArticleCategory update(ArticleCategory articleCategory) {
		return super.update(articleCategory);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public ArticleCategory update(ArticleCategory articleCategory, String... ignoreProperties) {
		return super.update(articleCategory, ignoreProperties);
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
	public void delete(ArticleCategory articleCategory) {
		super.delete(articleCategory);
	}

	public Page<ArticleCategory> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return articleCategoryDao.findPage(beginDate,endDate,pageable);
	}
}