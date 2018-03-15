package net.wit.dao;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Vip;

import java.util.Date;
import java.util.List;


/**
 * @ClassName: VipDao
 * @author 降魔战队
 * @date 2017-9-14 19:42:6
 */
 

public interface VipDao extends BaseDao<Vip, Long> {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<Vip>
	 */
	Page<Vip> findPage(Date beginDate, Date endDate, Pageable pageable);
}