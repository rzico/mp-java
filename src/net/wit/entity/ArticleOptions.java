package net.wit.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

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

	private static final long serialVersionUID = 402L;

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
	@Column(nullable = false)
	private Boolean isPublish;

	/** 是否精选 */
	@NotNull
	@Column(nullable = false)
	private Boolean isPitch;

	/** 是否评论 */
	@NotNull
	@Column(nullable = false)
	private Boolean isReview;

	/** 是否赞赏 */
	@NotNull
	@Column(nullable = false)
	private Boolean isReward;

	/** 权限 */
	@NotNull
	@Column(nullable = false)
	private Authority authority;

	/** 是否删除，false 删除不显示 */
	@NotNull
	@Column(nullable = false)
	private Boolean isShow;

}