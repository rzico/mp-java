
package net.wit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.wit.MapEntity;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity -  礼物
 * 
 */

@Entity
@Table(name = "wx_live_gift")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_live_gift_sequence")
public class LiveGift extends OrderEntity {

	private static final long serialVersionUID = 41L;

	/** 名称 */
	@NotNull
	@Length(max = 200)
	@Column(nullable = false, length = 200,columnDefinition="varchar(255) not null comment '名称'")
	private String name;

	/** 缩略图 */
	@Length(max = 200)
	@Column(nullable = false, length = 200,columnDefinition="varchar(255) not null comment '缩略图'")
	private String thumbnail;

	/** 价格 */
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false,columnDefinition="bigint(20) not null default 0 comment '价格'")
	private Long price;

	/** 是否删除 */
	@NotNull
	@Column(nullable = false,columnDefinition="bit not null comment '是否删除'")
	private Boolean deleted;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public Long getPrice() {
		return price;
	}

	public void setPrice(Long price) {
		this.price = price;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
}