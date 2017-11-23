package net.wit.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Article;
import net.wit.entity.ArticleVote;
import net.wit.entity.summary.ArticleVoteSummary;


/**
 * @ClassName: ArticleVoteDao
 * @author 降魔战队
 * @date 2017-9-14 19:42:4
 */
 

public interface ArticleVoteDao extends BaseDao<ArticleVote, Long> {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<ArticleVote>
	 */
	Page<ArticleVote> findPage(Date beginDate,Date endDate, Pageable pageable);
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param article
	 * @return Page<ArticleVoteSummary>
	 */
	List<ArticleVoteSummary> sumPage(Article article);
}