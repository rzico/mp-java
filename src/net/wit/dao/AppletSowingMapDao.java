package net.wit.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.AppletSowingMap;


/**
 * @ClassName: AppletSowingMapDao
 * @author 降魔战队
 * @date 2018-5-29 16:52:18
 */
 

public interface AppletSowingMapDao extends BaseDao<AppletSowingMap, Long> {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<AppletSowingMap>
	 */
	Page<AppletSowingMap> findPage(Date beginDate, Date endDate, Pageable pageable);
}