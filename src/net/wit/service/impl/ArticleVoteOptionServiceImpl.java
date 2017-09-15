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

import net.wit.dao.ArticleVoteOptionDao;
import net.wit.entity.*;
import net.wit.service.ArticleVoteOptionService;

/**
 * @ClassName: ArticleVoteOptionDaoImpl
 * @author 降魔战队
 * @date 2017-9-14 19:42:7
 */
 
 
@Service("articleVoteOptionServiceImpl")
public class ArticleVoteOptionServiceImpl extends BaseServiceImpl<ArticleVoteOption, Long> implements ArticleVoteOptionService {
	@Resource(name = "articleVoteOptionDaoImpl")
	private ArticleVoteOptionDao articleVoteOptionDao;

	@Resource(name = "articleVoteOptionDaoImpl")
	public void setBaseDao(ArticleVoteOptionDao articleVoteOptionDao) {
		super.setBaseDao(articleVoteOptionDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(ArticleVoteOption articleVoteOption) {
		super.save(articleVoteOption);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public ArticleVoteOption update(ArticleVoteOption articleVoteOption) {
		return super.update(articleVoteOption);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public ArticleVoteOption update(ArticleVoteOption articleVoteOption, String... ignoreProperties) {
		return super.update(articleVoteOption, ignoreProperties);
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
	public void delete(ArticleVoteOption articleVoteOption) {
		super.delete(articleVoteOption);
	}

	public Page<ArticleVoteOption> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return articleVoteOptionDao.findPage(beginDate,endDate,pageable);
	}
}