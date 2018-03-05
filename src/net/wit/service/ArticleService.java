package net.wit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Article;
import net.wit.entity.Member;
import net.wit.entity.Tag;

/**
 * @ClassName: ArticleService
 * @author 降魔战队
 * @date 2017-9-14 19:42:7
 */

public interface ArticleService extends BaseService<Article, Long> {
	Page<Article> findPage(Date beginDate, Date endDate, List<Tag> tags, Pageable pageable);
	Page<Article> findCircle(Member member,List<Tag> tags, Pageable pageable);
}