package net.wit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.ArticleCategory;

/**
 * @ClassName: ArticleCategoryService
 * @author 降魔战队
 * @date 2017-9-3 20:35:56
 */

public interface ArticleCategoryService extends BaseService<ArticleCategory, Long> {
	Page<ArticleCategory> findPage(Date beginDate, Date endDate, Pageable pageable);
}