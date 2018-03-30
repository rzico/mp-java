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

import net.wit.entity.summary.DepositSummary;
import net.wit.entity.summary.NihtanDepositSummary;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.wit.dao.DepositDao;
import net.wit.entity.*;
import net.wit.service.DepositService;

/**
 * @ClassName: DepositDaoImpl
 * @author 降魔战队
 * @date 2017-9-14 19:42:8
 */
 
 
@Service("depositServiceImpl")
public class DepositServiceImpl extends BaseServiceImpl<Deposit, Long> implements DepositService {
	@Resource(name = "depositDaoImpl")
	private DepositDao depositDao;

	@Resource(name = "depositDaoImpl")
	public void setBaseDao(DepositDao depositDao) {
		super.setBaseDao(depositDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(Deposit deposit) {
		super.save(deposit);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Deposit update(Deposit deposit) {
		return super.update(deposit);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Deposit update(Deposit deposit, String... ignoreProperties) {
		return super.update(deposit, ignoreProperties);
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
	public void delete(Deposit deposit) {
		super.delete(deposit);
	}

	public Page<Deposit> findPage(Date beginDate,Date endDate, Pageable pageable) {
		return depositDao.findPage(beginDate,endDate,pageable);
	}
	public BigDecimal summary(Deposit.Type type,Member member) {
		return depositDao.summary(type,member);
	}
	public BigDecimal summary(Deposit.Type type,Member member,Member seller) {
		return depositDao.summary(type,member,seller);
	}
	public List<DepositSummary> sumPage(Member member, Date beginDate, Date endDate) {
		return depositDao.sumPage(member,beginDate,endDate);
	}
	public List<NihtanDepositSummary> sumNihtanPage(Member member, Date beginDate, Date endDate) {
		return depositDao.sumNihtanPage(member,beginDate,endDate);
	}

}