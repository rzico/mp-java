package net.wit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Member;
import net.wit.entity.Product;
import net.wit.entity.Tag;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ClassName: ProductService
 * @author 降魔战队
 * @date 2017-9-14 19:42:8
 */

public interface ProductService extends BaseService<Product, Long> {

	/**
	 * 判断商品编号是否存在
	 *
	 * @param sn
	 *            商品编号(忽略大小写)
	 * @return 商品编号是否存在
	 */
	boolean snExists(Member member , String sn);

	/**
	 * 根据商品编号查找商品
	 *
	 * @param sn
	 *            商品编号(忽略大小写)
	 * @return 商品，若不存在则返回null
	 */
	Product findBySn(Member member ,String sn);

	/**
	 * 判断商品编号是否唯一
	 *
	 * @param previousSn
	 *            修改前商品编号(忽略大小写)
	 * @param currentSn
	 *            当前商品编号(忽略大小写)
	 * @return 商品编号是否唯一
	 */
	boolean snUnique(Member member ,String previousSn, String currentSn);
	Page<Product> findPage(Date beginDate, Date endDate, Tag tag, Pageable pageable);
}