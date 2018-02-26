package net.wit.dao;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Evaluation;
import net.wit.entity.EvaluationAttribute;
import net.wit.entity.MemberAttribute;
import net.wit.entity.Tag;

import java.util.Date;
import java.util.List;


/**
 * @ClassName: EvaluationAttributeDao
 * @author 降魔战队
 * @date 2018-2-12 21:4:34
 */
 

public interface EvaluationAttributeDao extends BaseDao<EvaluationAttribute, Long> {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<Gauge>
	 */
	Page<EvaluationAttribute> findPage(Date beginDate, Date endDate, Pageable pageable);

	EvaluationAttribute find(Evaluation evaluation, MemberAttribute attribute, EvaluationAttribute.Type type);

}