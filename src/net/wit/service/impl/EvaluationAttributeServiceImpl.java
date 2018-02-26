package net.wit.service.impl;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.EvaluationAttributeDao;
import net.wit.dao.EvaluationAttributeDao;
import net.wit.dao.PaymentDao;
import net.wit.entity.*;
import net.wit.service.EvaluationAttributeService;
import net.wit.service.EvaluationAttributeService;
import net.wit.service.SnService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: EvaluationAttributeAttributeServiceImpl
 * @author 降魔战队
 * @date 2018-2-12 21:4:37
 */
 
 
@Service("EvaluationAttributeServiceImpl")
public class EvaluationAttributeServiceImpl extends BaseServiceImpl<EvaluationAttribute, Long> implements EvaluationAttributeService {
	@Resource(name = "evaluationAttributeAttributeDaoImpl")
	private EvaluationAttributeDao evaluationAttributeDao;

	@Resource(name = "snServiceImpl")
	private SnService snService;

	@Resource(name = "paymentDaoImpl")
	private PaymentDao paymentDao;

	@Resource(name = "evaluationAttributeDaoImpl")
	public void setBaseDao(EvaluationAttributeDao EvaluationAttributeDao) {
		super.setBaseDao(EvaluationAttributeDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(EvaluationAttribute evaluationAttribute) {
		super.save(evaluationAttribute);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public EvaluationAttribute update(EvaluationAttribute evaluationAttribute) {
		return super.update(evaluationAttribute);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public EvaluationAttribute update(EvaluationAttribute evaluationAttribute, String... ignoreProperties) {
		return super.update(evaluationAttribute, ignoreProperties);
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
	public void delete(EvaluationAttribute evaluationAttribute) {
		super.delete(evaluationAttribute);
	}

	public Page<EvaluationAttribute> findPage(Date beginDate, Date endDate, Pageable pageable) {
		return evaluationAttributeDao.findPage(beginDate,endDate,pageable);
	}
	public EvaluationAttribute find(Evaluation evaluation, MemberAttribute attribute,EvaluationAttribute.Type type) {
		return evaluationAttributeDao.find(evaluation,attribute,type);
	}

}