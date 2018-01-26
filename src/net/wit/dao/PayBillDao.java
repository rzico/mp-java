package net.wit.dao;

import java.util.Date;
import java.util.List;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Enterprise;
import net.wit.entity.PayBill;
import net.wit.entity.Shop;
import net.wit.entity.summary.PayBillShopSummary;


/**
 * @ClassName: PayBillDao
 * @author 降魔战队
 * @date 2017-11-4 18:12:25
 */
 

public interface PayBillDao extends BaseDao<PayBill, Long> {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<PayBill>
	 */
	Page<PayBill> findPage(Date beginDate,Date endDate, Pageable pageable);


	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @return Page<PayBill>
	 */
	List<PayBillShopSummary> sumPage(Shop shop, Enterprise enterprise, Date beginDate, Date endDate);

}