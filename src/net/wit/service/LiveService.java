package net.wit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Live;

/**
 * @ClassName: LiveService
 * @author 降魔战队
 * @date 2018-4-5 18:22:32
 */

public interface LiveService extends BaseService<Live, Long> {
	Page<Live> findPage(Date beginDate, Date endDate, Pageable pageable);
}