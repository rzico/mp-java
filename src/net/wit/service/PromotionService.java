package net.wit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Promotion;

/**
 * @ClassName: PromotionService
 * @author 降魔战队
 * @date 2018-4-28 22:28:39
 */

public interface PromotionService extends BaseService<Promotion, Long> {
	Page<Promotion> findPage(Date beginDate, Date endDate, Pageable pageable);
}