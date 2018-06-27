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

import net.wit.dao.ArticleFormDao;
import net.wit.entity.*;
import net.wit.service.ArticleFormService;

/**
 * @ClassName: ArticleFormDaoImpl
 * @author 降魔战队
 * @date 2018-6-21 10:4:39
 */
 
 
@Service("articleFormServiceImpl")
public class ArticleFormServiceImpl extends BaseServiceImpl<ArticleForm, Long> implements ArticleFormService {
	@Resource(name = "articleFormDaoImpl")
	private ArticleFormDao articleFormDao;

	@Resource(name = "articleFormDaoImpl")
	public void setBaseDao(ArticleFormDao articleFormDao) {
		super.setBaseDao(articleFormDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(ArticleForm articleForm) {
		super.save(articleForm);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public ArticleForm update(ArticleForm articleForm) {
		return super.update(articleForm);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public ArticleForm update(ArticleForm articleForm, String... ignoreProperties) {
		return super.update(articleForm, ignoreProperties);
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
	public void delete(ArticleForm articleForm) {
		super.delete(articleForm);
	}

	public Page<ArticleForm> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return articleFormDao.findPage(beginDate,endDate,pageable);
	}
}