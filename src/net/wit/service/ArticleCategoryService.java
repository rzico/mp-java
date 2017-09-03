package net.wit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.ArticleCategory;

/**
 * @ClassName: ArticleCategoryService
 * @author 降魔战队
 * @date 2017-9-3 20:35:56
 */

public interface ArticleCategoryService extends BaseService<ArticleCategory, Long> {

	Page<ArticleCategory> findPage(Date beginDate, Date endDate, Pageable pageable);

	/**
	 * 查找顶级文章分类
	 *
	 * @return 顶级文章分类
	 */
	List<ArticleCategory> findRoots();

	/**
	 * 查找顶级文章分类
	 *
	 * @param count
	 *            数量
	 * @return 顶级文章分类
	 */
	List<ArticleCategory> findRoots(Integer count);

	/**
	 * 查找顶级文章分类(缓存)
	 *
	 * @param count
	 *            数量
	 * @param cacheRegion
	 *            缓存区域
	 * @return 顶级文章分类(缓存)
	 */
	List<ArticleCategory> findRoots(Integer count, String cacheRegion);

	/**
	 * 查找上级文章分类
	 *
	 * @param articleCategory
	 *            文章分类
	 * @return 上级文章分类
	 */
	List<ArticleCategory> findParents(ArticleCategory articleCategory);

	/**
	 * 查找上级文章分类
	 *
	 * @param articleCategory
	 *            文章分类
	 * @param count
	 *            数量
	 * @return 上级文章分类
	 */
	List<ArticleCategory> findParents(ArticleCategory articleCategory, Integer count);

	/**
	 * 查找上级文章分类(缓存)
	 *
	 * @param articleCategory
	 *            文章分类
	 * @param count
	 *            数量
	 * @param cacheRegion
	 *            缓存区域
	 * @return 上级文章分类(缓存)
	 */
	List<ArticleCategory> findParents(ArticleCategory articleCategory, Integer count, String cacheRegion);

	/**
	 * 查找文章分类树
	 *
	 * @return 文章分类树
	 */
	List<ArticleCategory> findTree();

	/**
	 * 查找下级文章分类
	 *
	 * @param articleCategory
	 *            文章分类
	 * @return 下级文章分类
	 */
	List<ArticleCategory> findChildren(ArticleCategory articleCategory);

	/**
	 * 查找下级文章分类
	 *
	 * @param articleCategory
	 *            文章分类
	 * @param count
	 *            数量
	 * @return 下级文章分类
	 */
	List<ArticleCategory> findChildren(ArticleCategory articleCategory, Integer count);

	/**
	 * 查找下级文章分类(缓存)
	 *
	 * @param articleCategory
	 *            文章分类
	 * @param count
	 *            数量
	 * @param cacheRegion
	 *            缓存区域
	 * @return 下级文章分类(缓存)
	 */
	List<ArticleCategory> findChildren(ArticleCategory articleCategory, Integer count, String cacheRegion);

}