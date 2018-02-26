package net.wit.service;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.*;

import java.util.Date;
import java.util.List;

/**
 * @ClassName: EvaluationService
 * @author 降魔战队
 * @date 2018-2-12 21:4:37
 */

public interface EvaluationAttributeService extends BaseService<EvaluationAttribute, Long> {
	Page<EvaluationAttribute> findPage(Date beginDate, Date endDate, Pageable pageable);
	public EvaluationAttribute find(Evaluation evaluation, MemberAttribute attribute,EvaluationAttribute.Type type);
}