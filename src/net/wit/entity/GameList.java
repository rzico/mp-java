
package net.wit.entity;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Entity - 游戏
 * @author 降魔战队
 * @date 2017/2/13 19:00:18
 */
@Entity
@Table(name = "nt_game_list")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "nt_game_list_sequence")
public class GameList extends OrderEntity {

	private static final long serialVersionUID = 49L;

	/**
	 * 状态
	 */
	public enum Status {

		/** 开通 */
		enabled,

		/** 关闭 */
		disabled

	}
	/**
	 * 类型
	 */
	public enum Type {

		/** 真人游戏 */
		nihtan,

		/** 电子游戏 */
		kage

	}

	/** 游戏名 */
	@Column(columnDefinition="varchar(255) not null comment '游戏名'")
	private String game;

	/** 图片 */
	@Column(columnDefinition="varchar(255) comment '图片'")
	private String logo;

	/** 桌号 */
	@Column(columnDefinition="varchar(255) comment '桌号'")
	private String tableNo;

	/** 投注 */
	@Column(columnDefinition="varchar(255) comment '投注'")
	private String ranges;

	/** 游戏名 */
	@Column(columnDefinition="varchar(255) comment '游戏名'")
	private String name;

	/** 状态 */
	@Column(nullable = false,columnDefinition="int(11) not null comment '状态'")
	private Status status;

	/** 类型 */
	@Column(nullable = false,columnDefinition="int(11) not null comment '类型'")
	private Type type;

	/** 说明 */
	@Length(max = 200)
	@Column(columnDefinition="varchar(255) comment '说明'")
	private String memo;

	public String getGame() {
		return game;
	}

	public void setGame(String game) {
		this.game = game;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getTableNo() {
		return tableNo;
	}

	public void setTableNo(String tableNo) {
		this.tableNo = tableNo;
	}

	public String getRanges() {
		return ranges;
	}

	public void setRanges(String ranges) {
		this.ranges = ranges;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

}