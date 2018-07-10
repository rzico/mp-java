package net.wit.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.RedPackage;


/**
 * @ClassName: RedPackageDao
 * @author 降魔战队
 * @date 2018-7-6 10:38:11
 */
 

public interface RedPackageDao extends BaseDao<RedPackage, Long> {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<RedPackage>
	 */
	Page<RedPackage> findPage(Date beginDate, Date endDate, Pageable pageable);
	public List<RedPackage> findByRedPackage(RedPackage redPackage);
}