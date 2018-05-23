package net.wit.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Shipping;


/**
 * @ClassName: ShippingDao
 * @author 降魔战队
 * @date 2017-9-14 19:42:5
 */
 

public interface ShippingDao extends BaseDao<Shipping, Long> {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<Shipping>
	 */
	Page<Shipping> findPage(Date beginDate,Date endDate, Pageable pageable);

	/**
	 * 根据订单编号查找订单
	 *
	 * @param sn
	 *            订单编号(忽略大小写)
	 * @return 订单，若不存在则返回null
	 */
	Shipping findBySn(String sn);
}