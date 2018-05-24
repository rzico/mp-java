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

import net.wit.service.SnService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.wit.dao.ShippingDao;
import net.wit.entity.*;
import net.wit.service.ShippingService;

/**
 * @ClassName: ShippingDaoImpl
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
 
@Service("shippingServiceImpl")
public class ShippingServiceImpl extends BaseServiceImpl<Shipping, Long> implements ShippingService {

	@Resource(name = "shippingDaoImpl")
	private ShippingDao shippingDao;

	@Resource(name = "snServiceImpl")
	private SnService snService;

	@Resource(name = "shippingDaoImpl")
	public void setBaseDao(ShippingDao shippingDao) {
		super.setBaseDao(shippingDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(Shipping shipping) {
		super.save(shipping);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Shipping update(Shipping shipping) {
		return super.update(shipping);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Shipping update(Shipping shipping, String... ignoreProperties) {
		return super.update(shipping, ignoreProperties);
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
	public void delete(Shipping shipping) {
		super.delete(shipping);
	}

	public Page<Shipping> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return shippingDao.findPage(beginDate,endDate,pageable);
	}


	/**
	 * 根据订单编号查找订单
	 *
	 * @param sn
	 *            订单编号(忽略大小写)
	 * @return 订单，若不存在则返回null
	 */
	public Shipping findBySn(String sn) {
		return shippingDao.findBySn(sn);
	}

	public Shipping create(Order order) {
		
		Shipping shipping = new Shipping();
		shipping.setAddress(order.getAddress());
		shipping.setAreaName(order.getAreaName());
		shipping.setConsignee(order.getConsignee());

		shipping.setMember(order.getMember());
		shipping.setOrder(order);
		shipping.setOrderStatus(Shipping.OrderStatus.unconfirmed);
		shipping.setShippingStatus(Shipping.ShippingStatus.unconfirmed);
		shipping.setPhone(order.getPhone());
		shipping.setSeller(order.getMember());
		shipping.setZipCode(order.getZipCode());
		shipping.setSn(snService.generate(Sn.Type.shipping));

		return shipping;

	}
}