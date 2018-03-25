package net.wit.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import net.wit.Page;
import net.wit.Pageable;

import net.wit.entity.GoldBuy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.wit.dao.GoldBuyDao;
import net.wit.service.GoldBuyService;

/**
 * @ClassName: GoldBuyDaoImpl
 * @author 降魔战队
 * @date 2018-3-25 14:59:5
 */
 
 
@Service("goldBuyServiceImpl")
public class GoldBuyServiceImpl extends BaseServiceImpl<GoldBuy, Long> implements GoldBuyService {
	@Resource(name = "goldBuyDaoImpl")
	private GoldBuyDao goldBuyDao;

	@Resource(name = "goldBuyDaoImpl")
	public void setBaseDao(GoldBuyDao goldBuyDao) {
		super.setBaseDao(goldBuyDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(GoldBuy goldBuy) {
		super.save(goldBuy);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public GoldBuy update(GoldBuy goldBuy) {
		return super.update(goldBuy);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public GoldBuy update(GoldBuy goldBuy, String... ignoreProperties) {
		return super.update(goldBuy, ignoreProperties);
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
	public void delete(GoldBuy goldBuy) {
		super.delete(goldBuy);
	}

	public Page<GoldBuy> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return goldBuyDao.findPage(beginDate,endDate,pageable);
	}
}