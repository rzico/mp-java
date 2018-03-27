package net.wit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Feedback;

/**
 * @ClassName: FeedbackService
 * @author 降魔战队
 * @date 2018-3-26 15:13:5
 */

public interface FeedbackService extends BaseService<Feedback, Long> {
	Page<Feedback> findPage(Date beginDate, Date endDate, Pageable pageable);
}