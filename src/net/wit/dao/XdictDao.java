package net.wit.dao;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Xdict;

import java.util.Date;


/**
 * @ClassName: XdictDao
 * @author 降魔战队
 * @date 2018-4-5 18:22:28
 */
 

public interface XdictDao extends BaseDao<Xdict, Long> {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<LiveGroup>
	 */
	Page<Xdict> findPage(Date beginDate, Date endDate, Pageable pageable);
}