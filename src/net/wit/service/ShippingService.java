package net.wit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Shipping;

/**
 * @ClassName: ShippingService
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */

public interface ShippingService extends BaseService<Shipping, Long> {
	Page<Shipping> findPage(Date beginDate,Date endDate, Pageable pageable);
}