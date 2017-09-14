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
	Page<Payment> findPage(Date beginDate,Date endDate, Pageable pageable);
}