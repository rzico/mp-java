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

import net.wit.dao.FeedbackDao;
import net.wit.entity.*;
import net.wit.service.FeedbackService;

/**
 * @ClassName: FeedbackDaoImpl
 * @author 降魔战队
 * @date 2018-3-26 15:13:5
 */
 
 
@Service("feedbackServiceImpl")
public class FeedbackServiceImpl extends BaseServiceImpl<Feedback, Long> implements FeedbackService {
	@Resource(name = "feedbackDaoImpl")
	private FeedbackDao feedbackDao;

	@Resource(name = "feedbackDaoImpl")
	public void setBaseDao(FeedbackDao feedbackDao) {
		super.setBaseDao(feedbackDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(Feedback feedback) {
		super.save(feedback);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Feedback update(Feedback feedback) {
		return super.update(feedback);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Feedback update(Feedback feedback, String... ignoreProperties) {
		return super.update(feedback, ignoreProperties);
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
	public void delete(Feedback feedback) {
		super.delete(feedback);
	}

	public Page<Feedback> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return feedbackDao.findPage(beginDate,endDate,pageable);
	}
}