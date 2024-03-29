package net.wit.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.ArticleCategory;
import net.wit.entity.ProductCategory;


/**
 * @ClassName: ProductCategoryDao
 * @author 降魔战队
 * @date 2017-9-14 19:42:5
 */
 

public interface ProductCategoryDao extends BaseDao<ProductCategory, Long> {
	/**
	 * 查找顶级文章分类
	 *
	 * @param count
	 *            数量
	 * @return 顶级文章分类
	 */
	List<ProductCategory> findRoots(Integer count);

	/**
	 * 查找上级文章分类
	 *
	 * @param productCategory
	 *            文章分类
	 * @param count
	 *            数量
	 * @return 上级文章分类
	 */
	List<ProductCategory> findParents(ProductCategory productCategory, Integer count);

	/**
	 * 查找下级文章分类
	 *
	 * @param productCategory
	 *            文章分类
	 * @param count
	 *            数量
	 * @return 下级文章分类
	 */
	List<ProductCategory> findChildren(ProductCategory productCategory, Integer count);

	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<ProductCategory>
	 */
	Page<ProductCategory> findPage(Date beginDate,Date endDate, Pageable pageable);
}