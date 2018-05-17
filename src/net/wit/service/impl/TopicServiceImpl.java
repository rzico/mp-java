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

import net.wit.dao.MemberDao;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.wit.dao.TopicDao;
import net.wit.entity.*;
import net.wit.service.TopicService;

/**
 * @ClassName: TopicDaoImpl
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */

@Service("topicServiceImpl")
public class TopicServiceImpl extends BaseServiceImpl<Topic, Long> implements TopicService {
	@Resource(name = "topicDaoImpl")
	private TopicDao topicDao;

	@Resource(name = "memberDaoImpl")
	private MemberDao memberDao;

	@Resource(name = "topicDaoImpl")
	public void setBaseDao(TopicDao topicDao) {
		super.setBaseDao(topicDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(Topic topic) {
		super.save(topic);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Topic update(Topic topic) {
		return super.update(topic);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Topic update(Topic topic, String... ignoreProperties) {
		return super.update(topic, ignoreProperties);
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
	public void delete(Topic topic) {
		super.delete(topic);
	}

	public Page<Topic> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return topicDao.findPage(beginDate,endDate,pageable);
	}
	public Topic find(Member member) {
		return topicDao.find(member);
	}

	public Topic findByUserName(String appid){
		return topicDao.findByUserName(appid);
	}

	public Topic create(Topic topic) {
		Member member = topic.getMember();
		topicDao.persist(topic);
		member.setTopic(topic);
		memberDao.merge(member);
		return topic;
	}

}