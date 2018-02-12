package net.wit.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Gauge;


/**
 * @ClassName: GaugeDao
 * @author 降魔战队
 * @date 2018-2-12 21:4:34
 */
 

public interface GaugeDao extends BaseDao<Gauge, Long> {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<Gauge>
	 */
	Page<Gauge> findPage(Date beginDate, Date endDate, Pageable pageable);
}