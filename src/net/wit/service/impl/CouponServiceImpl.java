package net.wit.service.impl;

import java.math.BigDecimal;
import java.util.*;

import javax.annotation.Resource;

import net.wit.Filter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.Principal;
import net.wit.Filter.Operator;

import org.apache.commons.lang.time.DateUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.wit.dao.CouponDao;
import net.wit.entity.*;
import net.wit.service.CouponService;

/**
 * @ClassName: CouponDaoImpl
 * @author 降魔战队
 * @date 2017-9-14 19:42:8
 */
 
 
@Service("couponServiceImpl")
public class CouponServiceImpl extends BaseServiceImpl<Coupon, Long> implements CouponService {
	@Resource(name = "couponDaoImpl")
	private CouponDao couponDao;

	@Resource(name = "couponDaoImpl")
	public void setBaseDao(CouponDao couponDao) {
		super.setBaseDao(couponDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(Coupon coupon) {
		super.save(coupon);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Coupon update(Coupon coupon) {
		return super.update(coupon);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Coupon update(Coupon coupon, String... ignoreProperties) {
		return super.update(coupon, ignoreProperties);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void delete(Long id) {
		Coupon coupon = couponDao.find(id);
		coupon.setDeleted(true);
		super.update(coupon);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void delete(Long... ids) {
		for (Long id:ids) {
			Coupon coupon = couponDao.find(id);
			coupon.setDeleted(true);
			super.update(coupon);
		}
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void delete(Coupon coupon) {
		coupon.setDeleted(true);
		super.update(coupon);
	}

	public Page<Coupon> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return couponDao.findPage(beginDate,endDate,pageable);
	}

	public Coupon create(Product product) {
		List<Filter> filters = new ArrayList<>();
		filters.add(new Filter("distributor",Operator.eq,product.getMember()));
		filters.add(new Filter("goods",Operator.eq,product.getGoods()));

		List<Coupon> data = couponDao.findList(null,null,filters,null);
		if (data.size()>0) {
			return data.get(0);
		} else {
			Coupon coupon = new Coupon();
			coupon.setActivity("{type:3,min:0,amount:0}");
			coupon.setGoods(product.getGoods());
			coupon.setAmount(product.getPrice());
			Date b = DateUtils.truncate(new Date(), Calendar.DATE);
			Date e = DateUtils.addYears(new Date(),100);
			coupon.setBeginDate(b);
			coupon.setEndDate(e);
			coupon.setColor(Coupon.Color.c1);
			coupon.setDeleted(false);
			coupon.setDistributor(product.getMember());
			coupon.setIntroduction("提货券,请勿删除");
			coupon.setMinimumPrice(BigDecimal.ZERO);
			coupon.setType(Coupon.Type.exchange);
			coupon.setName(product.getName()+"-提货券");
			coupon.setScope(Coupon.Scope.all);
			coupon.setStock(9999999L);
			couponDao.persist(coupon);
			return coupon;
		}
	}

}