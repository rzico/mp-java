package net.wit.service;

import java.util.Date;
import java.util.List;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.*;
import net.wit.entity.summary.PayBillShopSummary;

/**
 * @ClassName: PayBillService
 * @author 降魔战队
 * @date 2017-11-4 18:12:28
 */

public interface PayBillService extends BaseService<PayBill, Long> {
	Page<PayBill> findPage(Date beginDate,Date endDate, Pageable pageable);
	Payment submit(PayBill payBill) throws Exception;
	Payment cardFill(PayBill payBill) throws Exception;
	Refunds cardRefund(PayBill payBill) throws Exception;
	//重收款创建退款
	PayBill createRefund(PayBill payBill,Admin admin) throws Exception;

	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @return Page<PayBill>
	 */
	List<PayBillShopSummary> sumPage(Shop shop,Date beginDate,Date endDate);

}