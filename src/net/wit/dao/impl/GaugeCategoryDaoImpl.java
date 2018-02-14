package net.wit.dao.impl;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.GaugeCategoryDao;
import net.wit.entity.GaugeCategory;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import javax.persistence.FlushModeType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;


/**
 * @ClassName: GaugeCategoryDaoImpl
 * @author 降魔战队
 * @date 2017-9-14 19:42:5
 */
 

@Repository("gaugeCategoryDaoImpl")
public class GaugeCategoryDaoImpl extends BaseDaoImpl<GaugeCategory, Long> implements GaugeCategoryDao {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<gaugeCategory>
	 */
	public Page<GaugeCategory> findPage(Date beginDate,Date endDate, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<GaugeCategory> criteriaQuery = criteriaBuilder.createQuery(GaugeCategory.class);
		Root<GaugeCategory> root = criteriaQuery.from(GaugeCategory.class);
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


	public List<GaugeCategory> findRoots(Integer count) {
		String jpql = "select gaugeCategory from GaugeCategory gaugeCategory where gaugeCategory.parent is null order by gaugeCategory.orders asc";
		TypedQuery<GaugeCategory> query = entityManager.createQuery(jpql, GaugeCategory.class).setFlushMode(FlushModeType.COMMIT);
		if (count != null) {
			query.setMaxResults(count);
		}
		return query.getResultList();
	}

	public List<GaugeCategory> findParents(GaugeCategory gaugeCategory, Integer count) {
		if (gaugeCategory == null || gaugeCategory.getParent() == null) {
			return Collections.<GaugeCategory> emptyList();
		}
		String jpql = "select gaugeCategory from GaugeCategory gaugeCategory where gaugeCategory.id in (:ids) order by gaugeCategory.grade asc";
		TypedQuery<GaugeCategory> query = entityManager.createQuery(jpql, GaugeCategory.class).setFlushMode(FlushModeType.COMMIT).setParameter("ids", gaugeCategory.getTreePaths());
		if (count != null) {
			query.setMaxResults(count);
		}
		return query.getResultList();
	}

	public List<GaugeCategory> findChildren(GaugeCategory gaugeCategory, Integer count) {
		TypedQuery<GaugeCategory> query;
		if (gaugeCategory != null) {
			String jpql = "select gaugeCategory from GaugeCategory gaugeCategory where gaugeCategory.treePath like :treePath order by gaugeCategory.orders asc";
			query = entityManager.createQuery(jpql, GaugeCategory.class).setFlushMode(FlushModeType.COMMIT).setParameter("treePath", "%" + GaugeCategory.TREE_PATH_SEPARATOR + gaugeCategory.getId() + GaugeCategory.TREE_PATH_SEPARATOR + "%");
		} else {
			String jpql = "select gaugeCategory from GaugeCategory gaugeCategory order by gaugeCategory.orders asc";
			query = entityManager.createQuery(jpql, GaugeCategory.class).setFlushMode(FlushModeType.COMMIT);
		}
		if (count != null) {
			query.setMaxResults(count);
		}
		return sort(query.getResultList(), gaugeCategory);
	}


	/**
	 * 设置treePath、grade并保存
	 *
	 * @param gaugeCategory
	 *            文章分类
	 */
	@Override
	public void persist(GaugeCategory gaugeCategory) {
		Assert.notNull(gaugeCategory);
		setValue(gaugeCategory);
		super.persist(gaugeCategory);
	}

	/**
	 * 设置treePath、grade并更新
	 *
	 * @param gaugeCategory
	 *            文章分类
	 * @return 文章分类
	 */
	@Override
	public GaugeCategory merge(GaugeCategory gaugeCategory) {
		Assert.notNull(gaugeCategory);
		setValue(gaugeCategory);
		for (GaugeCategory category : findChildren(gaugeCategory, null)) {
			setValue(category);
		}
		return super.merge(gaugeCategory);
	}

	/**
	 * 排序文章分类
	 *
	 * @param gaugeCategories
	 *            文章分类
	 * @param parent
	 *            上级文章分类
	 * @return 文章分类
	 */
	private List<GaugeCategory> sort(List<GaugeCategory> gaugeCategories, GaugeCategory parent) {
		List<GaugeCategory> result = new ArrayList<GaugeCategory>();
		if (gaugeCategories != null) {
			for (GaugeCategory gaugeCategory : gaugeCategories) {
				if ((gaugeCategory.getParent() != null && gaugeCategory.getParent().equals(parent)) || (gaugeCategory.getParent() == null && parent == null)) {
					result.add(gaugeCategory);
					result.addAll(sort(gaugeCategories, gaugeCategory));
				}
			}
		}
		return result;
	}

	/**
	 * 设置值
	 *
	 * @param gaugeCategory
	 *            文章分类
	 */
	private void setValue(GaugeCategory gaugeCategory) {
		if (gaugeCategory == null) {
			return;
		}
		GaugeCategory parent = gaugeCategory.getParent();
		if (parent != null) {
			gaugeCategory.setTreePath(parent.getTreePath() + parent.getId() + GaugeCategory.TREE_PATH_SEPARATOR);
		} else {
			gaugeCategory.setTreePath(GaugeCategory.TREE_PATH_SEPARATOR);
		}
		gaugeCategory.setGrade(gaugeCategory.getTreePaths().size());
	}


}