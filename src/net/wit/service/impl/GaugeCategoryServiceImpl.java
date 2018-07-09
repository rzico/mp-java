package net.wit.service.impl;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.GaugeCategoryDao;
import net.wit.entity.GaugeCategory;
import net.wit.service.GaugeCategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @ClassName: GaugeCategoryServiceImpl
 * @author 降魔战队
 * @date 2017-9-14 19:42:8
 */
 
 
@Service("gaugeCategoryServiceImpl")
public class GaugeCategoryServiceImpl extends BaseServiceImpl<GaugeCategory, Long> implements GaugeCategoryService {
	@Resource(name = "gaugeCategoryDaoImpl")
	private GaugeCategoryDao gaugeCategoryDao;

	@Resource(name = "gaugeCategoryDaoImpl")
	public void setBaseDao(GaugeCategoryDao gaugeCategoryDao) {
		super.setBaseDao(gaugeCategoryDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(GaugeCategory gaugeCategory) {
		super.save(gaugeCategory);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public GaugeCategory update(GaugeCategory gaugeCategory) {
		return super.update(gaugeCategory);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public GaugeCategory update(GaugeCategory gaugeCategory, String... ignoreProperties) {
		return super.update(gaugeCategory, ignoreProperties);
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
	public void delete(GaugeCategory gaugeCategory) {
		super.delete(gaugeCategory);
	}

	public Page<GaugeCategory> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return gaugeCategoryDao.findPage(beginDate,endDate,pageable);
	}
}