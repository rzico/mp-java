package net.wit.service.impl;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.LiveAdminDao;
import net.wit.entity.LiveAdmin;
import net.wit.service.LiveAdminService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @ClassName: LiveAdminDaoImpl
 * @author 降魔战队
 * @date 2018-4-5 18:22:33
 */
 
 
@Service("liveAdminServiceImpl")
public class LiveAdminServiceImpl extends BaseServiceImpl<LiveAdmin, Long> implements LiveAdminService {
	@Resource(name = "liveAdminDaoImpl")
	private LiveAdminDao liveAdminDao;

	@Resource(name = "liveAdminDaoImpl")
	public void setBaseDao(LiveAdminDao liveAdminDao) {
		super.setBaseDao(liveAdminDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(LiveAdmin liveAdmin) {
		super.save(liveAdmin);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public LiveAdmin update(LiveAdmin liveAdmin) {
		return super.update(liveAdmin);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public LiveAdmin update(LiveAdmin liveAdmin, String... ignoreProperties) {
		return super.update(liveAdmin, ignoreProperties);
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
	public void delete(LiveAdmin liveAdmin) {
		super.delete(liveAdmin);
	}

	public Page<LiveAdmin> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return liveAdminDao.findPage(beginDate,endDate,pageable);
	}
}