package net.wit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Barrel;

/**
 * @ClassName: BarrelService
 * @author 降魔战队
 * @date 2018-5-28 15:8:46
 */

public interface BarrelService extends BaseService<Barrel, Long> {
	Page<Barrel> findPage(Date beginDate, Date endDate, Pageable pageable);
}