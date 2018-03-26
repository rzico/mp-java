package net.wit.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Gold;
import net.wit.entity.Member;
import net.wit.entity.summary.DepositSummary;
import net.wit.entity.summary.NihtanDepositSummary;


/**
 * @ClassName: GmGoldDao
 * @author 降魔战队
 * @date 2018-3-25 14:59:0
 */
 

public interface GoldDao extends BaseDao<Gold, Long> {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<GmGold>
	 */
	Page<Gold> findPage(Date beginDate, Date endDate, Pageable pageable);
	/**
	 */
	List<NihtanDepositSummary> sumPage(Member member, Date beginDate, Date endDate);
}