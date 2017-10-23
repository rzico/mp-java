package net.wit.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Payment;
import net.wit.entity.Refunds;


/**
 * @ClassName: RefundsDao
 * @author 降魔战队
 * @date 2017-9-14 19:42:5
 */
 

public interface RefundsDao extends BaseDao<Refunds, Long> {
	/**
	 * 根据编号查找收款单
	 * @param sn 编号(忽略大小写)
	 * @return 收款单，若不存在则返回null
	 */
	Refunds findBySn(String sn);
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<Refunds>
	 */
	Page<Refunds> findPage(Date beginDate,Date endDate, Pageable pageable);
}