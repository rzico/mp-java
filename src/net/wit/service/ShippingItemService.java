package net.wit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Enterprise;
import net.wit.entity.ShippingItem;
import net.wit.entity.summary.ShippingItemSummary;
import net.wit.entity.summary.ShippingSummary;

/**
 * @ClassName: ShippingItemService
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */

public interface ShippingItemService extends BaseService<ShippingItem, Long> {
	Page<ShippingItem> findPage(Date beginDate,Date endDate, Pageable pageable);

	public List<ShippingItemSummary> summary(Enterprise enterprise, Date beginDate, Date endDate, Pageable pageable);
}