package net.wit.dao;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.AgentGauge;
import net.wit.entity.Gauge;
import net.wit.entity.Tag;

import java.util.Date;
import java.util.List;


/**
 * @ClassName: AgentGaugeDao
 * @author 降魔战队
 * @date 2018-2-12 21:4:34
 */
 

public interface AgentGaugeDao extends BaseDao<AgentGauge, Long> {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<Gauge>
	 */
	Page<AgentGauge> findPage(Date beginDate, Date endDate, Pageable pageable);
}