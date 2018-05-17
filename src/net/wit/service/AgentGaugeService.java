package net.wit.service;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.AgentGauge;
import net.wit.entity.Gauge;
import net.wit.entity.Tag;

import java.util.Date;
import java.util.List;

/**
 * @ClassName: AgentGaugeService
 * @author 降魔战队
 * @date 2018-2-12 21:4:37
 */

public interface AgentGaugeService extends BaseService<AgentGauge, Long> {
	Page<AgentGauge> findPage(Date beginDate, Date endDate, List<Tag> tags, Pageable pageable);
}