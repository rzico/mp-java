package net.wit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Recharge;
import net.wit.entity.Transfer;

/**
 * @ClassName: RechargeService
 * @author 降魔战队
 * @date 2018-2-1 14:1:27
 */

public interface RechargeService extends BaseService<Recharge, Long> {
	Page<Recharge> findPage(Date beginDate, Date endDate, Pageable pageable);
	/**
	 * 根据编号查找充值单
	 * @param sn 编号(忽略大小写)
	 * @return 充值单，若不存在则返回null
	 */
	Recharge findBySn(String sn);
	/**
	 * 申请提现
	 * @param recharge 充值单
	 */
	Boolean submit(Recharge recharge) throws Exception;
	/**
	 * 成功处理
	 * @param recharge 充值单
	 */
	void handle(Recharge recharge) throws Exception;
	/**
	 * 失败处理
	 * @param recharge 充值单
	 */
	void close(Recharge recharge) throws Exception;
}