package net.wit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.*;

/**
 * @ClassName: OrderService
 * @author 降魔战队
 * @date 2017-9-14 19:42:8
 */

public interface OrderService extends BaseService<Order, Long> {
	Page<Order> findPage(Date beginDate,Date endDate, String status, Pageable pageable);

	/**
	 * 根据订单编号查找订单
	 *
	 * @param sn
	 *            订单编号(忽略大小写)
	 * @return 订单，若不存在则返回null
	 */
	Order findBySn(String sn);

	/**
	 * 释放过期订单库存
	 */
	void releaseStock();

	/**
	 * 生成订单
	 *
	 * @param cart
	 *            购物车
	 * @param receiver
	 *            收货地址
	 * @param memo
	 *            附言
	 * @return 订单
	 */
	Order build(Member member ,Product product, Integer quantity, Cart cart, Receiver receiver,String memo);

	/**
	 * 创建订单
	 *
	 * @param cart
	 *            购物车
	 * @param receiver
	 *            收货地址
	 * @param memo
	 *            附言
	 * @param operator
	 *            操作员
	 * @return 订单
	 */
	Order create(Member member ,Product product, Integer quantity, Cart cart, Receiver receiver, String memo,Long xuid, Admin operator);

	/**
	 * 更新订单
	 *
	 * @param order
	 *            订单
	 * @param operator
	 *            操作员
	 */
	void update(Order order, Admin operator);

	/**
	 * 订单确认
	 *
	 * @param order
	 *            订单
	 * @param operator
	 *            操作员
	 */
	void confirm(Order order, Admin operator) throws Exception;

	/**
	 * 订单完成
	 *
	 * @param order
	 *            订单
	 * @param operator
	 *            操作员
	 */
	void complete(Order order, Admin operator) throws Exception;

	/**
	 * 订单取消
	 *
	 * @param order
	 *            订单
	 * @param operator
	 *            操作员
	 */
	void cancel(Order order, Admin operator) throws Exception;

	/**
	 * 订单支付
	 *
	 * @param order
	 *            订单
	 * @param operator
	 *            操作员
	 */
	Payment payment(Order order, Admin operator) throws Exception;

	/**
	 * 订单退款
	 *
	 * @param order
	 *            订单
	 * @param operator
	 *            操作员
	 */
	public void refunds(Order order,Admin operator) throws Exception;

		/**
         * 订单发货
         *
         * @param order
         *            订单
         * @param operator
         *            操作员
         */
	void shipping(Order order, Admin operator) throws Exception;

	/**
	 * 订单退货
	 *
	 * @param order
	 *            订单
	 * @param operator
	 *            操作员
	 */
	void returns(Order order, Admin operator) throws Exception;


}