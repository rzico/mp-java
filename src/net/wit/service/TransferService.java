package net.wit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Payment;
import net.wit.entity.Transfer;

/**
 * @ClassName: TransferService
 * @author 降魔战队
 * @date 2017-10-17 19:48:19
 */

public interface TransferService extends BaseService<Transfer, Long> {
	Page<Transfer> findPage(Date beginDate, Date endDate, Pageable pageable);
	/**
	 * 根据编号查找转账单
	 * @param sn 编号(忽略大小写)
	 * @return 转账单，若不存在则返回null
	 */
	Transfer findBySn(String sn);
	/**
	 * 根据编号查找转账单
	 * @param sn 编号(忽略大小写)
	 * @return 转账单，若不存在则返回null
	 */
	Transfer findByOrderSn(String sn);
	/**
	 * 申请提现
	 * @param transfer 转账单
	 */
	Boolean submit(Transfer transfer) throws Exception;
	/**
	 * 提交处理
	 * @param transfer 转账单
	 */
	Boolean transfer(Transfer transfer) throws Exception;
	/**
	 * 成功处理
	 * @param transfer 转账单
	 */
	void handle(Transfer transfer) throws Exception;
	/**
	 * 失败处理
	 * @param transfer 转账单
	 */
	void refunds(Transfer transfer) throws Exception;
}