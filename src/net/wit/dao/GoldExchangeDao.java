package net.wit.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.GoldExchange;


/**
 * @ClassName: GoldExchangeDao
 * @author 降魔战队
 * @date 2018-3-25 14:59:0
 */
 

public interface GoldExchangeDao extends BaseDao<GoldExchange, Long> {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<GmGoldExchange>
	 */
	Page<GoldExchange> findPage(Date beginDate, Date endDate, Pageable pageable);
}