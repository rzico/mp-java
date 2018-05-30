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

import net.wit.dao.AppletSowingMapDao;
import net.wit.entity.*;
import net.wit.service.AppletSowingMapService;

/**
 * @ClassName: AppletSowingMapDaoImpl
 * @author 降魔战队
 * @date 2018-5-29 16:52:23
 */
 
 
@Service("appletSowingMapServiceImpl")
public class AppletSowingMapServiceImpl extends BaseServiceImpl<AppletSowingMap, Long> implements AppletSowingMapService {
	@Resource(name = "appletSowingMapDaoImpl")
	private AppletSowingMapDao appletSowingMapDao;

	@Resource(name = "appletSowingMapDaoImpl")
	public void setBaseDao(AppletSowingMapDao appletSowingMapDao) {
		super.setBaseDao(appletSowingMapDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(AppletSowingMap appletSowingMap) {
		super.save(appletSowingMap);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public AppletSowingMap update(AppletSowingMap appletSowingMap) {
		return super.update(appletSowingMap);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public AppletSowingMap update(AppletSowingMap appletSowingMap, String... ignoreProperties) {
		return super.update(appletSowingMap, ignoreProperties);
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
	public void delete(AppletSowingMap appletSowingMap) {
		super.delete(appletSowingMap);
	}

	public Page<AppletSowingMap> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return appletSowingMapDao.findPage(beginDate,endDate,pageable);
	}
}