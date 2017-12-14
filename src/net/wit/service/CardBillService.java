package net.wit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Card;
import net.wit.entity.CardBill;
import net.wit.entity.Payment;

/**
 * @ClassName: CardBillService
 * @author 降魔战队
 * @date 2017-11-4 18:12:27
 */

public interface CardBillService extends BaseService<CardBill, Long> {
	Page<CardBill> findPage(Date beginDate,Date endDate, Pageable pageable);

	public void fill(CardBill cardBill) throws Exception;
	public void refund(CardBill cardBill) throws Exception;
}