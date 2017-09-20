package net.wit.service;

import net.wit.entity.BindUser;
import net.wit.entity.BindUser.Type;
import net.wit.entity.Member;

/**
 * Service - 绑定登录
 * 
 * @author mayt
 * @version 3.0
 */
public interface BindUserService extends BaseService<BindUser,String>{

	/**
	 * 根据用户名查找绑定登录会员
	 * 
	 * @param openId
	 *            用户名(忽略大小写)
	 * @return 绑定登录会员，若不存在则返回null
	 */
	BindUser findOpenId(String openId,String appId, Type type);

	BindUser findUnionId(String unionId,Type type);
	/**
	 * 根据用户名查找绑定登录会员
	 *
	 * @param member
	 *            用户名(忽略大小写)
	 * @return 绑定登录会员，若不存在则返回null
	 */
	BindUser findMember(Member member, String appId, Type type);
}
