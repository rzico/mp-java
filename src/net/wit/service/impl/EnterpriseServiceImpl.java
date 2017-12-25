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
import net.wit.dao.RoleDao;
import net.wit.dao.ShopDao;
import net.wit.util.MD5Utils;
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
	@Resource(name = "roleDaoImpl")
	private RoleDao roleDao;

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

	@Transactional
	public Enterprise create(Topic topic) {
        Member member = topic.getMember();
		Enterprise enterprise = enterpriseDao.find(member);
		if (enterprise==null) {
			enterprise = new Enterprise();
			enterprise.setName(topic.getName());
			enterprise.setDeleted(false);
			enterprise.setBrokerage(new BigDecimal("0.45"));
			enterprise.setLogo(topic.getLogo());
			enterprise.setMember(member);
			enterprise.setType(Enterprise.Type.shop);
			enterpriseDao.persist(enterprise);
		}
		Admin admin = adminDao.findByMember(member);
		if (admin == null) {
			admin = new Admin();
			admin.setUsername(member.userId());
			admin.setName(member.getName());
//			admin.setEmail(member.getEmail());
			admin.setEnterprise(enterprise);
			admin.setIsLocked(false);
			admin.setIsEnabled(true);
			admin.setLoginFailureCount(0);
			admin.setMember(member);
			admin.setPassword(member.getPassword());
			if (admin.getPassword()==null) {
				String m = admin.getUsername();
				admin.setPassword(MD5Utils.getMD5Str(m));
			}
			if (member.getGender()!=null) {
				admin.setGender(Admin.Gender.valueOf(member.getGender().name()));
			}
			List<Role> roles = admin.getRoles();
			if (roles!=null) {
				roles = new ArrayList<Role>();
			}
			roles.add(roleDao.find(1L));
			admin.setRoles(roles);
			adminDao.persist(admin);
		} else {
			admin.setEnterprise(enterprise);
			enterpriseDao.persist(enterprise);
		}
		return enterprise;
	}

	@Transactional
	public Admin addAdmin(Enterprise enterprise,Member member) {
		Admin admin = adminDao.findByMember(member);
		if (admin==null) {
			admin = new Admin();
			admin.setUsername(member.userId());
			admin.setName(member.getName());
//			admin.setEmail(member.getEmail());
			admin.setEnterprise(enterprise);
			admin.setIsLocked(false);
			admin.setIsEnabled(true);
			admin.setLoginFailureCount(0);
			admin.setMember(member);
			admin.setPassword(member.getPassword());
			if (admin.getPassword()==null) {
				String m = admin.getUsername();
				admin.setPassword(MD5Utils.getMD5Str(m));
			}
			if (member.getGender()!=null) {
				admin.setGender(Admin.Gender.valueOf(member.getGender().name()));
			}
			List<Role> roles = admin.getRoles();
			if (roles!=null) {
				roles = new ArrayList<Role>();
			}
			roles.add(roleDao.find(1L));
			admin.setRoles(roles);
			adminDao.persist(admin);
		} else {
			if (admin.getEnterprise()!=null) {
				return null;
			}
			admin.setEnterprise(enterprise);
			admin.setShop(null);
			adminDao.merge(admin);
		}
		return admin;
	}

}