package net.wit.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.TopicCard;


/**
 * @ClassName: TopicCardDao
 * @author 降魔战队
 * @date 2017-11-9 12:58:51
 */
 

public interface TopicCardDao extends BaseDao<TopicCard, Long> {

	TopicCard find(String cardId);

	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<TopicCard>
	 */
	Page<TopicCard> findPage(Date beginDate, Date endDate, Pageable pageable);
}