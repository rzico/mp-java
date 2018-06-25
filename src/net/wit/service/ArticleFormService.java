package net.wit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.ArticleForm;

/**
 * @ClassName: ArticleFormService
 * @author 降魔战队
 * @date 2018-6-21 10:4:39
 */

public interface ArticleFormService extends BaseService<ArticleForm, Long> {
	Page<ArticleForm> findPage(Date beginDate, Date endDate, Pageable pageable);
}