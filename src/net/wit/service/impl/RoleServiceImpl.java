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

import net.wit.dao.RoleDao;
import net.wit.entity.*;
import net.wit.service.RoleService;

/**
 * @ClassName: RoleDaoImpl
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
 
@Service("roleServiceImpl")
public class RoleServiceImpl extends BaseServiceImpl<Role, Long> implements RoleService {
	@Resource(name = "roleDaoImpl")
	private RoleDao roleDao;

	@Resource(name = "roleDaoImpl")
	public void setBaseDao(RoleDao roleDao) {
		super.setBaseDao(roleDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(Role role) {
		super.save(role);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Role update(Role role) {
		return super.update(role);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Role update(Role role, String... ignoreProperties) {
		return super.update(role, ignoreProperties);
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
	public void delete(Role role) {
		super.delete(role);
	}

	public Page<Role> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return roleDao.findPage(beginDate,endDate,pageable);
	}
}