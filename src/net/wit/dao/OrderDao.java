package net.wit.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Order;


/**
 * @ClassName: OrderDao
 * @author 降魔战队
 * @date 2017-9-14 19:42:5
 */
 

public interface OrderDao extends BaseDao<Order, Long> {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<Order>
	 */
	Page<Order> findPage(Date beginDate,Date endDate, String status, Pageable pageable);

	/**
	 * 根据订单编号查找订单
	 *
	 * @param sn
	 *            订单编号(忽略大小写)
	 * @return 订单，若不存在则返回null
	 */
	Order findBySn(String sn);

}