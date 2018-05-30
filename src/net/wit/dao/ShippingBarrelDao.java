package net.wit.dao;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.ShippingBarrel;

import java.util.Date;


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
}