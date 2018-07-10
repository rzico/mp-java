package net.wit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Payment;
import net.wit.entity.RedPackage;

/**
 * @ClassName: RedPackageService
 * @author 降魔战队
 * @date 2018-7-6 10:38:17
 */

public interface RedPackageService extends BaseService<RedPackage, Long> {
	Page<RedPackage> findPage(Date beginDate, Date endDate, Pageable pageable);
	public Payment sendRedPackage(RedPackage redPackage);
	public double getRedPackage(RedPackage redPackage);
}