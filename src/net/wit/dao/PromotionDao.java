package net.wit.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Promotion;


/**
 * @ClassName: PromotionDao
 * @author 降魔战队
 * @date 2018-4-28 22:28:35
 */
 

public interface PromotionDao extends BaseDao<Promotion, Long> {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<Promotion>
	 */
	Page<Promotion> findPage(Date beginDate, Date endDate, Pageable pageable);
}