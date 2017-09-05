package net.wit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.ArticleCatalog;

/**
 * @ClassName: ArticleCatalogService
 * @author 降魔战队
 * @date 2017-9-3 20:35:56
 */

public interface ArticleCatalogService extends BaseService<ArticleCatalog, Long> {
	Page<ArticleCatalog> findPage(Date beginDate, Date endDate, Pageable pageable);
}