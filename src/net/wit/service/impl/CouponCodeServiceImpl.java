package net.wit.service.impl;

import java.math.BigDecimal;
import java.util.*;

import javax.annotation.Resource;
import javax.persistence.LockModeType;

import net.wit.Filter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.Principal;
import net.wit.Filter.Operator;

import net.wit.dao.CouponDao;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.wit.dao.CouponCodeDao;
import net.wit.entity.*;
import net.wit.service.CouponCodeService;

/**
 * @ClassName: CouponCodeDaoImpl
 * @author 降魔战队
 * @date 2017-9-14 19:42:8
 */
 
 
@Service("couponCodeServiceImpl")
public class CouponCodeServiceImpl extends BaseServiceImpl<CouponCode, Long> implements CouponCodeService {
	@Resource(name = "couponCodeDaoImpl")
	private CouponCodeDao couponCodeDao;

	@Resource(name = "couponDaoImpl")
	private CouponDao couponDao;

	@Resource(name = "couponCodeDaoImpl")
	public void setBaseDao(CouponCodeDao couponCodeDao) {
		super.setBaseDao(couponCodeDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(CouponCode couponCode) {
		super.save(couponCode);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public CouponCode update(CouponCode couponCode) {
		return super.update(couponCode);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public CouponCode update(CouponCode couponCode, String... ignoreProperties) {
		return super.update(couponCode, ignoreProperties);
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
	public void delete(CouponCode couponCode) {
		super.delete(couponCode);
	}

	@Transactional(readOnly = true)
	public CouponCode findByCode(String code) {
		return couponCodeDao.findByCode(code);
	}

	public Page<CouponCode> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return couponCodeDao.findPage(beginDate,endDate,pageable);
	}

	public CouponCode build(Coupon coupon, Member member) throws Exception {
		couponDao.lock(coupon, LockModeType.PESSIMISTIC_WRITE);
		if (coupon.getStock().equals(0L)) {
			 throw new RuntimeException("已抢完,下次再来");
		}
		Boolean has = false;
	    for (CouponCode couponCode:member.getCouponCodes()) {
	    	if (couponCode.getCoupon().equals(coupon)) {
	    		has = true;
	    		break;
			}
		}
		if (has) {
			throw new RuntimeException("你已经领取,不能领了");
		}
		CouponCode couponCode = new CouponCode();
		String uuid = UUID.randomUUID().toString().toUpperCase();
		couponCode.setCode(uuid.substring(0, 8) + uuid.substring(9, 13) + uuid.substring(14, 18) + uuid.substring(19, 23) + uuid.substring(24));
		couponCode.setIsUsed(false);
		couponCode.setCoupon(coupon);
		couponCode.setMember(member);
		couponCode.setStock(1L);
		couponCodeDao.persist(couponCode);
		coupon.setStock(coupon.getStock()-1);
		couponDao.merge(coupon);
		return couponCode;
	}

	public CouponCode build(Coupon coupon, Member member,Long amount) throws Exception {
		couponDao.lock(coupon, LockModeType.PESSIMISTIC_WRITE);
//		if (coupon.getStock().equals(0L)) {
//			throw new RuntimeException("已抢完,下次再来");
//		}
//		if (coupon.getStock().compareTo(amount)<0) {
//			throw new RuntimeException("库存不足，下次再来");
//		}
		CouponCode couponCode = null;
		for (CouponCode c:member.getCouponCodes()) {
			if (c.getCoupon().equals(coupon)) {
				couponCode = c;
				break;
			}
		}
		if (couponCode==null) {
			couponCode = new CouponCode();
			String uuid = UUID.randomUUID().toString().toUpperCase();
			couponCode.setCode(uuid.substring(0, 8) + uuid.substring(9, 13) + uuid.substring(14, 18) + uuid.substring(19, 23) + uuid.substring(24));
			couponCode.setIsUsed(false);
			couponCode.setCoupon(coupon);
			couponCode.setMember(member);
			couponCode.setStock(amount);
			couponCodeDao.persist(couponCode);
		} else {
			couponCode.setStock(couponCode.getStock()+amount);
			couponCodeDao.merge(couponCode);
		}
//		coupon.setStock(coupon.getStock()-amount);
//		couponDao.merge(coupon);
		return couponCode;
	}

}