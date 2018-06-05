package net.wit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Member;
import net.wit.entity.Topic;

/**
 * @ClassName: TopicService
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */

public interface TopicService extends BaseService<Topic, Long> {
	Topic find(Member member);
	Page<Topic> findPage(Date beginDate,Date endDate, Pageable pageable);
	public Topic create(Topic topic);
	public Topic autoCreate(Member member);

}