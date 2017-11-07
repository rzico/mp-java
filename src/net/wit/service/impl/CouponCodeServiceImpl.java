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
}