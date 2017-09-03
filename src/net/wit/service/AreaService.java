package net.wit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Area;

/**
 * @ClassName: AreaService
 * @author 降魔战队
 * @date 2017-9-3 20:35:56
 */

public interface AreaService extends BaseService<Area, Long> {
	Page<Area> findPage(Date beginDate, Date endDate, Pageable pageable);
	/**
	 * 查找顶级地区
	 *
	 * @return 顶级地区
	 */
	List<Area> findRoots();

	/**
	 * 查找顶级地区
	 *
	 * @param count
	 *            数量
	 * @return 顶级地区
	 */
	List<Area> findRoots(Integer count);
}