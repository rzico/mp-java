package net.wit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Shop;

/**
 * @ClassName: ShopService
 * @author 降魔战队
 * @date 2017-11-4 18:12:28
 */

public interface ShopService extends BaseService<Shop, Long> {
	Page<Shop> findPage(Date beginDate,Date endDate, Pageable pageable);
	Shop find(String code);
}