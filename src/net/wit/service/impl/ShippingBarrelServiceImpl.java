package net.wit.service.impl;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.ShippingBarrelDao;
import net.wit.dao.ShippingBarrelDao;
import net.wit.entity.Enterprise;
import net.wit.entity.Member;
import net.wit.entity.ShippingBarrel;
import net.wit.entity.ShippingBarrel;
import net.wit.entity.summary.BarrelSummary;
import net.wit.entity.summary.PaymentSummary;
import net.wit.service.ShippingBarrelService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: ShippingBarrelServiceImpl
 * @author 降魔战队
 * @date 2018-5-28 15:8:46
 */
 
 
@Service("shippingBarrelServiceImpl")
public class ShippingBarrelServiceImpl extends BaseServiceImpl<ShippingBarrel, Long> implements ShippingBarrelService {
	@Resource(name = "shippingBarrelDaoImpl")
	private ShippingBarrelDao shippingBarrelDao;

	@Resource(name = "shippingBarrelDaoImpl")
	public void setBaseDao(ShippingBarrelDao shippingBarrelDao) {
		super.setBaseDao(shippingBarrelDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(ShippingBarrel shippingBarrel) {
		super.save(shippingBarrel);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public ShippingBarrel update(ShippingBarrel shippingBarrel) {
		return super.update(shippingBarrel);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public ShippingBarrel update(ShippingBarrel shippingBarrel, String... ignoreProperties) {
		return super.update(shippingBarrel, ignoreProperties);
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
	public void delete(ShippingBarrel shippingBarrel) {
		super.delete(shippingBarrel);
	}

	public Page<ShippingBarrel> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return shippingBarrelDao.findPage(beginDate,endDate,pageable);
	}



	public List<BarrelSummary> summary(Enterprise enterprise, Date beginDate, Date endDate, Pageable pageable) {
		return shippingBarrelDao.summary(enterprise,beginDate,endDate,pageable);
	}

	public List<BarrelSummary> summary_barrel(Enterprise enterprise, Date beginDate, Date endDate, Pageable pageable) {
		return shippingBarrelDao.summary_barrel(enterprise,beginDate,endDate,pageable);
	}



}