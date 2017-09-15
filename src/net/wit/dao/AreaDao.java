package net.wit.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Area;


/**
 * @ClassName: AreaDao
 * @author 降魔战队
 * @date 2017-9-14 19:42:4
 */
 

public interface AreaDao extends BaseDao<Area, Long> {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<Area>
	 */
	Page<Area> findPage(Date beginDate, Date endDate, Pageable pageable);
	/**
	 * 查找顶级地区
	 *
	 * @param count
	 *            数量
	 * @return 顶级地区
	 */
	List<Area> findRoots(Integer count);
}