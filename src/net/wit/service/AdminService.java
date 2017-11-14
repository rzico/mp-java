package net.wit.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Admin;
import net.wit.entity.Member;

/**
 * @ClassName: AdminService
 * @author 降魔战队
 * @date 2017-9-14 19:42:7
 */

public interface AdminService extends BaseService<Admin, Long> {

	/**
	 * 判断用户名是否存在
	 *
	 * @param username
	 *            用户名(忽略大小写)
	 * @return 用户名是否存在
	 */
	boolean usernameExists(String username);

	/**
	 * 根据用户名查找管理员
	 *
	 * @param username
	 *            用户名(忽略大小写)
	 * @return 管理员，若不存在则返回null
	 */
	Admin findByUsername(String username);

	/**
	 * 根据会员查找管理员
	 *
	 * @param member
	 *            用户名(忽略大小写)
	 * @return 管理员，若不存在则返回null
	 */
	Admin findByMember(Member member);

	/**
	 * 根据ID查找权限
	 *
	 * @param id
	 *            ID
	 * @return 权限,若不存在则返回null
	 */
	List<String> findAuthorities(Long id);

	/**
	 * 判断管理员是否登录
	 *
	 * @return 管理员是否登录
	 */
	boolean isAuthenticated();

	/**
	 * 获取当前登录管理员
	 *
	 * @return 当前登录管理员,若不存在则返回null
	 */
	Admin getCurrent();

	/**
	 * 获取当前登录用户名
	 *
	 * @return 当前登录用户名,若不存在则返回null
	 */
	String getCurrentUsername();

	/**
	 * @Title：findPage
	 * @Description：标准代码
	 * @param beginDate
	 * @param endDate
	 * @param pageable
	 * @return Page<Admin>
	 */
	Page<Admin> findPage(Date beginDate,Date endDate, Pageable pageable);
}