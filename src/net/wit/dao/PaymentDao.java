/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.*;
import net.wit.entity.Payment.Method;
import net.wit.entity.Payment.Status;

/**
 * Dao - 收款单
 * @author rsico Team
 * @version 3.0
 */
public interface PaymentDao extends BaseDao<Payment, Long> {

	/**
	 * 根据编号查找收款单
	 * @param sn 编号(忽略大小写)
	 * @return 收款单，若不存在则返回null
	 */
	Payment findBySn(String sn);


	/**
	 * @Title：findPage
	 * @Description：
	 * @param type
	 * @param pageable
	 * @return Page<Payment>
	 */
	Page<Payment> findPage(Member member, Pageable pageable, Payment.Type type);

}