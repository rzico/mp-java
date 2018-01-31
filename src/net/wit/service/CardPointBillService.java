package net.wit.service;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.CardPointBill;

import java.util.Date;

/**
 * @ClassName: CardPointBillService
 * @author 降魔战队
 * @date 2017-11-4 18:12:27
 */

public interface CardPointBillService extends BaseService<CardPointBill, Long> {
	Page<CardPointBill> findPage(Date beginDate, Date endDate, Pageable pageable);
}