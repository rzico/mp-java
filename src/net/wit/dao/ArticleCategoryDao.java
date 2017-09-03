package net.wit.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.ArticleCategory;


/**
 * @ClassName: ArticleCategoryDao
 * @author 降魔战队
 * @date 2017-9-3 21:54:58
 */
 

public interface ArticleCategoryDao extends BaseDao<ArticleCategory, Long> {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<ArticleCategory>
	 */
	Page<ArticleCategory> findPage(Date beginDate, Date endDate, Pageable pageable);
}