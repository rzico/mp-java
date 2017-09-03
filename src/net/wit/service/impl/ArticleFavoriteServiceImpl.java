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

import net.wit.dao.ArticleFavoriteDao;
import net.wit.entity.*;
import net.wit.service.ArticleFavoriteService;

/**
 * @ClassName: ArticleFavoriteDaoImpl
 * @author 降魔战队
 * @date 2017-9-3 21:54:58
 */
 
 
@Service("articleFavoriteServiceImpl")
public class ArticleFavoriteServiceImpl extends BaseServiceImpl<ArticleFavorite, Long> implements ArticleFavoriteService {
	@Resource(name = "articleFavoriteDaoImpl")
	private ArticleFavoriteDao articleFavoriteDao;

	@Resource(name = "articleFavoriteDaoImpl")
	public void setBaseDao(ArticleFavoriteDao articleFavoriteDao) {
		super.setBaseDao(articleFavoriteDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(ArticleFavorite articleFavorite) {
		super.save(articleFavorite);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public ArticleFavorite update(ArticleFavorite articleFavorite) {
		return super.update(articleFavorite);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public ArticleFavorite update(ArticleFavorite articleFavorite, String... ignoreProperties) {
		return super.update(articleFavorite, ignoreProperties);
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
	public void delete(ArticleFavorite articleFavorite) {
		super.delete(articleFavorite);
	}

	public Page<ArticleFavorite> findPage(Date beginDate,Date endDate, Pageable pageable) {
	  return articleFavoriteDao.findPage(beginDate,endDate,pageable);
	}
}