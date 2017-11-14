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

import net.wit.dao.AdminDao;
import net.wit.dao.ShopDao;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.wit.dao.EnterpriseDao;
import net.wit.entity.*;
import net.wit.service.EnterpriseService;

/**
 * @ClassName: EnterpriseDaoImpl
 * @author 降魔战队
 * @date 2017-9-14 19:42:8
 */
 
 
@Service("enterpriseServiceImpl")
public class EnterpriseServiceImpl extends BaseServiceImpl<Enterprise, Long> implements EnterpriseService {
	@Resource(name = "enterpriseDaoImpl")
	private EnterpriseDao enterpriseDao;
	@Resource(name = "adminDaoImpl")
	private AdminDao adminDao;
	@Resource(name = "shopDaoImpl")
	private ShopDao shopDao;

	@Resource(name = "enterpriseDaoImpl")
	public void setBaseDao(EnterpriseDao enterpriseDao) {
		super.setBaseDao(enterpriseDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(Enterprise enterprise) {
		super.save(enterprise);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Enterprise update(Enterprise enterprise) {
		return super.update(enterprise);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Enterprise update(Enterprise enterprise, String... ignoreProperties) {
		return super.update(enterprise, ignoreProperties);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void delete(Long id) {
		Enterprise enterprise = enterpriseDao.find(id);
		enterprise.setDeleted(true);
		super.update(enterprise);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void delete(Long... ids) {
		for (Long id:ids) {
			Enterprise enterprise = enterpriseDao.find(id);
			enterprise.setDeleted(true);
			super.update(enterprise);
		}
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void delete(Enterprise enterprise) {
		enterprise.setDeleted(true);
		super.update(enterprise);
	}

	public Page<Enterprise> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return enterpriseDao.findPage(beginDate,endDate,pageable);
	}

	public Enterprise create(Topic topic) {
        Member member = topic.getMember();
		Enterprise enterprise = enterpriseDao.find(member);
		if (enterprise==null) {
			enterprise.setName(topic.getName());
			enterprise.setDeleted(false);
			enterprise.setBrokerage(new BigDecimal("0.40"));
			enterprise.setLogo(topic.getLogo());
			enterprise.setMember(member);
			enterprise.setType(Enterprise.Type.shop);
			enterpriseDao.persist(enterprise);
			Admin admin = new Admin();
			admin.setUsername(member.getMobile());
			admin.setEnterprise(enterprise);
			admin.setIsLocked(false);
			admin.setIsEnabled(true);
			admin.setMember(member);
			if (member.getGender()!=null) {
				admin.setGender(Admin.Gender.valueOf(member.getGender().name()));
			}
			adminDao.persist(admin);
		}
		return enterprise;
	}

}