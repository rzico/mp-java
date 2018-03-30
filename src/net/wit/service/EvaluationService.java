package net.wit.service;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.*;
import net.wit.entity.summary.EvaluationSummary;

import java.util.Date;
import java.util.List;

/**
 * @ClassName: EvaluationService
 * @author 降魔战队
 * @date 2018-2-12 21:4:37
 */

public interface EvaluationService extends BaseService<Evaluation, Long> {
	Page<Evaluation> findPage(Date beginDate, Date endDate, Pageable pageable);

	public Payment create(Evaluation evaluation);

	/**
	 */
	List<EvaluationSummary> sumPromoter(Gauge gauge,Date beginDate, Date endDate);
	public Evaluation answer(Evaluation evaluation,List<EvalAnswer> evals);

}