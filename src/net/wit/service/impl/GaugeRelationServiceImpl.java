package net.wit.service.impl;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.GaugeRelationDao;
import net.wit.entity.GaugeRelation;
import net.wit.service.GaugeRelationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @ClassName: GaugeRelationDaoImpl
 * @author 降魔战队
 * @date 2018-2-12 21:4:37
 */
 
 
@Service("gaugeRelationServiceImpl")
public class GaugeRelationServiceImpl extends BaseServiceImpl<GaugeRelation, Long> implements GaugeRelationService {
	@Resource(name = "gaugeRelationDaoImpl")
	private GaugeRelationDao gaugeRelationDao;

	@Resource(name = "gaugeRelationDaoImpl")
	public void setBaseDao(GaugeRelationDao gaugeRelationDao) {
		super.setBaseDao(gaugeRelationDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(GaugeRelation gaugeRelation) {
		super.save(gaugeRelation);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public GaugeRelation update(GaugeRelation gaugeRelation) {
		return super.update(gaugeRelation);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public GaugeRelation update(GaugeRelation gaugeRelation, String... ignoreProperties) {
		return super.update(gaugeRelation, ignoreProperties);
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
	public void delete(GaugeRelation gaugeRelation) {
		super.delete(gaugeRelation);
	}

	public Page<GaugeRelation> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return gaugeRelationDao.findPage(beginDate,endDate,pageable);
	}
}