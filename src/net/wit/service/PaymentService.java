package net.wit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Payment;

/**
 * @ClassName: PaymentService
 * @author 降魔战队
 * @date 2017-9-14 19:42:8
 */

public interface PaymentService extends BaseService<Payment, Long> {

	/**
	 * 根据编号查找收款单
	 * @param sn 编号(忽略大小写)
	 * @return 收款单，若不存在则返回null
	 */
	Payment findBySn(String sn);

	/**
	 * 支付处理
	 * @param payment 收款单
	 */
	void handle(Payment payment) throws Exception;


	Page<Payment> findPage(Date beginDate, Date endDate, Pageable pageable);


	/**
	 * 支付关闭
	 * @param payment 收款单
	 */
	void close(Payment payment) throws Exception;

	/**
	 * 查询状态
	 */
	void query();

}