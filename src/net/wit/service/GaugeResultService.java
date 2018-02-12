package net.wit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.GaugeResult;

/**
 * @ClassName: GaugeResultService
 * @author 降魔战队
 * @date 2018-2-12 21:4:37
 */

public interface GaugeResultService extends BaseService<GaugeResult, Long> {
	Page<GaugeResult> findPage(Date beginDate, Date endDate, Pageable pageable);
}