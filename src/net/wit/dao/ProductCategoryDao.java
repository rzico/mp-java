package net.wit.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.ProductCategory;


/**
 * @ClassName: ProductCategoryDao
 * @author 降魔战队
 * @date 2017-9-14 19:42:5
 */
 

public interface ProductCategoryDao extends BaseDao<ProductCategory, Long> {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<ProductCategory>
	 */
	Page<ProductCategory> findPage(Date beginDate,Date endDate, Pageable pageable);
}