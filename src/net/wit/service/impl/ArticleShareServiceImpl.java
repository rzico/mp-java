package net.wit.service.impl;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.ArticleShareDao;
import net.wit.entity.ArticleShare;
import net.wit.service.ArticleShareService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @ClassName: ArticleLaudDaoImpl
 * @author 降魔战队
 * @date 2017-9-14 19:42:7
 */
 
 
@Service("articleShareServiceImpl")
public class ArticleShareServiceImpl extends BaseServiceImpl<ArticleShare, Long> implements ArticleShareService {
	@Resource(name = "articleShareDaoImpl")
	private ArticleShareDao articleShareDao;

	@Resource(name = "articleShareDaoImpl")
	public void setBaseDao(ArticleShareDao articleShareDao) {
		super.setBaseDao(articleShareDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(ArticleShare articleShare) {
		super.save(articleShare);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public ArticleShare update(ArticleShare articleShare) {
		return super.update(articleShare);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public ArticleShare update(ArticleShare articleShare, String... ignoreProperties) {
		return super.update(articleShare, ignoreProperties);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void delete(Long id) {
		ArticleShare articleShare = articleShareDao.find(id);
		articleShare.setIsShow(true);
		super.update(articleShare);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void delete(Long... ids) {
		for (Long id:ids) {
			ArticleShare articleShare = articleShareDao.find(id);
			articleShare.setIsShow(true);
			super.update(articleShare);
		}
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void delete(ArticleShare articleShare) {
		articleShare.setIsShow(true);
		super.update(articleShare);
	}

	public Page<ArticleShare> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return articleShareDao.findPage(beginDate,endDate,pageable);
	}
}