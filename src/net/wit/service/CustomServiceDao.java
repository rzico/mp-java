package net.wit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.BaseDao;
import net.wit.entity.CustomService;


/**
 * @ClassName: CustomServiceDao
 * @author 降魔战队
 * @date 2018-2-3 21:3:47
 */
 

public interface CustomServiceDao extends BaseDao<CustomService, Long> {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<CustomService>
	 */
	Page<CustomService> findPage(Date beginDate, Date endDate, Pageable pageable);
}