package net.wit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Order;
import net.wit.entity.Shipping;

/**
 * @ClassName: ShippingService
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */

public interface ShippingService extends BaseService<Shipping, Long> {
	Page<Shipping> findPage(Date beginDate,Date endDate, Pageable pageable);


	/**
	 * 根据订单编号查找订单
	 *
	 * @param sn
	 *            订单编号(忽略大小写)
	 * @return 订单，若不存在则返回null
	 */
	Shipping findBySn(String sn);

	Shipping create(Order order);

	Shipping dispatch(Shipping shipping) throws Exception;

	Shipping receive(Shipping shipping) throws Exception;

	Shipping completed(Shipping shipping) throws Exception;

}