package net.wit.service;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.GaugeCategory;

import java.util.Date;

/**
 * @ClassName: ProductCategoryService
 * @author 降魔战队
 * @date 2017-9-14 19:42:8
 */

public interface GaugeCategoryService extends BaseService<GaugeCategory, Long> {
	Page<GaugeCategory> findPage(Date beginDate, Date endDate, Pageable pageable);
}