package net.wit.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;
import net.wit.*;
import net.wit.Filter.Operator;

import net.wit.dao.TopicDao;
import net.wit.plat.weixin.util.WeiXinUtils;
import net.wit.util.SettingUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.wit.dao.TopicCardDao;
import net.wit.entity.*;
import net.wit.service.TopicCardService;

/**
 * @ClassName: TopicCardDaoImpl
 * @author 降魔战队
 * @date 2017-11-9 12:58:55
 */
 
 
@Service("topicCardServiceImpl")
public class TopicCardServiceImpl extends BaseServiceImpl<TopicCard, Long> implements TopicCardService {
	@Resource(name = "topicCardDaoImpl")
	private TopicCardDao topicCardDao;

	@Resource(name = "topicDaoImpl")
	private TopicDao topicDao;

	@Resource(name = "topicCardDaoImpl")
	public void setBaseDao(TopicCardDao topicCardDao) {
		super.setBaseDao(topicCardDao);
	}

	private String getColor(TopicCard.Color color) {
		if (color.equals(TopicCard.Color.c1)) {
			return "Color010";
		} else
		if (color.equals(TopicCard.Color.c2)) {
			return "Color020";
		} else
		if (color.equals(TopicCard.Color.c3)) {
			return "Color030";
		} else
		if (color.equals(TopicCard.Color.c4)) {
			return "Color040";
		} else
		if (color.equals(TopicCard.Color.c5)) {
			return "Color050";
		} else
		if (color.equals(TopicCard.Color.c6)) {
			return "Color060";
		} else
		if (color.equals(TopicCard.Color.c7)) {
			return "Color070";
		} else
		if (color.equals(TopicCard.Color.c8)) {
			return "Color080";
		} else
		if (color.equals(TopicCard.Color.c9)) {
			return "Color090";
		} else {
			return "Color100";
		}

	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(TopicCard topicCard) {
		JSONObject data = WeiXinUtils.createMemberCard(
			    topicCard.getBackground(),
				topicCard.getPrerogative(),
				topicCard.getTopic().getName(),
				topicCard.getTopic().getMember().getLogo(),
				topicCard.getTitle(),
				topicCard.getDescription(),getColor(topicCard.getColor())
		);
		if (data.getString("errcode").equals("0")){
			String cardId = data.getString("card_id");
			topicCard.setStatus(TopicCard.Status.waiting);
			topicCard.setWeixinCardId(cardId);
			super.save(topicCard);
			Topic topic = topicCard.getTopic();
			topic.setTopicCard(topicCard);
			topicDao.merge(topic);
		}
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public TopicCard update(TopicCard topicCard) {
		JSONObject data = WeiXinUtils.updateMemberCard(
				topicCard.getWeixinCardId(),
				topicCard.getBackground(),
				topicCard.getPrerogative(),
				topicCard.getTopic().getName(),
				topicCard.getTopic().getMember().getLogo(),
				topicCard.getTitle(),
				topicCard.getDescription(),getColor(topicCard.getColor())
		);
		if (data.getString("errcode").equals("0")){
			return super.update(topicCard);
		}  else {
			return null;
		}
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public TopicCard update(TopicCard topicCard, String... ignoreProperties) {
		JSONObject data = WeiXinUtils.updateMemberCard(
				topicCard.getWeixinCardId(),
				topicCard.getBackground(),
				topicCard.getPrerogative(),
				topicCard.getTopic().getName(),
				topicCard.getTopic().getMember().getLogo(),
				topicCard.getTitle(),
				topicCard.getDescription(),getColor(topicCard.getColor())
		);
		if (data.getString("errcode").equals("0")){
			return super.update(topicCard, ignoreProperties);
		}  else {
			return null;
		}
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
	public void delete(TopicCard topicCard) {
		super.delete(topicCard);
	}

	public Page<TopicCard> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return topicCardDao.findPage(beginDate,endDate,pageable);
	}

	@Transactional
	public TopicCard create(Topic topic) {
       TopicCard topicCard = topic.getTopicCard();
       if (topicCard==null) {
       	  Setting setting = SettingUtils.get();
       	  topicCard = new TopicCard();
       	  topicCard.setStatus(TopicCard.Status.waiting);
       	  topicCard.setColor(TopicCard.Color.c9);
       	  topicCard.setTopic(topic);
       	  topicCard.setTitle(topic.getName());
       	  topicCard.setIncrement(0L);
		  topicCard.setDescription(
		  		"储值卡余额只能使用所属商家，解悉权归发行商家所有；平台作为工具提供方不承担相关法律责任。"
		  );
		  topicCard.setPrerogative("持卡会员尊受商家提供的优惠折扣，分享平台商品可获取相应奖励金。");
		  this.save(topicCard);
	   } else {
       	  this.update(topicCard);
	   }
       return topicCard;
	}


	public TopicCard find(String cardId) {
	  return topicCardDao.find(cardId);
	}


}