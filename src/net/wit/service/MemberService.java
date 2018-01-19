package net.wit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Card;
import net.wit.entity.Member;
import net.wit.entity.Payment;
import net.wit.entity.Refunds;

/**
 * @ClassName: MemberService
 * @author 降魔战队
 * @date 2017-9-14 19:42:8
 */

public interface MemberService extends BaseService<Member, Long> {
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
	/**
	 * 根据设备号查找会员
	 * @param uuid 设备号(忽略大小写)
	 * @return 会员，若不存在则返回null
	 */
	Member findByUUID(String uuid);
	/**
	 * 判断会员是否登录
	 * @return 会员是否登录
	 */
	boolean isAuthenticated();

	/**
	 * 获取当前登录会员
	 * @return 当前登录会员，若不存在则返回null
	 */
	Member getCurrent();

	//支付插件专用方法
	public void payment(Member member, Payment payment) throws Exception;

	//支付插件专用方法
	public void refunds(Member member, Refunds refunds) throws Exception;


	//发展成员
	public void create(Member member, Member promoter) throws Exception;

}