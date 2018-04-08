package net.wit.service.impl;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.HostDao;
import net.wit.entity.Host;
import net.wit.service.HostService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @ClassName: HostDaoImpl
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
 
@Service("hostServiceImpl")
public class HostServiceImpl extends BaseServiceImpl<Host, Long> implements HostService {
	@Resource(name = "hostDaoImpl")
	private HostDao hostDao;

	@Resource(name = "hostDaoImpl")
	public void setBaseDao(HostDao hostDao) {
		super.setBaseDao(hostDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(Host host) {
		super.save(host);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Host update(Host host) {
		return super.update(host);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Host update(Host host, String... ignoreProperties) {
		return super.update(host, ignoreProperties);
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
	public void delete(Host host) {
		super.delete(host);
	}

	public Page<Host> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return hostDao.findPage(beginDate,endDate,pageable);
	}
}