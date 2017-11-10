
package net.wit.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Entity - 会员注册项
 * 
 * @version 3.0
 */
@Entity
@Table(name = "wx_member_attribute")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_member_attribute_sequence")
public class MemberAttribute extends OrderEntity {

	private static final long serialVersionUID = 30L;

	/**
	 * 类型
	 */
	public enum Type {

		/** 姓名 */
		name,

		/** 性别 */
		gender,

		/** 出生日期 */
		birth,

		/** 地区 */
		area,

		/** 地址 */
		address,

		/** 邮编 */
		zipCode,

		/** 电话 */
		phone,

		/** 手机 */
		mobile,

		/** 文本 */
		text,

		/** 单选项 */
		select,

		/** 多选项 */
		checkbox
	}

	/** 名称 */
	@NotEmpty
	@Length(max = 200)
	@Column(columnDefinition="varchar(255) not null comment '名称'")
	private String name;

	/** 类型 */
	@NotNull(groups = Save.class)
	@Column(columnDefinition="int(11) not null comment '类型 {name:姓名,gender:性别,birth:出生日期,area:地区,address:地址,zipCode:邮编,phone:电话,mobile:手机,text:文本,select:单选项,checkbox:多选项}'")
	private Type type;

	/** 是否启用 */
	@NotNull
	@Column(columnDefinition="bit not null comment '是否启用'")
	private Boolean isEnabled;

	/** 是否必填 */
	@NotNull
	@Column(columnDefinition="bit not null comment '是否必填'")
	private Boolean isRequired;

	/** 属性序号 */
	@Column(columnDefinition="int(11) not null comment '序号'")
	private Integer propertyIndex;

	/** 可选项 */
	@ElementCollection
	@CollectionTable(name = "wx_member_attribute_option")
	private List<String> options = new ArrayList<String>();

	/**
	 * 获取名称
	 * 
	 * @return 名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置名称
	 * 
	 * @param name
	 *            名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取类型
	 * 
	 * @return 类型
	 */
	public Type getType() {
		return type;
	}

	/**
	 * 设置类型
	 * 
	 * @param type
	 *            类型
	 */
	public void setType(Type type) {
		this.type = type;
	}

	/**
	 * 获取是否启用
	 * 
	 * @return 是否启用
	 */
	public Boolean getIsEnabled() {
		return isEnabled;
	}

	/**
	 * 设置是否启用
	 * 
	 * @param isEnabled
	 *            是否启用
	 */
	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	/**
	 * 获取是否必填
	 * 
	 * @return 是否必填
	 */
	public Boolean getIsRequired() {
		return isRequired;
	}

	/**
	 * 设置是否必填
	 * 
	 * @param isRequired
	 *            是否必填
	 */
	public void setIsRequired(Boolean isRequired) {
		this.isRequired = isRequired;
	}

	/**
	 * 获取属性序号
	 * 
	 * @return 属性序号
	 */
	public Integer getPropertyIndex() {
		return propertyIndex;
	}

	/**
	 * 设置属性序号
	 * 
	 * @param propertyIndex
	 *            属性序号
	 */
	public void setPropertyIndex(Integer propertyIndex) {
		this.propertyIndex = propertyIndex;
	}

	/**
	 * 获取可选项
	 * 
	 * @return 可选项
	 */
	public List<String> getOptions() {
		return options;
	}

	/**
	 * 设置可选项
	 * 
	 * @param options
	 *            可选项
	 */
	public void setOptions(List<String> options) {
		this.options = options;
	}

}