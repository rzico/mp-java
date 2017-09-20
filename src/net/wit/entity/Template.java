
package net.wit.entity;

import net.wit.MapEntity;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @ClassName: template
 * @Description:  模板
 * @author 降魔战队
 * @date 2017/2/13 19:00:18
 */
@Entity
@Table(name = "wx_template")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_template_sequence")
public class Template extends OrderEntity {

	private static final long serialVersionUID = 104L;

	/**
	 * 模板类型
	 */
	public enum Type {

		/** 文章  */
		article,

		/** 商品  */
		product,

		/** 专题  */
		topic
	}

	/** 模板类型 */
	@NotEmpty
	@Column(columnDefinition="int(11) not null comment '模板类型 {article:文章,product:商品,topic=专题}'")
	private Type type;

	/** 模板编号 */
	@NotEmpty
	@Length(max = 200)
	@Column(columnDefinition="varchar(50) not null comment '模板编号'")
	private String sn;

	/** 模板名称 */
	@NotEmpty
	@Length(max = 200)
	@Column(columnDefinition="varchar(255) not null comment '模板名称'")
	private String name;

	/** 缩例图 */
	@Length(max = 255)
	@Column(columnDefinition="varchar(255) comment '缩例图'")
	private String thumbnial;

	/** 是否免费 */
	@NotEmpty
	@Column(columnDefinition="bit not null comment '是否免费'")
	private  Boolean isFreed;

	/** 是否默认 */
	@NotEmpty
	@Column(columnDefinition="bit not null comment '是否默认'")
	private  Boolean isDefault;

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getIsFreed() {
		return isFreed;
	}

	public void setIsFreed(Boolean freed) {
		isFreed = freed;
	}

	public Boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Boolean aDefault) {
		isDefault = aDefault;
	}

	public String getThumbnial() {
		return thumbnial;
	}

	public void setThumbnial(String thumbnial) {
		this.thumbnial = thumbnial;
	}
}