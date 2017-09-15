package net.wit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Product;

/**
 * @ClassName: ProductService
 * @author 降魔战队
 * @date 2017-9-14 19:42:8
 */

public interface ProductService extends BaseService<Product, Long> {
	Page<Product> findPage(Date beginDate,Date endDate, Pageable pageable);
}