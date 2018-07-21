package net.wit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Course;
import net.wit.entity.Enterprise;
import net.wit.entity.Evaluation;
import net.wit.entity.Payment;

/**
 * @ClassName: CourseService
 * @author 降魔战队
 * @date 2018-7-13 14:38:35
 */

public interface CourseService extends BaseService<Course, Long> {
	Page<Course> findPage(Date beginDate, Date endDate, Pageable pageable);

	Page<Course> findPage(Date beginDate, Date endDate, Enterprise enterprise, Pageable pageable);

}