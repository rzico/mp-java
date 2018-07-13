package net.wit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Counselor;

/**
 * @ClassName: CounselorService
 * @author 降魔战队
 * @date 2018-7-13 14:38:35
 */

public interface CounselorService extends BaseService<Counselor, Long> {
	Page<Counselor> findPage(Date beginDate, Date endDate, Pageable pageable);
}