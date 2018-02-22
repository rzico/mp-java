package net.wit.dao.impl;

import java.util.*;

import javax.annotation.Resource;
import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.wit.dao.ProductDao;
import net.wit.dao.SnDao;
import net.wit.entity.Product;
import net.wit.entity.Sn;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.GoodsDao;
import net.wit.entity.Goods;


/**
 * @ClassName: GoodsDaoImpl
 * @author 降魔战队
 * @date 2017-9-14 19:42:5
 */
 

@Repository("goodsDaoImpl")
public class GoodsDaoImpl extends BaseDaoImpl<Goods, Long> implements GoodsDao {
	@Resource(name = "productDaoImpl")
	private ProductDao productDao;
	@Resource(name = "snDaoImpl")
	private SnDao snDao;

	/**
	 * 设置值并保存
	 *
	 * @param goods
	 *            货品
	 */
	@Override
	public void persist(Goods goods) {
		Assert.notNull(goods);

		if (goods.getProducts() != null) {
			for (Product product : goods.getProducts()) {
				setValue(product);
			}
		}
		super.persist(goods);
	}

	/**
	 * 设置值并更新
	 *
	 * @param goods
	 *            货品
	 * @return 货品
	 */
	@Override
	public Goods merge(Goods goods) {
		Assert.notNull(goods);

		if (goods.getProducts() != null) {
			for (Product product : goods.getProducts()) {
				setValue(product);
			}
		}
		return super.merge(goods);
	}

	/**
	 * 设置值
	 *
	 * @param product
	 *            商品
	 */
	private void setValue(Product product) {
		if (product == null) {
			return;
		}
		if (org.apache.commons.lang.StringUtils.isEmpty(product.getSn())) {
			String sn;
			do {
				sn = snDao.generate(Sn.Type.product);
			} while (productDao.snExists(product.getMember(),sn));
			product.setSn(sn);
		}
	}

	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<Goods>
	 */
	public Page<Goods> findPage(Date beginDate,Date endDate, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Goods> criteriaQuery = criteriaBuilder.createQuery(Goods.class);
		Root<Goods> root = criteriaQuery.from(Goods.class);
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
}