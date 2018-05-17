package net.wit.service.impl;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.AgentCategoryDao;
import net.wit.entity.AgentCategory;
import net.wit.service.AgentCategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @ClassName: AgentCategoryServiceImpl
 * @author 降魔战队
 * @date 2017-9-14 19:42:8
 */
 
 
@Service("agentCategoryServiceImpl")
public class AgentCategoryServiceImpl extends BaseServiceImpl<AgentCategory, Long> implements AgentCategoryService {
	@Resource(name = "agentCategoryDaoImpl")
	private AgentCategoryDao agentCategoryDao;

	@Resource(name = "agentCategoryDaoImpl")
	public void setBaseDao(AgentCategoryDao agentCategoryDao) {
		super.setBaseDao(agentCategoryDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(AgentCategory agentCategory) {
		super.save(agentCategory);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public AgentCategory update(AgentCategory agentCategory) {
		return super.update(agentCategory);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public AgentCategory update(AgentCategory agentCategory, String... ignoreProperties) {
		return super.update(agentCategory, ignoreProperties);
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
	public void delete(AgentCategory agentCategory) {
		super.delete(agentCategory);
	}

	public Page<AgentCategory> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return agentCategoryDao.findPage(beginDate,endDate,pageable);
	}
}