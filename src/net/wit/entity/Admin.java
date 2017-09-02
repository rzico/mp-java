package net.wit.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Entity - 管理员
 *
 * @author 降魔战队
 * @date 2017/2/13 19:00:18
 */
@Entity
@Table(name = "xm_admin")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xm_admin_sequence")
public class Admin extends BaseEntity {

	private static final long serialVersionUID = 101L;

	/** 用户名 */
	@NotEmpty(groups = Save.class)
	@Pattern(regexp = "^[0-9a-z_A-Z\\u4e00-\\u9fa5]+$")
	@Length(min = 2, max = 20)
	@Column(columnDefinition="varchar(255) not null unique comment '用户名'")
	private String username;

	/** 密码 */
	@NotEmpty(groups = Save.class)
	@Pattern(regexp = "^[^\\s&\"<>]+$")
	@Length(min = 4, max = 20)
	@Column(columnDefinition="varchar(255) not null comment '密码'")
	private String password;

	/** E-mail */
	@NotEmpty
	@Email
	@Length(max = 200)
	@Column(columnDefinition="varchar(255) comment '邮箱'")
	private String email;

	/** 姓名 */
	@Length(max = 200)
	@Column(columnDefinition="varchar(255) comment '真实姓名'")
	private String name;

	/** 部门 */
	@Length(max = 200)
	@Column(columnDefinition="varchar(255) comment '部门'")
	private String department;

	/** 是否启用 */
	@NotNull
	@Column(columnDefinition="bit not null comment '是否启用'")
	private Boolean isEnabled;

	/** 是否锁定 */
	@Column(columnDefinition="bit not null comment '是否锁定'")
	private Boolean isLocked;

	/** 连续登录失败次数 */
	@Column(columnDefinition="int(11) not null comment '登录失败次数'")
	private Integer loginFailureCount;

	/** 锁定日期 */
	@Column(columnDefinition="datetime not null comment '锁定日期'")
	private Date lockedDate;

	/** 最后登录日期 */
	@Column(columnDefinition="datetime not null comment '最后登录日期'")
	private Date loginDate;

	/** 最后登录IP */
	@Column(columnDefinition="varchar(255) comment '最后登录IP'")
	private String loginIp;

	/** 角色 */
	@NotEmpty
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "xm_admin_role")
	private Set<Role> roles = new HashSet<Role>();

	/**
	 * 删除前处理
	 */
	@PreRemove
	public void preRemove() {
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public Boolean getEnabled() {
		return isEnabled;
	}

	public void setEnabled(Boolean enabled) {
		isEnabled = enabled;
	}

	public Boolean getLocked() {
		return isLocked;
	}

	public void setLocked(Boolean locked) {
		isLocked = locked;
	}

	public Integer getLoginFailureCount() {
		return loginFailureCount;
	}

	public void setLoginFailureCount(Integer loginFailureCount) {
		this.loginFailureCount = loginFailureCount;
	}

	public Date getLockedDate() {
		return lockedDate;
	}

	public void setLockedDate(Date lockedDate) {
		this.lockedDate = lockedDate;
	}

	public Date getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}

	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
}