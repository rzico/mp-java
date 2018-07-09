package net.wit.dao;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.GaugeCategory;

import java.util.Date;
import java.util.List;


/**
 * @ClassName: GaugeCategoryDao
 * @author 降魔战队
 * @date 2017-9-14 19:42:5
 */
 

public interface GaugeCategoryDao extends BaseDao<GaugeCategory, Long> {
	/**
	 * 查找顶级文章分类
	 *
	 * @param count
	 *            数量
	 * @return 顶级文章分类
	 */
	List<GaugeCategory> findRoots(Integer count);

	/**
	 * 查找上级文章分类
	 *
	 * @param gaugeCategory
	 *            文章分类
	 * @param count
	 *            数量
	 * @return 上级文章分类
	 */
	List<GaugeCategory> findParents(GaugeCategory gaugeCategory, Integer count);

	/**
	 * 查找下级文章分类
	 *
	 * @param gaugeCategory
	 *            文章分类
	 * @param count
	 *            数量
	 * @return 下级文章分类
	 */
	List<GaugeCategory> findChildren(GaugeCategory gaugeCategory, Integer count);

	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<GaugeCategory>
	 */
	Page<GaugeCategory> findPage(Date beginDate, Date endDate, Pageable pageable);
}