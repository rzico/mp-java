package net.wit.service;

import java.util.Date;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.CounselorOrder;

/**
 * @ClassName: CounselorOrderService
 * @author 降魔战队
 * @date 2018-7-13 14:38:37
 */

public interface CounselorOrderService extends BaseService<CounselorOrder, Long> {
	Page<CounselorOrder> findPage(Date beginDate, Date endDate, Pageable pageable);
}