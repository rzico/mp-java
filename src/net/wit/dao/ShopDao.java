package net.wit.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Member;
import net.wit.entity.Shop;


/**
 * @ClassName: ShopDao
 * @author 降魔战队
 * @date 2017-11-4 18:12:25
 */
 

public interface ShopDao extends BaseDao<Shop, Long> {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<Shop>
	 */
	Page<Shop> findPage(Date beginDate,Date endDate, Pageable pageable);

	/**
	 * @Title：findPage
	 * @Description：联盟商家配送点
	 * @param pageable
	 * @return Page<Shop>
	 */
	Page<Shop> findPage(Member owner, Pageable pageable);

	Shop find(String code);
}