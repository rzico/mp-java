
package net.wit.entity;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * @ClassName: Occupation
 * @Description:  职业
 * @author 降魔战队
 * @date 2017/2/13 19:00:18
 */
@Entity
@Table(name = "wx_occupation")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_occupation_sequence")
public class Occupation extends OrderEntity {

	private static final long serialVersionUID = 33L;

	/**
	 * 状态
	 */
	public enum Status {

		/** 开启  */
		enabled,

		/** 关闭  */
		disabled
	}

	/** 状态 */
	@NotNull
	@Column(columnDefinition="int(11) not null comment '状态 {enabled:开启,disabled:关闭}'")
	private Status status;

	/** 名称 */
	@NotNull
	@Length(max = 200)
	@Column(columnDefinition="varchar(255) not null comment '名称'")
	private String name;

	public Occupation.Status getStatus() {
		return status;
	}

	public void setStatus(Occupation.Status status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}