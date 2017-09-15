
package net.wit.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Entity - 角色
 * @author 降魔战队
 * @date 2017/2/13 19:00:18
 */
@Entity
@Table(name = "wx_role")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_role_sequence")
public class Role extends BaseEntity {

	private static final long serialVersionUID = 120L;

	/** 名称 */
	@NotEmpty
	@Length(max = 200)
	@Column(columnDefinition="varchar(255) not null comment '名称'")
	private String name;

	/** 是否内置 */
	@Column(columnDefinition="bit not null comment '是否内置'")
	private Boolean isSystem;

	/** 描述 */
	@Length(max = 200)
	@Column(columnDefinition="varchar(255) comment '描述'")
	private String description;

	/** 权限 */
	@ElementCollection
	@CollectionTable(name = "wx_role_authority")
	private List<String> authorities = new ArrayList<String>();

	/** 管理员 */
	@ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
	private Set<Admin> admins = new HashSet<Admin>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getIsSystem() {
		return isSystem;
	}

	public void setIsSystem(Boolean system) {
		isSystem = system;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<String> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(List<String> authorities) {
		this.authorities = authorities;
	}

	public Set<Admin> getAdmins() {
		return admins;
	}

	public void setAdmins(Set<Admin> admins) {
		this.admins = admins;
	}

}