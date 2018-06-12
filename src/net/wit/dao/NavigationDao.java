package net.wit.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Navigation;


/**
 * @ClassName: NavigationDao
 * @author 降魔战队
 * @date 2018-6-12 10:12:9
 */
 

public interface NavigationDao extends BaseDao<Navigation, Long> {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<Navigation>
	 */
	Page<Navigation> findPage(Date beginDate, Date endDate, Pageable pageable);
}