package net.wit.dao.impl;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.AgentCategoryDao;
import net.wit.entity.AgentCategory;
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
 * @ClassName: AgentCategoryDaoImpl
 * @author 降魔战队
 * @date 2017-9-14 19:42:5
 */
 

@Repository("agentCategoryDaoImpl")
public class AgentCategoryDaoImpl extends BaseDaoImpl<AgentCategory, Long> implements AgentCategoryDao {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<gaugeCategory>
	 */
	public Page<AgentCategory> findPage(Date beginDate,Date endDate, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<AgentCategory> criteriaQuery = criteriaBuilder.createQuery(AgentCategory.class);
		Root<AgentCategory> root = criteriaQuery.from(AgentCategory.class);
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


	public List<AgentCategory> findRoots(Integer count) {
		String jpql = "select gaugeCategory from AgentCategory gaugeCategory where gaugeCategory.parent is null order by gaugeCategory.orders asc";
		TypedQuery<AgentCategory> query = entityManager.createQuery(jpql, AgentCategory.class).setFlushMode(FlushModeType.COMMIT);
		if (count != null) {
			query.setMaxResults(count);
		}
		return query.getResultList();
	}

	public List<AgentCategory> findParents(AgentCategory gaugeCategory, Integer count) {
		if (gaugeCategory == null || gaugeCategory.getParent() == null) {
			return Collections.<AgentCategory> emptyList();
		}
		String jpql = "select gaugeCategory from AgentCategory gaugeCategory where gaugeCategory.id in (:ids) order by gaugeCategory.grade asc";
		TypedQuery<AgentCategory> query = entityManager.createQuery(jpql, AgentCategory.class).setFlushMode(FlushModeType.COMMIT).setParameter("ids", gaugeCategory.getTreePaths());
		if (count != null) {
			query.setMaxResults(count);
		}
		return query.getResultList();
	}

	public List<AgentCategory> findChildren(AgentCategory gaugeCategory, Integer count) {
		TypedQuery<AgentCategory> query;
		if (gaugeCategory != null) {
			String jpql = "select gaugeCategory from AgentCategory gaugeCategory where gaugeCategory.treePath like :treePath order by gaugeCategory.orders asc";
			query = entityManager.createQuery(jpql, AgentCategory.class).setFlushMode(FlushModeType.COMMIT).setParameter("treePath", "%" + AgentCategory.TREE_PATH_SEPARATOR + gaugeCategory.getId() + AgentCategory.TREE_PATH_SEPARATOR + "%");
		} else {
			String jpql = "select gaugeCategory from AgentCategory gaugeCategory order by gaugeCategory.orders asc";
			query = entityManager.createQuery(jpql, AgentCategory.class).setFlushMode(FlushModeType.COMMIT);
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
	public void persist(AgentCategory gaugeCategory) {
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
	public AgentCategory merge(AgentCategory gaugeCategory) {
		Assert.notNull(gaugeCategory);
		setValue(gaugeCategory);
		for (AgentCategory category : findChildren(gaugeCategory, null)) {
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
	private List<AgentCategory> sort(List<AgentCategory> gaugeCategories, AgentCategory parent) {
		List<AgentCategory> result = new ArrayList<AgentCategory>();
		if (gaugeCategories != null) {
			for (AgentCategory gaugeCategory : gaugeCategories) {
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
	private void setValue(AgentCategory gaugeCategory) {
		if (gaugeCategory == null) {
			return;
		}
		AgentCategory parent = gaugeCategory.getParent();
		if (parent != null) {
			gaugeCategory.setTreePath(parent.getTreePath() + parent.getId() + AgentCategory.TREE_PATH_SEPARATOR);
		} else {
			gaugeCategory.setTreePath(AgentCategory.TREE_PATH_SEPARATOR);
		}
		gaugeCategory.setGrade(gaugeCategory.getTreePaths().size());
	}


}