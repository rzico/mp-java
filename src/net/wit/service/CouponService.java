package net.wit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Coupon;
import net.wit.entity.Product;

/**
 * @ClassName: CouponService
 * @author 降魔战队
 * @date 2017-9-14 19:42:8
 */

public interface CouponService extends BaseService<Coupon, Long> {
	Page<Coupon> findPage(Date beginDate,Date endDate, Pageable pageable);

	Coupon create(Product product);
}