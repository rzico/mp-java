package net.wit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Cart;
import net.wit.entity.Member;

/**
 * @ClassName: CartService
 * @author 降魔战队
 * @date 2017-9-14 19:42:7
 */

public interface CartService extends BaseService<Cart, Long> {

	/**
	 * 获取当前购物车
	 *
	 * @return 当前购物车,若不存在则返回null
	 */
	Cart getCurrent();

	/**
	 * 合并临时购物车至会员
	 *
	 * @param member
	 *            会员
	 * @param cart
	 *            临时购物车
	 */
	void merge(Member member, Cart cart);

	/**
	 * 清除过期购物车
	 */
	void evictExpired();

	Page<Cart> findPage(Date beginDate,Date endDate, Pageable pageable);
}