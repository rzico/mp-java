package net.wit.dao.impl;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;

import net.wit.dao.BindUserDao;
import net.wit.entity.BindUser;
import net.wit.entity.BindUser.Type;
import net.wit.entity.Member;

import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Dao - 绑定登录
 * @author mayt
 * @version 3.0
 */
@Repository("bindUserDaoImpl")
public class BindUserDaoImpl extends BaseDaoImpl<BindUser, String> implements BindUserDao {

	public BindUser findOpenId(String openId,String appId, Type type) {
		if (openId == null) {
			return null;
		}
		try {
			String jpql = null;
			jpql = "select bindUsers from BindUser bindUsers where bindUsers.openId = :openId and bindUsers.appId=:appId and bindUsers.type=:type";

			return entityManager.createQuery(jpql, BindUser.class).setFlushMode(FlushModeType.COMMIT).setParameter("openId", openId).setParameter("appId", appId).setParameter("type", type).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	public BindUser findMember(Member member,String appId, Type type) {
		if (member == null) {
			return null;
		}
		try {
			String jpql = null;
			jpql = "select bindUsers from BindUser bindUsers where bindUsers.member = :member and bindUsers.appId=:appId and bindUsers.type=:type";

			return entityManager.createQuery(jpql, BindUser.class).setFlushMode(FlushModeType.COMMIT).setParameter("member", member).setParameter("appId", appId).setParameter("type", type).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
}
