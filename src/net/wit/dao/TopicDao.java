package net.wit.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Member;
import net.wit.entity.Topic;


/**
 * @ClassName: TopicDao
 * @author 降魔战队
 * @date 2017-9-14 19:42:6
 */
 

public interface TopicDao extends BaseDao<Topic, Long> {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<Topic>
	 */
	Page<Topic> findPage(Date beginDate,Date endDate, Pageable pageable);
	Topic find(Member member);
}