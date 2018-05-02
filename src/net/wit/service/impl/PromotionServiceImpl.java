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

import net.wit.dao.PromotionDao;
import net.wit.entity.*;
import net.wit.service.PromotionService;

/**
 * @ClassName: PromotionDaoImpl
 * @author 降魔战队
 * @date 2018-4-28 22:28:39
 */
 
 
@Service("promotionServiceImpl")
public class PromotionServiceImpl extends BaseServiceImpl<Promotion, Long> implements PromotionService {
	@Resource(name = "promotionDaoImpl")
	private PromotionDao promotionDao;

	@Resource(name = "promotionDaoImpl")
	public void setBaseDao(PromotionDao promotionDao) {
		super.setBaseDao(promotionDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(Promotion promotion) {
		super.save(promotion);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Promotion update(Promotion promotion) {
		return super.update(promotion);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Promotion update(Promotion promotion, String... ignoreProperties) {
		return super.update(promotion, ignoreProperties);
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
	public void delete(Promotion promotion) {
		super.delete(promotion);
	}

	public Page<Promotion> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return promotionDao.findPage(beginDate,endDate,pageable);
	}
}