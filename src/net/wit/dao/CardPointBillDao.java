package net.wit.dao;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Card;
import net.wit.entity.CardPointBill;

import java.util.Date;


/**
 * @ClassName: CardPointBillDao
 * @author 降魔战队
 * @date 2017-11-4 18:12:24
 */
 

public interface CardPointBillDao extends BaseDao<CardPointBill, Long> {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<CardPointBill>
	 */
	Page<CardPointBill> findPage(Date beginDate, Date endDate, Pageable pageable);
}