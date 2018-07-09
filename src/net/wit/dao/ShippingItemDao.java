package net.wit.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Enterprise;
import net.wit.entity.Member;
import net.wit.entity.ShippingItem;
import net.wit.entity.summary.OrderItemSummary;
import net.wit.entity.summary.ShippingItemSummary;


/**
 * @ClassName: ShippingItemDao
 * @author 降魔战队
 * @date 2017-9-14 19:42:5
 */
 

public interface ShippingItemDao extends BaseDao<ShippingItem, Long> {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<ShippingItem>
	 */
	Page<ShippingItem> findPage(Date beginDate,Date endDate, Pageable pageable);

	public List<ShippingItemSummary> summary(Enterprise enterprise, Date beginDate, Date endDate, Pageable pageable);
}