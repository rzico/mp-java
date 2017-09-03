package net.wit.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Message;


/**
 * @ClassName: MessageDao
 * @author 降魔战队
 * @date 2017-9-3 21:54:59
 */
 

public interface MessageDao extends BaseDao<Message, Long> {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<Message>
	 */
	Page<Message> findPage(Date beginDate, Date endDate, Pageable pageable);
}