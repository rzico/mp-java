package net.wit.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Cart;


/**
 * @ClassName: CartDao
 * @author 降魔战队
 * @date 2017-9-14 19:42:4
 */
 

public interface CartDao extends BaseDao<Cart, Long> {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<Cart>
	 */
	Page<Cart> findPage(Date beginDate,Date endDate, Pageable pageable);
	/**
	 * 清除过期购物车
	 */
	void evictExpired();
}