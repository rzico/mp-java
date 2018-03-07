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

import net.wit.dao.FriendsDao;
import net.wit.dao.MemberDao;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.wit.dao.MemberFollowDao;
import net.wit.entity.*;
import net.wit.service.MemberFollowService;

/**
 * @ClassName: MemberFollowDaoImpl
 * @author 降魔战队
 * @date 2017-9-14 19:42:8
 */
 
 
@Service("memberFollowServiceImpl")
public class MemberFollowServiceImpl extends BaseServiceImpl<MemberFollow, Long> implements MemberFollowService {
	@Resource(name = "memberFollowDaoImpl")
	private MemberFollowDao memberFollowDao;
	@Resource(name = "memberDaoImpl")
	private MemberDao memberDao;

	@Resource(name = "memberFollowDaoImpl")
	public void setBaseDao(MemberFollowDao memberFollowDao) {
		super.setBaseDao(memberFollowDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(MemberFollow memberFollow) {
		super.save(memberFollow);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public MemberFollow update(MemberFollow memberFollow) {
		return super.update(memberFollow);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public MemberFollow update(MemberFollow memberFollow, String... ignoreProperties) {
		return super.update(memberFollow, ignoreProperties);
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
	public void delete(MemberFollow memberFollow) {
		super.delete(memberFollow);
	}

	public Page<MemberFollow> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return memberFollowDao.findPage(beginDate,endDate,pageable);
	}
	public MemberFollow find(Member member, Member follow) {
		return memberFollowDao.find(member,follow);
	}

}