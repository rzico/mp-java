package net.wit.dao.impl;

import java.util.ArrayList;
import java.util.Calendar;

import java.util.Date;
import java.util.List;
import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.wit.entity.ArticleCategory;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.AreaDao;
import net.wit.entity.Area;


/**
 * @ClassName: AreaDaoImpl
 * @author 降魔战队
 * @date 2017-9-14 19:42:4
 */
 

@Repository("areaDaoImpl")
public class AreaDaoImpl extends BaseDaoImpl<Area, Long> implements AreaDao {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<Area>
	 */
	public Page<Area> findPage(Date beginDate,Date endDate, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Area> criteriaQuery = criteriaBuilder.createQuery(Area.class);
		Root<Area> root = criteriaQuery.from(Area.class);
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
	public List<Area> findRoots(Integer count) {
		String jpql = "select area from Area area where area.parent is null order by area.orders asc";
		TypedQuery<Area> query = entityManager.createQuery(jpql, Area.class).setFlushMode(FlushModeType.COMMIT);
		if (count != null) {
			query.setMaxResults(count);
		}
		return query.getResultList();
	}
	/**
	 * 设置treePath、grade并保存
	 *
	 * @param area
	 *            文章分类
	 */
	@Override
	public void persist(Area area) {
		Assert.notNull(area);
		setValue(area);
		super.persist(area);
	}

	public List<Area> findChildren(Area area, Integer count) {
		TypedQuery<Area> query;
		if (area != null) {
			String jpql = "select area from Area area where area.treePath like :treePath order by area.orders asc";
			query = entityManager.createQuery(jpql, Area.class).setFlushMode(FlushModeType.COMMIT).setParameter("treePath", "%" + Area.TREE_PATH_SEPARATOR + area.getId() + Area.TREE_PATH_SEPARATOR + "%");
		} else {
			String jpql = "select area from Area area order by area.orders asc";
			query = entityManager.createQuery(jpql, Area.class).setFlushMode(FlushModeType.COMMIT);
		}
		if (count != null) {
			query.setMaxResults(count);
		}
		return sort(query.getResultList(), area);
	}

	/**
	 * 设置treePath、grade并更新
	 *
	 * @param area
	 *            文章分类
	 * @return 文章分类
	 */
	@Override
	public Area merge(Area area) {
		Assert.notNull(area);
		setValue(area);
		for (Area child : findChildren(area, null)) {
			setValue(child);
		}
		return super.merge(area);
	}
	/**
	 * 排序文章分类
	 *
	 * @param areas
	 *            文章分类
	 * @param parent
	 *            上级文章分类
	 * @return 文章分类
	 */
	private List<Area> sort(List<Area> areas, Area parent) {
		List<Area> result = new ArrayList<Area>();
		if (areas != null) {
			for (Area area : areas) {
				if ((area.getParent() != null && area.getParent().equals(parent)) || (area.getParent() == null && parent == null)) {
					result.add(area);
					result.addAll(sort(areas, area));
				}
			}
		}
		return result;
	}
	/**
	 * 设置值
	 *
	 * @param area
	 *            文章分类
	 */
	private void setValue(Area area) {
		if (area == null) {
			return;
		}
		Area parent = area.getParent();
		if (parent != null) {
			area.setTreePath(parent.getTreePath() + parent.getId() + Area.TREE_PATH_SEPARATOR);
		} else {
			area.setTreePath(ArticleCategory.TREE_PATH_SEPARATOR);
		}
		if (parent != null) {
			area.setFullName(parent.getFullName() + area.getName());
		} else {
			area.setFullName(area.getName());
		}
	}
}