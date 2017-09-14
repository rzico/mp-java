package net.wit.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Smssend;


/**
 * @ClassName: SmssendDao
 * @author 降魔战队
 * @date 2017-9-14 19:42:6
 */
 

public interface SmssendDao extends BaseDao<Smssend, Long> {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<Smssend>
	 */
	Page<Smssend> findPage(Date beginDate,Date endDate, Pageable pageable);
}