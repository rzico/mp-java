
package net.wit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: Music
 * @Description:  音乐
 * @author 降魔战队
 * @date 2017/2/13 19:00:18
 */
@Entity
@Table(name = "wx_music")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_music_sequence")
public class Music extends OrderEntity {

	private static final long serialVersionUID = 33L;

	/** 标题 */
	@NotNull
	@Length(max = 200)
	@Column(columnDefinition="varchar(255) not null comment '标题'")
	private String title;

	/** 缩例图 */
	@NotEmpty
	@Length(max = 200)
	@Column(columnDefinition="varchar(255) not null comment '缩例图'")
	private String thumbnail;

	/** 阅读数 */
	@Min(0)
	@NotNull
	@Column(columnDefinition="bigint(20) not null default 0 comment '阅读数'")
	private Long hits;

	/** 介绍 */
	@Lob
	@Column(columnDefinition="longtext comment '介绍'")
	@JsonIgnore
	private String content;

	/** 企业 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private Enterprise enterprise;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public Long getHits() {
		return hits;
	}

	public void setHits(Long hits) {
		this.hits = hits;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Enterprise getEnterprise() {
		return enterprise;
	}

	public void setEnterprise(Enterprise enterprise) {
		this.enterprise = enterprise;
	}
}