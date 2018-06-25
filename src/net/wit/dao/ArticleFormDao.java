package net.wit.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.ArticleForm;


/**
 * @ClassName: ArticleFormDao
 * @author 降魔战队
 * @date 2018-6-21 10:4:33
 */
 

public interface ArticleFormDao extends BaseDao<ArticleForm, Long> {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<ArticleForm>
	 */
	Page<ArticleForm> findPage(Date beginDate, Date endDate, Pageable pageable);
}