package net.wit.service.impl;

import java.math.BigDecimal;
import java.util.*;

import javax.annotation.Resource;

import net.wit.*;
import net.wit.Filter.Operator;

import net.wit.dao.MemberDao;
import net.wit.service.TemplateService;
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

	@Resource(name = "templateServiceImpl")
	private TemplateService templateService;

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

	public Topic create(Topic topic) {
		Member member = topic.getMember();
		topicDao.persist(topic);
		member.setTopic(topic);
		memberDao.merge(member);
		return topic;
	}

	public Topic autoCreate(Member member) {
		Topic topic =  member.getTopic();
		if (topic==null) {
			topic = new Topic();
			topic.setName(member.displayName());
			topic.setBrokerage(new BigDecimal("0.6"));
			topic.setPaybill(new BigDecimal("0.6"));
			topic.setStatus(Topic.Status.waiting);
			topic.setHits(0L);
			topic.setMember(member);
			topic.setFee(new BigDecimal("588"));
			topic.setLogo(member.getLogo());
			topic.setType(Topic.Type.personal);
			topic.setRanking(0L);
			TopicConfig config = topic.getConfig();
			if (config==null) {
				config = new TopicConfig();
				config.setUseCard(false);
				config.setUseCashier(false);
				config.setUseCoupon(false);
				config.setPromoterType(TopicConfig.PromoterType.any);
				config.setPattern(TopicConfig.Pattern.pattern1);
				config.setAmount(BigDecimal.ZERO);
				config.setTokenExpire(new Date());
				config.setEstate(TopicConfig.Estate.UNAUTHORIZED);
			}
			topic.setConfig(config);
			Calendar calendar   =   new GregorianCalendar();
			calendar.setTime(new Date());
			calendar.add(calendar.MONTH, 1);
			topic.setExpire(calendar.getTime());
			topic.setTemplate(templateService.findDefault(Template.Type.topic));
		    return create(topic);
		} else {
			return member.getTopic();
		}

	}

}