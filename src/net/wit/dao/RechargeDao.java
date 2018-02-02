package net.wit.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Recharge;


/**
 * @ClassName: RechargeDao
 * @author 降魔战队
 * @date 2018-2-1 14:1:25
 */
 

public interface RechargeDao extends BaseDao<Recharge, Long> {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<Recharge>
	 */
	Page<Recharge> findPage(Date beginDate, Date endDate, Pageable pageable);
}