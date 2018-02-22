package net.wit.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.CardBill;


/**
 * @ClassName: CardBillDao
 * @author 降魔战队
 * @date 2017-11-4 18:12:24
 */
 

public interface CardBillDao extends BaseDao<CardBill, Long> {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<CardBill>
	 */
	Page<CardBill> findPage(Date beginDate,Date endDate, Pageable pageable);
}