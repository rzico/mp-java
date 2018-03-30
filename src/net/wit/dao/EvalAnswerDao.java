package net.wit.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.EvalAnswer;


/**
 * @ClassName: EvalAnswerDao
 * @author 降魔战队
 * @date 2018-3-14 23:23:47
 */
 

public interface EvalAnswerDao extends BaseDao<EvalAnswer, Long> {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<EvalAnswer>
	 */
	Page<EvalAnswer> findPage(Date beginDate, Date endDate, Pageable pageable);
}