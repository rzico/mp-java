package net.wit.service;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.ArticleLaud;
import net.wit.entity.ArticleShare;

import java.util.Date;

/**
 * @ClassName: ArticleShareService
 * @author 降魔战队
 * @date 2017-9-14 19:42:7
 */

public interface ArticleShareService extends BaseService<ArticleShare, Long> {
	Page<ArticleShare> findPage(Date beginDate, Date endDate, Pageable pageable);
}