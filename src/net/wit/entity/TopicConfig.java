package net.wit.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

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
}