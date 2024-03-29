package net.wit.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Enterprise;
import net.wit.entity.Member;


/**
 * @ClassName: EnterpriseDao
 * @author 降魔战队
 * @date 2017-9-14 19:42:4
 */
 

public interface EnterpriseDao extends BaseDao<Enterprise, Long> {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<Enterprise>
	 */
	Page<Enterprise> findPage(Date beginDate,Date endDate, Pageable pageable);

	public Enterprise find(Member member);
}