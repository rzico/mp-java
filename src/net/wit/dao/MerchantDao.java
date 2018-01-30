package net.wit.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Merchant;


/**
 * @ClassName: MerchantDao
 * @author 降魔战队
 * @date 2018-1-10 16:2:38
 */
 

public interface MerchantDao extends BaseDao<Merchant, Long> {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<Merchant>
	 */
	Page<Merchant> findPage(Date beginDate,Date endDate, Pageable pageable);
}