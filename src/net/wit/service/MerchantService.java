package net.wit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Merchant;

/**
 * @ClassName: MerchantService
 * @author 降魔战队
 * @date 2018-1-10 16:2:40
 */

public interface MerchantService extends BaseService<Merchant, Long> {
	Page<Merchant> findPage(Date beginDate,Date endDate, Pageable pageable);
}