package net.wit.dao;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.CouponCode;
import net.wit.entity.CouponGiven;

import java.util.Date;


/**
 * @ClassName: CouponGivenDao
 * @author 降魔战队
 * @date 2017-9-14 19:42:4
 */
 

public interface CouponGivenDao extends BaseDao<CouponGiven, Long> {

	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<CouponGiven>
	 */
	Page<CouponGiven> findPage(Date beginDate, Date endDate, Pageable pageable);
}