package net.wit.service;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.BarrelStock;
import net.wit.entity.Enterprise;
import net.wit.entity.Member;
import net.wit.entity.ShippingBarrel;
import net.wit.entity.summary.BarrelSummary;
import net.wit.entity.summary.PaymentSummary;

import java.util.Date;
import java.util.List;

/**
 * @ClassName: ShippingBarrelService
 * @author 降魔战队
 * @date 2018-5-28 15:8:46
 */

public interface ShippingBarrelService extends BaseService<ShippingBarrel, Long> {
	Page<ShippingBarrel> findPage(Date beginDate, Date endDate, Pageable pageable);

	public List<BarrelSummary> summary(Enterprise enterprise, Date beginDate, Date endDate, Pageable pageable);
	public List<BarrelSummary> summary_barrel(Enterprise enterprise, Date beginDate, Date endDate, Pageable pageable);

}