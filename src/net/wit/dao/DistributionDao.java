package net.wit.dao;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Distribution;

import java.util.Date;


/**
 * @ClassName: ReceiverDao
 * @author 降魔战队
 * @date 2017-9-14 19:42:5
 */
 

public interface DistributionDao extends BaseDao<Distribution, Long> {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<Receiver>
	 */
	Page<Distribution> findPage(Date beginDate, Date endDate, Pageable pageable);
}