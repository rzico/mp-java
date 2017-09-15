package net.wit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Returns;

/**
 * @ClassName: ReturnsService
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */

public interface ReturnsService extends BaseService<Returns, Long> {
	Page<Returns> findPage(Date beginDate,Date endDate, Pageable pageable);
}