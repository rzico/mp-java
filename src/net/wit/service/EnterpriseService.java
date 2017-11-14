package net.wit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Enterprise;
import net.wit.entity.Member;
import net.wit.entity.Topic;

/**
 * @ClassName: EnterpriseService
 * @author 降魔战队
 * @date 2017-9-14 19:42:8
 */

public interface EnterpriseService extends BaseService<Enterprise, Long> {
	Page<Enterprise> findPage(Date beginDate,Date endDate, Pageable pageable);

	public Enterprise create(Topic topic);
}