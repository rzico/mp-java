package net.wit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Lob;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ClassName: SafeKey
 * @Description: 安全密钥
 *
 * @author 降魔战队
 * @date 2017/2/13 19:00:18
 */

@Embeddable
public class TopicConfig implements Serializable {

	private static final long serialVersionUID = 61L;

	public static enum PromoterType{
		/** 任何用户 */
		any,
		/** 团队成员 */
		team,
		/** 分红股东 */
		partner
	};

	public static enum Pattern{
		/** 无门槛，领卡，即成为团队成员 */
		pattern1,
		/** 购买任意商品，即成为团队成员 */
		pattern2,
		/** 购买分销商品，即成为团队成员 */
		pattern3,
		/** 单次消费满1000(设置)，即成为团队成员 */
		pattern4,
		/** 累计消费满3000(设置)，即成为团队成员 */
		pattern5

	};

	/**  团队类型 */
	@NotNull
	@Column(columnDefinition="int(11) not null comment '团队类型'")
	private PromoterType promoterType;

	/**  团队模式 */
	@NotNull
	@Column(columnDefinition="int(11) not null comment '团队模式'")
	private Pattern pattern;

	/**  设置金额 */
	@Min(0)
	@Column(columnDefinition="decimal(21,6) not null default 0 comment '设置金额'")
	private BigDecimal amount;

	/** 开通会员卡 */
	@Column(columnDefinition="bit not null comment '开通会员卡'")
	private Boolean useCard;

	/** 开通优惠券 */
	@Column(columnDefinition="bit not null comment '开通优惠券'")
	private Boolean useCoupon;

	/** 开通收银台 */
	@Column(columnDefinition="bit not null comment '开通收银台'")
	private Boolean useCashier;

	/** 微信 appId */
	@Column(columnDefinition="varchar(255) comment '微信appId'")
	private String wxAppId;

	/** 微信 appSerect */
	@Column(columnDefinition="varchar(255) comment '微信appSerect'")
	private String wxAppSerect;

	/** 小程序 appId */
	@Column(columnDefinition="varchar(255) comment '小程序appId'")
	private String appetAppId;

	/** 小程序 appSerect */
	@Column(columnDefinition="varchar(255) comment '小程序appSerect'")
	private String appetAppSerect;

	public PromoterType getPromoterType() {
		return promoterType;
	}

	public void setPromoterType(PromoterType promoterType) {
		this.promoterType = promoterType;
	}

	public Boolean getUseCard() {
		return useCard;
	}

	public void setUseCard(Boolean useCard) {
		this.useCard = useCard;
	}

	public Boolean getUseCoupon() {
		return useCoupon;
	}

	public void setUseCoupon(Boolean useCoupon) {
		this.useCoupon = useCoupon;
	}

	public Boolean getUseCashier() {
		return useCashier;
	}

	public void setUseCashier(Boolean useCashier) {
		this.useCashier = useCashier;
	}

	public String getWxAppId() {
		return wxAppId;
	}

	public void setWxAppId(String wxAppId) {
		this.wxAppId = wxAppId;
	}

	public String getWxAppSerect() {
		return wxAppSerect;
	}

	public void setWxAppSerect(String wxAppSerect) {
		this.wxAppSerect = wxAppSerect;
	}

	public String getAppetAppId() {
		return appetAppId;
	}

	public void setAppetAppId(String appetAppId) {
		this.appetAppId = appetAppId;
	}

	public String getAppetAppSerect() {
		return appetAppSerect;
	}

	public void setAppetAppSerect(String appetAppSerect) {
		this.appetAppSerect = appetAppSerect;
	}

	public Pattern getPattern() {
		return pattern;
	}

	public void setPattern(Pattern pattern) {
		this.pattern = pattern;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
}