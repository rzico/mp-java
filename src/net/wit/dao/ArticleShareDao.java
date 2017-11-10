package net.wit.dao;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.ArticleShare;

import java.util.Date;


/**
 * @ClassName: ArticleShareDao
 * @author 降魔战队
 * @date 2017-9-14 19:42:4
 */
 

public interface ArticleShareDao extends BaseDao<ArticleShare, Long> {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<ArticleShare>
	 */
	Page<ArticleShare> findPage(Date beginDate, Date endDate, Pageable pageable);
}