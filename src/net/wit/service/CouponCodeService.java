package net.wit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.CouponCode;

/**
 * @ClassName: CouponCodeService
 * @author 降魔战队
 * @date 2017-9-14 19:42:8
 */

public interface CouponCodeService extends BaseService<CouponCode, Long> {
	/**
	 * 根据编号查找优惠券
	 * @param code 编号(忽略大小写)
	 * @return 优惠券，若不存在则返回null
	 */
	CouponCode findByCode(String code);
	Page<CouponCode> findPage(Date beginDate,Date endDate, Pageable pageable);
}