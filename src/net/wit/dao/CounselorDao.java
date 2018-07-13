package net.wit.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Counselor;


/**
 * @ClassName: CounselorDao
 * @author 降魔战队
 * @date 2018-7-13 14:38:29
 */
 

public interface CounselorDao extends BaseDao<Counselor, Long> {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<Counselor>
	 */
	Page<Counselor> findPage(Date beginDate, Date endDate, Pageable pageable);
}