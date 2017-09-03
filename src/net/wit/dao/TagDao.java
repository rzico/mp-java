package net.wit.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Tag;


/**
 * @ClassName: TagDao
 * @author 降魔战队
 * @date 2017-9-3 21:55:0
 */
 

public interface TagDao extends BaseDao<Tag, Long> {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<Tag>
	 */
	Page<Tag> findPage(Date beginDate, Date endDate, Pageable pageable);
}