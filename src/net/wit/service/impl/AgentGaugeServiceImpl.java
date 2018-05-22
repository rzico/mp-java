package net.wit.service.impl;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.AgentGaugeDao;
import net.wit.dao.GaugeDao;
import net.wit.entity.AgentGauge;
import net.wit.entity.Gauge;
import net.wit.entity.Tag;
import net.wit.service.AgentGaugeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: GaugeDaoImpl
 * @author 降魔战队
 * @date 2018-2-12 21:4:37
 */
 
 
@Service("agentGaugeServiceImpl")
public class AgentGaugeServiceImpl extends BaseServiceImpl<AgentGauge, Long> implements AgentGaugeService {
	@Resource(name = "agentGaugeDaoImpl")
	private AgentGaugeDao agentGaugeDao;

	@Resource(name = "agentGaugeDaoImpl")
	public void setBaseDao(AgentGaugeDao agentGaugeDao) {
		super.setBaseDao(agentGaugeDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(AgentGauge agentGauge) {
		super.save(agentGauge);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public AgentGauge update(AgentGauge agentGauge) {
		return super.update(agentGauge);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public AgentGauge update(AgentGauge agentGauge, String... ignoreProperties) {
		return super.update(agentGauge, ignoreProperties);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void delete(Long... ids) {
		super.delete(ids);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void delete(AgentGauge agentGauge) {
		super.delete(agentGauge);
	}

	public Page<AgentGauge> findPage(Date beginDate,Date endDate, List<Tag> tags, Pageable pageable) {
		return agentGaugeDao.findPage(beginDate,endDate,pageable);
	}
}