package net.wit.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import net.wit.Page;
import net.wit.Pageable;

import net.wit.dao.CounselorOrderDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.wit.dao.CounselorOrderDao;
import net.wit.entity.*;
import net.wit.service.CounselorOrderService;

/**
 * @ClassName: SubscribeDaoImpl
 * @author 降魔战队
 * @date 2018-7-13 14:38:37
 */
 
 
@Service("counselorOrderServiceImpl")
public class CounselorOrderServiceImpl extends BaseServiceImpl<CounselorOrder, Long> implements CounselorOrderService {
	@Resource(name = "counselorOrderDaoImpl")
	private CounselorOrderDao counselorOrderDao;

	@Resource(name = "counselorOrderDaoImpl")
	public void setBaseDao(CounselorOrderDao counselorOrderDao) {
		super.setBaseDao(counselorOrderDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(CounselorOrder subscribe) {
		super.save(subscribe);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public CounselorOrder update(CounselorOrder subscribe) {
		return super.update(subscribe);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public CounselorOrder update(CounselorOrder subscribe, String... ignoreProperties) {
		return super.update(subscribe, ignoreProperties);
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
	public void delete(CounselorOrder subscribe) {
		super.delete(subscribe);
	}

	public Page<CounselorOrder> findPage(Date beginDate, Date endDate, Pageable pageable) {
		return counselorOrderDao.findPage(beginDate,endDate,pageable);
	}
}