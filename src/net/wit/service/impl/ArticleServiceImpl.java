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
 * @date 2017-9-14 19:42:7
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
		Article article = articleDao.find(id);
		article.setDeleted(true);
		super.update(article);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void delete(Long... ids) {
		for (Long id:ids) {
			Article article = articleDao.find(id);
			article.setDeleted(true);
			super.update(article);
		}
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void delete(Article article) {
		article.setDeleted(true);
		super.update(article);
	}

	public Page<Article> findPage(Date beginDate,Date endDate, List<Tag> tags, Pageable pageable) {
		return articleDao.findPage(beginDate,endDate,tags,pageable);
	}

	public Page<Article> findMemberPage(Date beginDate, Date endDate, List<Member> members, Pageable pageable){
		return articleDao.findMemberPage(beginDate,endDate,members,pageable);
	}
}