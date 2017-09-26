package net.wit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Friends;
import net.wit.entity.Member;

/**
 * @ClassName: FriendsService
 * @author 降魔战队
 * @date 2017-9-14 19:42:8
 */

public interface FriendsService extends BaseService<Friends, Long> {
	Page<Friends> findPage(Date beginDate,Date endDate, Pageable pageable);
	/**
	 * @Title：find
	 * @Description：标准代码
	 * @param member
	 * @param friend
	 * @return Friends
	 */
	Friends find(Member member, Member friend);
}