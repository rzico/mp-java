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

import net.wit.dao.TopicBillDao;
import net.wit.entity.*;
import net.wit.service.TopicBillService;

/**
 * @ClassName: TopicBillDaoImpl
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */
 
 
@Service("topicBillServiceImpl")
public class TopicBillServiceImpl extends BaseServiceImpl<TopicBill, Long> implements TopicBillService {
	@Resource(name = "topicBillDaoImpl")
	private TopicBillDao topicBillDao;

	@Resource(name = "topicBillDaoImpl")
	public void setBaseDao(TopicBillDao topicBillDao) {
		super.setBaseDao(topicBillDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(TopicBill topicBill) {
		super.save(topicBill);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public TopicBill update(TopicBill topicBill) {
		return super.update(topicBill);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public TopicBill update(TopicBill topicBill, String... ignoreProperties) {
		return super.update(topicBill, ignoreProperties);
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
	public void delete(TopicBill topicBill) {
		super.delete(topicBill);
	}

	public Page<TopicBill> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return topicBillDao.findPage(beginDate,endDate,pageable);
	}
}