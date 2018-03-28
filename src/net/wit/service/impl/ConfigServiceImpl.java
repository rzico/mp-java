package net.wit.service.impl;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.MemberDao;
import net.wit.dao.ConfigDao;
import net.wit.entity.Member;
import net.wit.entity.Config;
import net.wit.service.ConfigService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @ClassName: ConfigDaoImpl
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */

@Service("configServiceImpl")
public class ConfigServiceImpl extends BaseServiceImpl<Config, Long> implements ConfigService {
	@Resource(name = "configDaoImpl")
	private ConfigDao configDao;

	@Resource(name = "memberDaoImpl")
	private MemberDao memberDao;

	@Resource(name = "configDaoImpl")
	public void setBaseDao(ConfigDao configDao) {
		super.setBaseDao(configDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(Config config) {
		super.save(config);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Config update(Config config) {
		return super.update(config);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Config update(Config config, String... ignoreProperties) {
		return super.update(config, ignoreProperties);
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
	public void delete(Config config) {
		super.delete(config);
	}

	public Page<Config> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return configDao.findPage(beginDate,endDate,pageable);
	}
	public Config find(String key) {
		return configDao.find(key);
	}
}