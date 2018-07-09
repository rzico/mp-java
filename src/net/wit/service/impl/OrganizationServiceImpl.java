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

import net.wit.dao.OrganizationDao;
import net.wit.entity.*;
import net.wit.service.OrganizationService;

/**
 * @ClassName: OrganizationDaoImpl
 * @author 降魔战队
 * @date 2018-2-28 16:42:7
 */
 
 
@Service("organizationServiceImpl")
public class OrganizationServiceImpl extends BaseServiceImpl<Organization, Long> implements OrganizationService {
	@Resource(name = "organizationDaoImpl")
	private OrganizationDao organizationDao;

	@Resource(name = "organizationDaoImpl")
	public void setBaseDao(OrganizationDao organizationDao) {
		super.setBaseDao(organizationDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(Organization organization) {
		super.save(organization);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Organization update(Organization organization) {
		return super.update(organization);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Organization update(Organization organization, String... ignoreProperties) {
		return super.update(organization, ignoreProperties);
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
	public void delete(Organization organization) {
		super.delete(organization);
	}

	public Page<Organization> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return organizationDao.findPage(beginDate,endDate,pageable);
	}
}