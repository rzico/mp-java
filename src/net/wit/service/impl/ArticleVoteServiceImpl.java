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

import net.wit.dao.ArticleVoteDao;
import net.wit.entity.*;
import net.wit.service.ArticleVoteService;

/**
 * @ClassName: ArticleVoteDaoImpl
 * @author 降魔战队
 * @date 2017-9-14 19:42:7
 */
 
 
@Service("articleVoteServiceImpl")
public class ArticleVoteServiceImpl extends BaseServiceImpl<ArticleVote, Long> implements ArticleVoteService {
	@Resource(name = "articleVoteDaoImpl")
	private ArticleVoteDao articleVoteDao;

	@Resource(name = "articleVoteDaoImpl")
	public void setBaseDao(ArticleVoteDao articleVoteDao) {
		super.setBaseDao(articleVoteDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(ArticleVote articleVote) {
		super.save(articleVote);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public ArticleVote update(ArticleVote articleVote) {
		return super.update(articleVote);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public ArticleVote update(ArticleVote articleVote, String... ignoreProperties) {
		return super.update(articleVote, ignoreProperties);
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
	public void delete(ArticleVote articleVote) {
		super.delete(articleVote);
	}

	public Page<ArticleVote> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return articleVoteDao.findPage(beginDate,endDate,pageable);
	}
}