package net.wit.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Card;


/**
 * @ClassName: CardDao
 * @author 降魔战队
 * @date 2017-11-4 18:12:24
 */
 

public interface CardDao extends BaseDao<Card, Long> {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<Card>
	 */
	Page<Card> findPage(Date beginDate,Date endDate, Pageable pageable);
	public Card find(String code);
}