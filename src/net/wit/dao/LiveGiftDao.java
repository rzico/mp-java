package net.wit.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.LiveGift;


/**
 * @ClassName: LiveGiftDao
 * @author 降魔战队
 * @date 2018-4-5 18:22:28
 */
 

public interface LiveGiftDao extends BaseDao<LiveGift, Long> {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<LiveGift>
	 */
	Page<LiveGift> findPage(Date beginDate, Date endDate, Pageable pageable);
}