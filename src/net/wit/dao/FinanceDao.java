package net.wit.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Finance;


/**
 * @ClassName: FinanceDao
 * @author 降魔战队
 * @date 2018-2-3 21:28:23
 */
 

public interface FinanceDao extends BaseDao<Finance, Long> {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<Finance>
	 */
	Page<Finance> findPage(Date beginDate, Date endDate, Pageable pageable);
}