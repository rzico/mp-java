package net.wit.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Member;


/**
 * @ClassName: MemberDao
 * @author 降魔战队
 * @date 2017-9-3 21:54:59
 */
 

public interface MemberDao extends BaseDao<Member, Long> {
	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<Member>
	 */
	Page<Member> findPage(Date beginDate, Date endDate, Pageable pageable);
}