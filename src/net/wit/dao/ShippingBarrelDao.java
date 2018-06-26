package net.wit.dao;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Enterprise;
import net.wit.entity.Member;
import net.wit.entity.ShippingBarrel;
import net.wit.entity.summary.BarrelSummary;
import net.wit.entity.summary.OrderSummary;

import java.util.Date;
import java.util.List;


/**
 * @ClassName: BarrelStockDao
 * @author 降魔战队
 * @date 2018-5-28 15:8:41
 */
 

public interface ShippingBarrelDao extends BaseDao<ShippingBarrel, Long> {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<BarrelStock>
	 */
	Page<ShippingBarrel> findPage(Date beginDate, Date endDate, Pageable pageable);


	List<BarrelSummary> summary(Enterprise enterprise, Date beginDate, Date endDate, Pageable pageable);


	List<BarrelSummary> summary_barrel(Enterprise enterprise, Date beginDate, Date endDate, Pageable pageable);

}