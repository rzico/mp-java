package net.wit.entity;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

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
public class ArticleTitle implements Serializable {

	private static final long serialVersionUID = 108L;

	public static enum TitleType{
		/** 单图 */
		image1,
		/** 2张图 */
		image2,
		/** 2张图 */
		image3,
		/** 4张图 */
		image4,
		/** 5张图 */
		image5,
		/** 6张图 */
		image6
	};

	/** 标题类型 */
	@NotEmpty
	@Column(columnDefinition="int(11) comment '类型 {image1:单图,image2:2张图,image3:3张图,image4:4张图,image5:5张图,image6:6张图}'")
	private TitleType titleType;

	/** 标题图1 */
	@Length(max = 255)
	@Column(columnDefinition="varchar(255) comment '标题图1'")
	private String image1;
	/** 标题图2 */
	@Length(max = 255)
	@Column(columnDefinition="varchar(255) comment '标题图2'")
	private String image2;
	/** 标题图3 */
	@Length(max = 255)
	@Column(columnDefinition="varchar(255) comment '标题图3'")
	private String image3;
	/** 标题图4 */
	@Length(max = 255)
	@Column(columnDefinition="varchar(255) comment '标题图4'")
	private String image4;
	/** 标题图5 */
	@Length(max = 255)
	@Column(columnDefinition="varchar(255) comment '标题图5'")
	private String image5;
	/** 标题图6 */
	@Length(max = 255)
	@Column(columnDefinition="varchar(255) comment '标题图6'")
	private String image6;

	public TitleType getTitleType() {
		return titleType;
	}

	public void setTitleType(TitleType titleType) {
		this.titleType = titleType;
	}

	public String getImage1() {
		return image1;
	}

	public void setImage1(String image1) {
		this.image1 = image1;
	}

	public String getImage2() {
		return image2;
	}

	public void setImage2(String image2) {
		this.image2 = image2;
	}

	public String getImage3() {
		return image3;
	}

	public void setImage3(String image3) {
		this.image3 = image3;
	}

	public String getImage4() {
		return image4;
	}

	public void setImage4(String image4) {
		this.image4 = image4;
	}

	public String getImage5() {
		return image5;
	}

	public void setImage5(String image5) {
		this.image5 = image5;
	}

	public String getImage6() {
		return image6;
	}

	public void setImage6(String image6) {
		this.image6 = image6;
	}
}