package net.wit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Navigation;

/**
 * @ClassName: NavigationService
 * @author 降魔战队
 * @date 2018-6-12 10:12:13
 */

public interface NavigationService extends BaseService<Navigation, Long> {
	Page<Navigation> findPage(Date beginDate, Date endDate, Pageable pageable);
}