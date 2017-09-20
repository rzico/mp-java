package net.wit.dao;

import net.wit.entity.BindUser;
import net.wit.entity.BindUser.Type;
import net.wit.entity.Member;


/**
 * Dao - 绑定登录
 * 
 * @author mayt
 * @version 3.0
 */
public interface BindUserDao extends BaseDao<BindUser,String> {

	BindUser findOpenId(String openId,String appId,Type type);
	BindUser findUnionId(String openId,Type type);
	BindUser findMember(Member member,String appId,Type type);
}
