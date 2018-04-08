package net.wit.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Live;


/**
 * @ClassName: LiveDao
 * @author 降魔战队
 * @date 2018-4-5 18:22:28
 */
 

public interface LiveDao extends BaseDao<Live, Long> {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<Live>
	 */
	Page<Live> findPage(Date beginDate, Date endDate, Pageable pageable);
}