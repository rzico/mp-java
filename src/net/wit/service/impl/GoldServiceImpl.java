package net.wit.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import net.wit.Page;
import net.wit.Pageable;

import net.wit.entity.Gold;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.wit.dao.GoldDao;
import net.wit.service.GoldService;

/**
 * @ClassName: GmGoldDaoImpl
 * @author 降魔战队
 * @date 2018-3-25 14:59:5
 */
 
 
@Service("goldServiceImpl")
public class GoldServiceImpl extends BaseServiceImpl<Gold, Long> implements GoldService {
	@Resource(name = "goldDaoImpl")
	private GoldDao goldDao;

	@Resource(name = "goldDaoImpl")
	public void setBaseDao(GoldDao goldDao) {
		super.setBaseDao(goldDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(Gold gold) {
		super.save(gold);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Gold update(Gold gold) {
		return super.update(gold);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Gold update(Gold gold, String... ignoreProperties) {
		return super.update(gold, ignoreProperties);
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
	public void delete(Gold gold) {
		super.delete(gold);
	}

	public Page<Gold> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return goldDao.findPage(beginDate,endDate,pageable);
	}
}