package net.wit.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.time.DateUtils;

/**
 * @ClassName: SafeKey
 * @Description: 安全密钥
 *
 * @author 降魔战队
 * @date 2017/2/13 19:00:18
 */
@Embeddable
public class ArticleOptions implements Serializable {

	private static final long serialVersionUID = 8L;

	/**
	 * 权限
	 */
	public enum Authority {

		/** 公开  所有人可见，且录入个人专栏  */
		isPublic,

		/** 不公开，自行控制分享范围，仅被分享的人可见  */
		isShare,

		/** 加密，设置密码，凭密码访问  */
		isEncrypt,

		/** 私密，公自已可见  */
		isPrivate
	}

	/** 是否投稿 */
	@NotNull
	@Column(columnDefinition="bit comment '是否投稿'")
	private Boolean isPublish;

	/** 是否精选 */
	@NotNull
	@Column(columnDefinition="bit comment '是否精选'")
	private Boolean isPitch;

	/** 是否评论 */
	@NotNull
	@Column(columnDefinition="bit comment '是否评论'")
	private Boolean isReview;

	/** 是否赞赏 */
	@NotNull
	@Column(columnDefinition="bit comment '是否赞赏'")
	private Boolean isReward;

	/** 谁可见 */
	@NotNull
	@Column(columnDefinition="int(11) comment '谁可见  {isPublic:公开,isShare:不会开,isEncrypt:加密,isPrivate:私秘}'")
	private Authority authority;

	/** 是否样例 */
	@NotNull
	@Column(columnDefinition="bit comment '是否样例'")
	private Boolean isExample;

	/** 是否置顶 */
	@NotNull
	@Column(columnDefinition="bit comment '是否置顶'")
	private Boolean isTop;

	/** 密码 */
	@Column(columnDefinition=" varchar(255) comment '密码'")
	private String password;

	public Boolean getIsPublish() {
		return isPublish;
	}

	public void setIsPublish(Boolean publish) {
		isPublish = publish;
	}

	public Boolean getIsPitch() {
		return isPitch;
	}

	public void setIsPitch(Boolean pitch) {
		isPitch = pitch;
	}

	public Boolean getIsReview() {
		return isReview;
	}

	public void setIsReview(Boolean review) {
		isReview = review;
	}

	public Boolean getIsReward() {
		return isReward;
	}

	public void setIsReward(Boolean reward) {
		isReward = reward;
	}

	public Authority getAuthority() {
		return authority;
	}

	public void setAuthority(Authority authority) {
		this.authority = authority;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getIsExample() {
		return isExample;
	}

	public void setIsExample(Boolean isExample) {
		isExample = isExample;
	}

	public Boolean getIsTop() {
		return isTop;
	}

	public void setIsTop(Boolean top) {
		isTop = top;
	}
}