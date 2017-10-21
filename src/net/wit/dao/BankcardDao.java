package net.wit.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Bankcard;
import net.wit.entity.Member;


/**
 * @ClassName: BankcardDao
 * @author 降魔战队
 * @date 2017-10-20 17:56:0
 */
 

public interface BankcardDao extends BaseDao<Bankcard, Long> {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<Bankcard>
	 */
	Page<Bankcard> findPage(Date beginDate, Date endDate, Pageable pageable);

	Bankcard findDefault(Member member);
}