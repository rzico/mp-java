
package net.wit.entity;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Entity - 客服
 * @author 降魔战队
 * @date 2017/2/13 19:00:18
 */
@Entity
@Table(name = "wx_custom_service")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_custom_service_sequence")
public class CustomService extends BaseEntity {

	private static final long serialVersionUID = 49L;

	/** 头像 */
	@NotEmpty
	@Length(max = 200)
	@Column(columnDefinition="varchar(255) not null comment '头像'")
	private String logo;

	/** 名称 */
	@NotEmpty
	@Length(max = 200)
	@Column(columnDefinition="varchar(255) not null comment '名称'")
	private String name;

	/** 微信号 */
	@Length(max = 200)
	@Column(columnDefinition="varchar(255) comment '微信号'")
	private String wechat;

	/** QQ号 */
	@Length(max = 200)
	@Column(columnDefinition="varchar(255) comment 'QQ号'")
	private String qq;

	/** 描述 */
	@Length(max = 200)
	@Column(columnDefinition="varchar(255) comment '描述'")
	private String description;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getWechat() {
		return wechat;
	}

	public void setWechat(String wechat) {
		this.wechat = wechat;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}
}