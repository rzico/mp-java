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

import net.wit.dao.ArticleDao;
import net.wit.entity.*;
import net.wit.service.ArticleService;

/**
 * @ClassName: ArticleDaoImpl
 * @author 降魔战队
 * @date 2017-9-3 21:54:58
 */
 
 
@Service("articleServiceImpl")
public class ArticleServiceImpl extends BaseServiceImpl<Article, Long> implements ArticleService {
	@Resource(name = "articleDaoImpl")
	private ArticleDao articleDao;

	@Resource(name = "articleDaoImpl")
	public void setBaseDao(ArticleDao articleDao) {
		super.setBaseDao(articleDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(Article article) {
		super.save(article);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Article update(Article article) {
		return super.update(article);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Article update(Article article, String... ignoreProperties) {
		return super.update(article, ignoreProperties);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void delete(Long id) {
		Article entity = super.find(id);
		entity.setDeleted(true);
		super.save(entity);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void delete(Long... ids) {
		List<Article> entitys = super.findList(ids);
		for (Article article:entitys) {
			article.setDeleted(true);
			super.save(article);
		}
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void delete(Article article) {
		article.setDeleted(true);
		super.save(article);
	}

	public Page<Article> findPage(Date beginDate,Date endDate, Pageable pageable) {
	  return articleDao.findPage(beginDate,endDate,pageable);
	}
}