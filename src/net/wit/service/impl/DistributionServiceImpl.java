package net.wit.service.impl;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.DistributionDao;
import net.wit.dao.ReceiverDao;
import net.wit.entity.Distribution;
import net.wit.entity.Receiver;
import net.wit.service.DistributionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @ClassName: DistributionServiceImpl
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
 
@Service("distributionServiceImpl")
public class DistributionServiceImpl extends BaseServiceImpl<Distribution, Long> implements DistributionService {
	@Resource(name = "distributionDaoImpl")
	private DistributionDao distributionDao;

	@Resource(name = "distributionDaoImpl")
	public void setBaseDao(DistributionDao distributionDao) {
		super.setBaseDao(distributionDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(Distribution distribution) {
		super.save(distribution);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Distribution update(Distribution distribution) {
		return super.update(distribution);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Distribution update(Distribution distribution, String... ignoreProperties) {
		return super.update(distribution, ignoreProperties);
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
	public void delete(Distribution distribution) {
		super.delete(distribution);
	}

	public Page<Distribution> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return distributionDao.findPage(beginDate,endDate,pageable);
	}
}