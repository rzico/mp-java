package net.wit.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import net.wit.Page;
import net.wit.Pageable;

import net.wit.entity.GoldProduct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.wit.dao.GoldProductDao;
import net.wit.service.GoldProductService;

/**
 * @ClassName: GoldProductDaoImpl
 * @author 降魔战队
 * @date 2018-3-25 14:59:5
 */
 
 
@Service("goldProductServiceImpl")
public class GoldProductServiceImpl extends BaseServiceImpl<GoldProduct, Long> implements GoldProductService {
	@Resource(name = "goldProductDaoImpl")
	private GoldProductDao goldProductDao;

	@Resource(name = "goldProductDaoImpl")
	public void setBaseDao(GoldProductDao goldProductDao) {
		super.setBaseDao(goldProductDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(GoldProduct goldProduct) {
		super.save(goldProduct);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public GoldProduct update(GoldProduct goldProduct) {
		return super.update(goldProduct);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public GoldProduct update(GoldProduct goldProduct, String... ignoreProperties) {
		return super.update(goldProduct, ignoreProperties);
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
	public void delete(GoldProduct goldProduct) {
		super.delete(goldProduct);
	}

	public Page<GoldProduct> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return goldProductDao.findPage(beginDate,endDate,pageable);
	}
}