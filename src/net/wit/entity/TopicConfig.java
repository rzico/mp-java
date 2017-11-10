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

}