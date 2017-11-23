package net.wit.dao.impl;

import java.util.ArrayList;
import java.util.Calendar;

import java.util.Date;
import java.util.List;
import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.wit.entity.Article;
import net.wit.entity.summary.ArticleVoteSummary;
import net.wit.entity.summary.PayBillShopSummary;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.ArticleVoteDao;
import net.wit.entity.ArticleVote;


/**
 * @ClassName: ArticleVoteDaoImpl
 * @author 降魔战队
 * @date 2017-9-14 19:42:4
 */
 

@Repository("articleVoteDaoImpl")
public class ArticleVoteDaoImpl extends BaseDaoImpl<ArticleVote, Long> implements ArticleVoteDao {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<ArticleVote>
	 */
	public Page<ArticleVote> findPage(Date beginDate,Date endDate, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<ArticleVote> criteriaQuery = criteriaBuilder.createQuery(ArticleVote.class);
		Root<ArticleVote> root = criteriaQuery.from(ArticleVote.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.conjunction();
		if (beginDate!=null) {
			Date b = DateUtils.truncate(beginDate,Calendar.DATE);
			restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("createDate"), b));
		}
		if (endDate!=null) {
			Date e = DateUtils.truncate(endDate,Calendar.DATE);
			e =DateUtils.addDays(e,1);
			restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.lessThan(root.<Date> get("createDate"), e));
		}
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery,pageable);
	}

	public List<ArticleVoteSummary> sumPage(Article article) {
		String jpql =
				"select vote.title,vote.value,count(vote.id) "+
						"from ArticleVote vote where vote.article>=:article "+
						"group by vote.title,vote.value order by vote.title,vote.value";
		Query query = entityManager.createQuery(jpql).
				setFlushMode(FlushModeType.COMMIT).
				setParameter("article", article);
		List result = query.getResultList();
		List<ArticleVoteSummary> data = new ArrayList<>();
		for (int i=0;i<result.size();i++) {
			Object[] row = (Object[]) result.get(i);
			ArticleVoteSummary rw = new ArticleVoteSummary();
			rw.setTitle((String) row[1]);
			rw.setValue((String) row[3]);
			rw.setCount((Long) row[2]);
			data.add(rw);
		}
		return data;
	}

}