package net.wit.dao;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.LiveAdmin;

import java.util.Date;


/**
 * @ClassName: LiveAdminDao
 * @author 降魔战队
 * @date 2018-4-5 18:22:28
 */
 

public interface LiveAdminDao extends BaseDao<LiveAdmin, Long> {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<LiveGroup>
	 */
	Page<LiveAdmin> findPage(Date beginDate, Date endDate, Pageable pageable);
}