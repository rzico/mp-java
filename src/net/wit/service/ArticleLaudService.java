package net.wit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.ArticleLaud;

/**
 * @ClassName: ArticleLaudService
 * @author 降魔战队
 * @date 2017-9-14 19:42:7
 */

public interface ArticleLaudService extends BaseService<ArticleLaud, Long> {
	Page<ArticleLaud> findPage(Date beginDate,Date endDate, Pageable pageable);
}