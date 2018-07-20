package net.wit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Course;
import net.wit.entity.CourseOrder;
import net.wit.entity.Payment;

/**
 * @ClassName: CourseOrderService
 * @author 降魔战队
 * @date 2018-7-13 14:38:35
 */

public interface CourseOrderService extends BaseService<CourseOrder, Long> {
	Page<CourseOrder> findPage(Date beginDate, Date endDate, Pageable pageable);


	public Payment create(CourseOrder courseOrder);

}