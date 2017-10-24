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
 * @date 2017-9-14 19:42:7
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
		ArticleReview review = articleReviewDao.find(id);
		review.setDeleted(true);
		super.update(review);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void delete(Long... ids) {
		for (Long id:ids) {
			ArticleReview review = articleReviewDao.find(id);
			review.setDeleted(true);
			super.update(review);
		}
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void delete(ArticleReview articleReview) {
		articleReview.setDeleted(true);
		super.update(articleReview);
	}

	public Page<ArticleReview> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return articleReviewDao.findPage(beginDate,endDate,pageable);
	}
}