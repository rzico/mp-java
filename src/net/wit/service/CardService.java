package net.wit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.*;

/**
 * @ClassName: CardService
 * @author 降魔战队
 * @date 2017-11-4 18:12:27
 */

public interface CardService extends BaseService<Card, Long> {
	Page<Card> findPage(Date beginDate,Date endDate, Pageable pageable);

	public Card activate(Card card,Member member);

	public Card find(String code);

	public Card find(Member member,Member owner);

	public Card create(TopicCard topicCard,Shop shop, String code,Member member);

	//分销关系，创建并激活会员卡
	public Card createAndActivate(Member member,Member owner,Member promoter);

	//支付插件专用方法
	public void payment(Card card,Payment payment) throws Exception;
	//支付插件专用方法
	public void refunds(Card card,Refunds refunds) throws Exception;
}