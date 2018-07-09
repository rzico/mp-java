package net.wit.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Organization;


/**
 * @ClassName: OrganizationDao
 * @author 降魔战队
 * @date 2018-2-28 16:42:3
 */
 

public interface OrganizationDao extends BaseDao<Organization, Long> {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<Organization>
	 */
	Page<Organization> findPage(Date beginDate, Date endDate, Pageable pageable);
}