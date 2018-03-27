package net.wit.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Feedback;


/**
 * @ClassName: FeedbackDao
 * @author 降魔战队
 * @date 2018-3-26 15:13:1
 */
 

public interface FeedbackDao extends BaseDao<Feedback, Long> {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<Feedback>
	 */
	Page<Feedback> findPage(Date beginDate, Date endDate, Pageable pageable);
}