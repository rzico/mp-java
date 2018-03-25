package net.wit.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import net.wit.Page;
import net.wit.Pageable;

import net.wit.entity.GoldExchange;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.wit.dao.GoldExchangeDao;
import net.wit.service.GoldExchangeService;

/**
 * @ClassName: goldExchangeDaoImpl
 * @author 降魔战队
 * @date 2018-3-25 14:59:5
 */
 
 
@Service("goldExchangeServiceImpl")
public class GoldExchangeServiceImpl extends BaseServiceImpl<GoldExchange, Long> implements GoldExchangeService {
	@Resource(name = "goldExchangeDaoImpl")
	private GoldExchangeDao goldExchangeDao;

	@Resource(name = "goldExchangeDaoImpl")
	public void setBaseDao(GoldExchangeDao goldExchangeDao) {
		super.setBaseDao(goldExchangeDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(GoldExchange goldExchange) {
		super.save(goldExchange);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public GoldExchange update(GoldExchange goldExchange) {
		return super.update(goldExchange);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public GoldExchange update(GoldExchange goldExchange, String... ignoreProperties) {
		return super.update(goldExchange, ignoreProperties);
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
	public void delete(GoldExchange goldExchange) {
		super.delete(goldExchange);
	}

	public Page<GoldExchange> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return goldExchangeDao.findPage(beginDate,endDate,pageable);
	}
}