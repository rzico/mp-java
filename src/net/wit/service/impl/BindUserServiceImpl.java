package net.wit.service.impl;

import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.Resource;

import net.wit.dao.AreaDao;
import net.wit.dao.BindUserDao;
import net.wit.dao.MemberDao;
import net.wit.entity.Area;
import net.wit.entity.BindUser;
import net.wit.entity.BindUser.Type;
import net.wit.entity.Member;
import net.wit.service.BindUserService;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

/**
 * Service - 绑定登录
 * 
 * @author mayt
 * @version 3.0
 */
@Service("bindUserServiceImpl")
public class BindUserServiceImpl extends BaseServiceImpl<BindUser, String> implements BindUserService {

	
	@Resource(name = "bindUserDaoImpl")
	private BindUserDao bindUserDao;

	@Resource(name = "bindUserDaoImpl")
	public void setBaseDao(BindUserDao bindUserDao) {
		super.setBaseDao(bindUserDao);
	}

	public BindUser findOpenId(String openId,String appId,Type type) {
		return bindUserDao.findOpenId(openId,appId,type);
	}
	public BindUser findMember(Member member, String appId, Type type) {
		return bindUserDao.findMember(member,appId,type);
	}
}
