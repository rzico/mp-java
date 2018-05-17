package net.wit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Lob;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

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
	}

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

	}

	/**
	 * 1、未授权
	 * 2、已授权
	 * 3、待审核
	 * 4、正在审核
	 * 5、通过审核（待发布）
	 * 6、已发布
	 */
	public static enum Estate{
		UNAUTHORIZED,//未授权
		AUTHORIZED,//已授权
		AUDITING,//正在审核

		ISAUDITING,//通过审核（待发布）
		PASS//已发布

	}

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

	/** 小程序 version */
	@Column(columnDefinition="varchar(255) comment '小程序 version'")
	private String version;

	/** 小程序 estate */
	@NotNull
	@Column(columnDefinition="int(11) default '0'comment '状态 { 未授权:UNAUTHORIZED, 已授权:AUTHORIZED, 正在审核:AUDITING, 通过审核:ISAUDITING, 已发布:PASS}'")
	private Estate estate;

	/** 小程序 刷新token */
	@Column(columnDefinition="varchar(255) comment '刷新Token version'")
	private String refreshToken;

	/** 小程序 token到期时间 */
	@NotNull
	@Column(columnDefinition="datetime not null comment '到期日'")
	private Date tokenExpire;

	/** 小程序原始id */
	@Column(columnDefinition="varchar(255) comment '原始ID version'")
	private String userName;

	/** 小程序 二维码地址 */
	@Column(columnDefinition="varchar(255) comment '二维码地址 version'")
	private String qrcodePath;

	/** 小程序 状态备注 */
	@Column(columnDefinition="varchar(255) comment '备注 version'")
	private String stateRemark;

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

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Estate getEstate() {
		return estate;
	}

	public void setEstate(Estate estate) {
		this.estate = estate;
	}

	public String getStateRemark() {
		return stateRemark;
	}

	public void setStateRemark(String stateRemark) {
		this.stateRemark = stateRemark;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public Date getTokenExpire() {
		return tokenExpire;
	}

	public void setTokenExpire(Date tokenExpire) {
		this.tokenExpire = tokenExpire;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getQrcodePath() {
		return qrcodePath;
	}

	public void setQrcodePath(String qrcodePath) {
		this.qrcodePath = qrcodePath;
	}
}