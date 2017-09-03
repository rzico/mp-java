package net.wit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.ArticleFavorite;

/**
 * @ClassName: ArticleFavoriteService
 * @author 降魔战队
 * @date 2017-9-3 20:35:56
 */

public interface ArticleFavoriteService extends BaseService<ArticleFavorite, Long> {
	Page<ArticleFavorite> findPage(Date beginDate, Date endDate, Pageable pageable);
}