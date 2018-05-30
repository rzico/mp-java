package net.wit.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Barrel;
import net.wit.entity.BarrelStock;
import net.wit.entity.Member;


/**
 * @ClassName: BarrelStockDao
 * @author 降魔战队
 * @date 2018-5-28 15:8:41
 */
 

public interface BarrelStockDao extends BaseDao<BarrelStock, Long> {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<BarrelStock>
	 */
	Page<BarrelStock> findPage(Date beginDate, Date endDate, Pageable pageable);

	BarrelStock find(Member member, Barrel barrel);
}