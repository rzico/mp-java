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

import net.wit.dao.CounselorDao;
import net.wit.entity.*;
import net.wit.service.CounselorService;

/**
 * @ClassName: CounselorDaoImpl
 * @author 降魔战队
 * @date 2018-7-13 14:38:35
 */
 
 
@Service("counselorServiceImpl")
public class CounselorServiceImpl extends BaseServiceImpl<Counselor, Long> implements CounselorService {
	@Resource(name = "counselorDaoImpl")
	private CounselorDao counselorDao;

	@Resource(name = "counselorDaoImpl")
	public void setBaseDao(CounselorDao counselorDao) {
		super.setBaseDao(counselorDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(Counselor counselor) {
		super.save(counselor);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Counselor update(Counselor counselor) {
		return super.update(counselor);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Counselor update(Counselor counselor, String... ignoreProperties) {
		return super.update(counselor, ignoreProperties);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void delete(Long id) {
		Counselor counselor = counselorDao.find(id);
		counselor.setDeleted(true);
		super.update(counselor);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void delete(Long... ids) {
		for (Long id:ids) {
			this.delete(id);
		}
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void delete(Counselor counselor) {
		counselor.setDeleted(true);
		super.update(counselor);
	}

	public Page<Counselor> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return counselorDao.findPage(beginDate,endDate,pageable);
	}
}