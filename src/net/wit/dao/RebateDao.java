package net.wit.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Rebate;
import net.wit.entity.summary.RebateSummary;


/**
 * @ClassName: RebateDao
 * @author 降魔战队
 * @date 2018-4-24 20:57:48
 */
 

public interface RebateDao extends BaseDao<Rebate, Long> {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<Rebate>
	 */
	Page<Rebate> findPage(Date beginDate, Date endDate, Pageable pageable);
	Page<RebateSummary> sumPage(Date beginDate, Date endDate, Pageable pageable);

}