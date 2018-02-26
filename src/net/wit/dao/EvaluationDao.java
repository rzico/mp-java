package net.wit.dao;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Evaluation;
import net.wit.entity.Tag;

import java.util.Date;
import java.util.List;


/**
 * @ClassName: EvaluationDao
 * @author 降魔战队
 * @date 2018-2-12 21:4:34
 */
 

public interface EvaluationDao extends BaseDao<Evaluation, Long> {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<Gauge>
	 */
	Page<Evaluation> findPage(Date beginDate, Date endDate, Pageable pageable);
}