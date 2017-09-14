package net.wit.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.wit.Filter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.Principal;
import net.wit.Filter.Operator;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.wit.dao.PluginConfigDao;
import net.wit.entity.*;
import net.wit.service.PluginConfigService;

/**
 * @ClassName: PluginConfigDaoImpl
 * @author 降魔战队
 * @date 2017-9-14 19:42:8
 */
 
 
@Service("pluginConfigServiceImpl")
public class PluginConfigServiceImpl extends BaseServiceImpl<PluginConfig, Long> implements PluginConfigService {
	@Resource(name = "pluginConfigDaoImpl")
	private PluginConfigDao pluginConfigDao;

	@Resource(name = "pluginConfigDaoImpl")
	public void setBaseDao(PluginConfigDao pluginConfigDao) {
		super.setBaseDao(pluginConfigDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(PluginConfig pluginConfig) {
		super.save(pluginConfig);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public PluginConfig update(PluginConfig pluginConfig) {
		return super.update(pluginConfig);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public PluginConfig update(PluginConfig pluginConfig, String... ignoreProperties) {
		return super.update(pluginConfig, ignoreProperties);
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
	public void delete(PluginConfig pluginConfig) {
		super.delete(pluginConfig);
	}

	public Page<PluginConfig> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return pluginConfigDao.findPage(beginDate,endDate,pageable);
	}
}