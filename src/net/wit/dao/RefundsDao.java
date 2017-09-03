package net.wit.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Refunds;


/**
 * @ClassName: RefundsDao
 * @author 降魔战队
 * @date 2017-9-3 21:55:0
 */
 

public interface RefundsDao extends BaseDao<Refunds, Long> {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<Refunds>
	 */
	Page<Refunds> findPage(Date beginDate, Date endDate, Pageable pageable);
}