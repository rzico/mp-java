
package net.wit.entity;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;

/**
 * Entity -  系统公告
 *
 * @author 降魔战队
 * @date 2017/2/13 19:00:18
 */
@Entity
@Table(name = "wx_notice")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_notice_sequence")
public class Notice extends BaseEntity {

	private static final long serialVersionUID = 17L;

	/**
	 * 状态
	 */
	public enum Type {
		/** 直播公告 */
		live,
		/** 平台公告 */
		mall
	}


	/**  类型 */
	@Column(columnDefinition="int(11) comment '类型 { live:直播公告,mall:平台公告}'")
	private Type type;

	/** 标题 */
	@Length(max = 255)
	@Column(columnDefinition="varchar(255) comment '标题'")
	private String content;

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}