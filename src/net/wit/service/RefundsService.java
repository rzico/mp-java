package net.wit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.*;

import javax.persistence.LockModeType;
import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName: RefundsService
 * @author 降魔战队
 * @date 2017-9-14 19:42:9
 */

public interface RefundsService extends BaseService<Refunds, Long> {
	Page<Refunds> findPage(Date beginDate,Date endDate, Pageable pageable);
	/**
	 * 根据编号查找收款单
	 * @param sn 编号(忽略大小写)
	 * @return 收款单，若不存在则返回null
	 */
	Refunds findBySn(String sn);

	/**
	 * 支付处理
	 * @param refunds 收款单
	 */
	void handle(Refunds refunds) throws Exception;

	/**
	 * 提交处理
	 * @param refunds 退款单
	 */
	Boolean refunds(Refunds refunds, HttpServletRequest request) throws Exception;
	void close(Refunds refunds) throws Exception;


	/**
	 * 查询状态
	 */
	void query(Long id);

}