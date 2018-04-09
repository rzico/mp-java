package net.wit.service.impl;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.calculator.GeneCalculator;
import net.wit.dao.*;
import net.wit.dao.EvaluationDao;
import net.wit.entity.*;
import net.wit.entity.Evaluation;
import net.wit.entity.summary.EvaluationSummary;
import net.wit.service.EvaluationService;
import net.wit.service.EvaluationService;
import net.wit.service.SnService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: EvaluationServiceImpl
 * @author 降魔战队
 * @date 2018-2-12 21:4:37
 */
 
 
@Service("evaluationServiceImpl")
public class EvaluationServiceImpl extends BaseServiceImpl<Evaluation, Long> implements EvaluationService {
	@Resource(name = "evaluationDaoImpl")
	private EvaluationDao evaluationDao;

	@Resource(name = "snServiceImpl")
	private SnService snService;

	@Resource(name = "paymentDaoImpl")
	private PaymentDao paymentDao;

	@Resource(name = "gaugeDaoImpl")
	private GaugeDao gaugeDao;

	@Resource(name = "evalAnswerDaoImpl")
	private EvalAnswerDao evalAnswerDao;

	@Resource(name = "evaluationDaoImpl")
	public void setBaseDao(EvaluationDao evaluationDao) {
		super.setBaseDao(evaluationDao);
	}
	
	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public void save(Evaluation evaluation) {
		super.save(evaluation);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Evaluation update(Evaluation evaluation) {
		return super.update(evaluation);
	}

	@Override
	@Transactional
	//@CacheEvict(value = "authorization", allEntries = true)
	public Evaluation update(Evaluation evaluation, String... ignoreProperties) {
		return super.update(evaluation, ignoreProperties);
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
	public void delete(Evaluation evaluation) {
		super.delete(evaluation);
	}

	public Page<Evaluation> findPage(Date beginDate,Date endDate,Pageable pageable) {
		return evaluationDao.findPage(beginDate,endDate,pageable);
	}
	public Payment create(Evaluation evaluation) {
		evaluationDao.persist(evaluation);
		Payment payment = new Payment();
		payment.setAmount(evaluation.getPrice());
		payment.setStatus(Payment.Status.waiting);
		payment.setType(Payment.Type.evaluation);
		payment.setMethod(Payment.Method.online);
		payment.setMember(evaluation.getMember());
		payment.setPayee(evaluation.getMember());
		payment.setSn(snService.generate(Sn.Type.payment));
		payment.setMemo("购买测评");
		payment.setEvaluation(evaluation);
		if (payment.getAmount().compareTo(BigDecimal.ZERO)==0) {
			payment.setStatus(Payment.Status.success);
			evaluation.setEvalStatus(Evaluation.EvalStatus.paid);
			evaluationDao.merge(evaluation);
		}
		paymentDao.persist(payment);

		return payment;
	}

	@Transactional
	public Evaluation answer(Evaluation evaluation,List<EvalAnswer> evals) throws Exception  {
		Long sec = evaluation.getSeconds();
		if (sec==null) {
			sec = 0L;
		}
		Evaluation ev = evaluationDao.find(evaluation.getId());
		ev.setSeconds(sec);
        for (EvalAnswer answer:evals) {
        	answer.setEvaluation(ev);
			answer.setGauge(ev.getGauge());
			answer.setMember(ev.getMember());
			evalAnswerDao.persist(answer);
			evalAnswerDao.flush();
		}
        try {
	    	GeneCalculator calculator = new GeneCalculator();
            calculator.calcAll(ev);
            if (calculator.getResults().size()==0) {
                throw new RuntimeException("无效测试结果");
            }
			ev.setSeconds(sec);
			ev.setResult(calculator.getHtml());
			ev.setEvalvar(calculator.getVars());
        } catch (Exception e) {
			throw new RuntimeException(e.getMessage());
        }
		ev.setEval(new Long(ev.getEvalAnswers().size()));
		ev.setEvalStatus(Evaluation.EvalStatus.completed);
		evaluationDao.merge(ev);
        Gauge gauge = ev.getGauge();
        gauge.setEvaluation(gauge.getEvaluation()+1L);
		gaugeDao.merge(gauge);
        return ev;

    }

	/**
	 */
	public List<EvaluationSummary> sumPromoter(Gauge gauge,Date beginDate, Date endDate) {
		return evaluationDao.sumPromoter(gauge,beginDate,endDate);
	}


}