package net.wit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Topic;
import net.wit.entity.TopicCard;

/**
 * @ClassName: TopicCardService
 * @author 降魔战队
 * @date 2017-11-9 12:58:55
 */

public interface TopicCardService extends BaseService<TopicCard, Long> {
	Page<TopicCard> findPage(Date beginDate, Date endDate, Pageable pageable);
	public TopicCard create(Topic topic);
}