package net.wit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Subscribe;

/**
 * @ClassName: SubscribeService
 * @author 降魔战队
 * @date 2018-7-13 14:38:37
 */

public interface SubscribeService extends BaseService<Subscribe, Long> {
	Page<Subscribe> findPage(Date beginDate, Date endDate, Pageable pageable);
}