package net.wit.dao.impl;

import java.util.*;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.ArticleCategoryDao;
import net.wit.entity.ArticleCategory;


/**
 * @ClassName: ArticleCategoryDaoImpl
 * @author 降魔战队
 * @date 2017-9-14 19:42:4
 */
 

@Repository("articleCategoryDaoImpl")
public class ArticleCategoryDaoImpl extends BaseDaoImpl<ArticleCategory, Long> implements ArticleCategoryDao {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<ArticleCategory>
	 */
	public Page<ArticleCategory> findPage(Date beginDate,Date endDate, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<ArticleCategory> criteriaQuery = criteriaBuilder.createQuery(ArticleCategory.class);
		Root<ArticleCategory> root = criteriaQuery.from(ArticleCategory.class);
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

	public List<ArticleCategory> findRoots(Integer count) {
		String jpql = "select articleCategory from ArticleCategory articleCategory where articleCategory.parent is null order by articleCategory.order asc";
		TypedQuery<ArticleCategory> query = entityManager.createQuery(jpql, ArticleCategory.class).setFlushMode(FlushModeType.COMMIT);
		if (count != null) {
			query.setMaxResults(count);
		}
		return query.getResultList();
	}

	public List<ArticleCategory> findParents(ArticleCategory articleCategory, Integer count) {
		if (articleCategory == null || articleCategory.getParent() == null) {
			return Collections.<ArticleCategory> emptyList();
		}
		String jpql = "select articleCategory from ArticleCategory articleCategory where articleCategory.id in (:ids) order by articleCategory.grade asc";
		TypedQuery<ArticleCategory> query = entityManager.createQuery(jpql, ArticleCategory.class).setFlushMode(FlushModeType.COMMIT).setParameter("ids", articleCategory.getTreePaths());
		if (count != null) {
			query.setMaxResults(count);
		}
		return query.getResultList();
	}

	public List<ArticleCategory> findChildren(ArticleCategory articleCategory, Integer count) {
		TypedQuery<ArticleCategory> query;
		if (articleCategory != null) {
			String jpql = "select articleCategory from ArticleCategory articleCategory where articleCategory.treePath like :treePath order by articleCategory.orders asc";
			query = entityManager.createQuery(jpql, ArticleCategory.class).setFlushMode(FlushModeType.COMMIT).setParameter("treePath", "%" + ArticleCategory.TREE_PATH_SEPARATOR + articleCategory.getId() + ArticleCategory.TREE_PATH_SEPARATOR + "%");
		} else {
			String jpql = "select articleCategory from ArticleCategory articleCategory order by articleCategory.orders asc";
			query = entityManager.createQuery(jpql, ArticleCategory.class).setFlushMode(FlushModeType.COMMIT);
		}
		if (count != null) {
			query.setMaxResults(count);
		}
		return sort(query.getResultList(), articleCategory);
	}


	/**
	 * 设置treePath、grade并保存
	 *
	 * @param articleCategory
	 *            文章分类
	 */
	@Override
	public void persist(ArticleCategory articleCategory) {
		Assert.notNull(articleCategory);
		setValue(articleCategory);
		super.persist(articleCategory);
	}

	/**
	 * 设置treePath、grade并更新
	 *
	 * @param articleCategory
	 *            文章分类
	 * @return 文章分类
	 */
	@Override
	public ArticleCategory merge(ArticleCategory articleCategory) {
		Assert.notNull(articleCategory);
		setValue(articleCategory);
		for (ArticleCategory category : findChildren(articleCategory, null)) {
			setValue(category);
		}
		return super.merge(articleCategory);
	}

	/**
	 * 排序文章分类
	 *
	 * @param articleCategories
	 *            文章分类
	 * @param parent
	 *            上级文章分类
	 * @return 文章分类
	 */
	private List<ArticleCategory> sort(List<ArticleCategory> articleCategories, ArticleCategory parent) {
		List<ArticleCategory> result = new ArrayList<ArticleCategory>();
		if (articleCategories != null) {
			for (ArticleCategory articleCategory : articleCategories) {
				if ((articleCategory.getParent() != null && articleCategory.getParent().equals(parent)) || (articleCategory.getParent() == null && parent == null)) {
					result.add(articleCategory);
					result.addAll(sort(articleCategories, articleCategory));
				}
			}
		}
		return result;
	}

	/**
	 * 设置值
	 *
	 * @param articleCategory
	 *            文章分类
	 */
	private void setValue(ArticleCategory articleCategory) {
		if (articleCategory == null) {
			return;
		}
		ArticleCategory parent = articleCategory.getParent();
		if (parent != null) {
			articleCategory.setTreePath(parent.getTreePath() + parent.getId() + ArticleCategory.TREE_PATH_SEPARATOR);
		} else {
			articleCategory.setTreePath(ArticleCategory.TREE_PATH_SEPARATOR);
		}
		articleCategory.setGrade(articleCategory.getTreePaths().size());
	}


}