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
 * @date 2017-9-14 19:42:5
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
	Page<Member> findPage(Date beginDate,Date endDate, Pageable pageable);

	/**
	 * 根据用户名查找会员
	 * @param username 用户名(忽略大小写)
	 * @return 会员，若不存在则返回null
	 */
	Member findByUsername(String username);
	/**
	 * 根据手机号查找会员
	 * @param mobile 用户名(忽略大小写)
	 * @return 会员，若不存在则返回null
	 */
	Member findByMobile(String mobile);
}