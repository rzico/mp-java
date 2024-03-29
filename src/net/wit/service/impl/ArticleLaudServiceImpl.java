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

import net.wit.dao.ArticleDao;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.wit.dao.ArticleLaudDao;
import net.wit.entity.*;
import net.wit.service.ArticleLaudService;

/**
 * @ClassName: ArticleLaudDaoImpl
 * @author 降魔战队
 * @date 2017-9-14 19:42:7
 */
 
 
@Service("articleLaudServiceImpl")
public class ArticleLaudServiceImpl extends BaseServiceImpl<ArticleLaud, Long> implements ArticleLaudService {
	@Resource(name = "articleLaudDaoImpl")
	private ArticleLaudDao articleLaudDao;
	@Resource(name = "articleDaoImpl")
	private ArticleDao articleDao;

	@Resource(name = "articleLaudDaoImpl")
	public void setBaseDao(ArticleLaudDao articleLaudDao) {
		super.setBaseDao(articleLaudDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(ArticleLaud articleLaud) {
		Article article = articleLaud.getArticle();
		article.setLaud(article.getLaud()+1);
		articleDao.merge(article);
		super.save(articleLaud);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public ArticleLaud update(ArticleLaud articleLaud)  {
		return super.update(articleLaud);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public ArticleLaud update(ArticleLaud articleLaud, String... ignoreProperties) {
		return super.update(articleLaud, ignoreProperties);
	}


	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void delete(Long id) {
		ArticleLaud articleLaud = articleLaudDao.find(id);
		Article article = articleLaud.getArticle();
		article.setLaud(article.getLaud()-1);
		articleDao.merge(article);
		super.delete(articleLaud);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void delete(Long... ids) {
		for (Long id:ids) {
			ArticleLaud articleLaud = articleLaudDao.find(id);
			Article article = articleLaud.getArticle();
			article.setLaud(article.getLaud()-1);
			articleDao.merge(article);
			super.delete(articleLaud);
		}
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void delete(ArticleLaud articleLaud) {
		Article article = articleLaud.getArticle();
		article.setLaud(article.getLaud()-1);
		articleDao.merge(article);
		super.delete(articleLaud);
	}

	public Page<ArticleLaud> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return articleLaudDao.findPage(beginDate,endDate,pageable);
	}
}