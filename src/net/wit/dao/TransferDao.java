package net.wit.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Transfer;


/**
 * @ClassName: TransferDao
 * @author 降魔战队
 * @date 2017-10-17 19:48:15
 */
 

public interface TransferDao extends BaseDao<Transfer, Long> {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<Transfer>
	 */
	Page<Transfer> findPage(Date beginDate, Date endDate, Pageable pageable);
	Transfer findBySn(String sn);
	Transfer findByOrderSn(String sn);
}