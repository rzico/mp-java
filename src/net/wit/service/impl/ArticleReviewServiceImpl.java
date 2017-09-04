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

import net.wit.dao.ArticleReviewDao;
import net.wit.entity.*;
import net.wit.service.ArticleReviewService;

/**
 * @ClassName: ArticleReviewDaoImpl
 * @author 降魔战队
 * @date 2017-9-3 21:54:59
 */
 
 
@Service("articleReviewServiceImpl")
public class ArticleReviewServiceImpl extends BaseServiceImpl<ArticleReview, Long> implements ArticleReviewService {
	@Resource(name = "articleReviewDaoImpl")
	private ArticleReviewDao articleReviewDao;

	@Resource(name = "articleReviewDaoImpl")
	public void setBaseDao(ArticleReviewDao articleReviewDao) {
		super.setBaseDao(articleReviewDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(ArticleReview articleReview) {
		super.save(articleReview);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public ArticleReview update(ArticleReview articleReview) {
		return super.update(articleReview);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public ArticleReview update(ArticleReview articleReview, String... ignoreProperties) {
		return super.update(articleReview, ignoreProperties);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void delete(Long id) {
		ArticleReview entity = super.find(id);
		entity.setDeleted(true);
		super.save(entity);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void delete(Long... ids) {
		List<ArticleReview> reviews = super.findList(ids);
		for (ArticleReview review:reviews) {
			review.setDeleted(true);
			super.save(review);
		}
		super.delete(ids);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void delete(ArticleReview articleReview) {
		articleReview.setDeleted(true);
		super.save(articleReview);
	}

	public Page<ArticleReview> findPage(Date beginDate,Date endDate, Pageable pageable) {
	  return articleReviewDao.findPage(beginDate,endDate,pageable);
	}
}