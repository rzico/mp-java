package net.wit.dao;

import java.util.Date;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.CounselorOrder;


/**
 * @ClassName: SubscribeDao
 * @author 降魔战队
 * @date 2018-7-13 14:38:31
 */
 

public interface CounselorOrderDao extends BaseDao<CounselorOrder, Long> {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<Subscribe>
	 */
	Page<CounselorOrder> findPage(Date beginDate, Date endDate, Pageable pageable);
}