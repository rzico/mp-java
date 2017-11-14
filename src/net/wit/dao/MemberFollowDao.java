package net.wit.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Member;
import net.wit.entity.MemberFollow;


/**
 * @ClassName: MemberFollowDao
 * @author 降魔战队
 * @date 2017-9-14 19:42:5
 */
 

public interface MemberFollowDao extends BaseDao<MemberFollow, Long> {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<MemberFollow>
	 */
	Page<MemberFollow> findPage(Date beginDate,Date endDate, Pageable pageable);
	MemberFollow find(Member member, Member follow);
}