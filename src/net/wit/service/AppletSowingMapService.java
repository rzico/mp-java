package net.wit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.AppletSowingMap;

/**
 * @ClassName: AppletSowingMapService
 * @author 降魔战队
 * @date 2018-5-29 16:52:23
 */

public interface AppletSowingMapService extends BaseService<AppletSowingMap, Long> {
	Page<AppletSowingMap> findPage(Date beginDate, Date endDate, Pageable pageable);
}