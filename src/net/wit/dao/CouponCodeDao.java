package net.wit.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.CouponCode;


/**
 * @ClassName: CouponCodeDao
 * @author 降魔战队
 * @date 2017-9-14 19:42:4
 */
 

public interface CouponCodeDao extends BaseDao<CouponCode, Long> {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<CouponCode>
	 */
	Page<CouponCode> findPage(Date beginDate,Date endDate, Pageable pageable);
}