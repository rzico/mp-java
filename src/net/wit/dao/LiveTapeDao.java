package net.wit.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.LiveTape;


/**
 * @ClassName: LiveTapeDao
 * @author 降魔战队
 * @date 2018-4-5 18:22:28
 */
 

public interface LiveTapeDao extends BaseDao<LiveTape, Long> {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<LiveTape>
	 */
	Page<LiveTape> findPage(Date beginDate, Date endDate, Pageable pageable);
}