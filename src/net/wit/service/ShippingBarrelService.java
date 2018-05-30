package net.wit.service;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.BarrelStock;
import net.wit.entity.ShippingBarrel;

import java.util.Date;

/**
 * @ClassName: ShippingBarrelService
 * @author 降魔战队
 * @date 2018-5-28 15:8:46
 */

public interface ShippingBarrelService extends BaseService<ShippingBarrel, Long> {
	Page<ShippingBarrel> findPage(Date beginDate, Date endDate, Pageable pageable);
}