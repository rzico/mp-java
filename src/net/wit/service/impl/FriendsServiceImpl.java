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

import net.wit.dao.FriendsDao;
import net.wit.entity.*;
import net.wit.service.FriendsService;

/**
 * @ClassName: FriendsDaoImpl
 * @author 降魔战队
 * @date 2017-9-14 19:42:8
 */
 
 
@Service("friendsServiceImpl")
public class FriendsServiceImpl extends BaseServiceImpl<Friends, Long> implements FriendsService {
	@Resource(name = "friendsDaoImpl")
	private FriendsDao friendsDao;

	@Resource(name = "friendsDaoImpl")
	public void setBaseDao(FriendsDao friendsDao) {
		super.setBaseDao(friendsDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(Friends friends) {
		super.save(friends);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Friends update(Friends friends) {
		return super.update(friends);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Friends update(Friends friends, String... ignoreProperties) {
		return super.update(friends, ignoreProperties);
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
	public void delete(Friends friends) {
		super.delete(friends);
	}

	public Page<Friends> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return friendsDao.findPage(beginDate,endDate,pageable);
	}
	public Friends find(Member member,Member friend) {
		return friendsDao.find(member,friend);
	}
}