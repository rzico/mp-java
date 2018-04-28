package net.wit.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Road;


/**
 * @ClassName: RoadDao
 * @author 降魔战队
 * @date 2018-4-28 14:18:44
 */
 

public interface RoadDao extends BaseDao<Road, Long> {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<Road>
	 */
	Page<Road> findPage(Date beginDate, Date endDate, Pageable pageable);
}