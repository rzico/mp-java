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

import net.wit.dao.MerchantDao;
import net.wit.entity.*;
import net.wit.service.MerchantService;

/**
 * @ClassName: MerchantDaoImpl
 * @author 降魔战队
 * @date 2018-1-10 16:2:40
 */
 
 
@Service("merchantServiceImpl")
public class MerchantServiceImpl extends BaseServiceImpl<Merchant, Long> implements MerchantService {
	@Resource(name = "merchantDaoImpl")
	private MerchantDao merchantDao;

	@Resource(name = "merchantDaoImpl")
	public void setBaseDao(MerchantDao merchantDao) {
		super.setBaseDao(merchantDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(Merchant merchant) {
		super.save(merchant);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Merchant update(Merchant merchant) {
		return super.update(merchant);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Merchant update(Merchant merchant, String... ignoreProperties) {
		return super.update(merchant, ignoreProperties);
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
	public void delete(Merchant merchant) {
		super.delete(merchant);
	}

	public Page<Merchant> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return merchantDao.findPage(beginDate,endDate,pageable);
	}
}