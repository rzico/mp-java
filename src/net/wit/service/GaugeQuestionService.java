package net.wit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.GaugeQuestion;

/**
 * @ClassName: GaugeQuestionService
 * @author 降魔战队
 * @date 2018-2-12 21:4:37
 */

public interface GaugeQuestionService extends BaseService<GaugeQuestion, Long> {
	Page<GaugeQuestion> findPage(Date beginDate, Date endDate, Pageable pageable);
}