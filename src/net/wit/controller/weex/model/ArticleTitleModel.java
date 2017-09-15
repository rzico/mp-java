package net.wit.controller.weex.model;

import net.wit.entity.Article;
import net.wit.entity.ArticleTitle;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import java.io.Serializable;

//文章编辑模板

public class ArticleTitleModel implements Serializable {

    /** 标题类型 */
    private ArticleTitle.TitleType titleType;
    /** 标题图1 */
    private String image1;
    /** 标题图2 */
    private String image2;
    /** 标题图3 */
    private String image3;
    /** 标题图4 */
    private String image4;
    /** 标题图5 */
    private String image5;
    /** 标题图6 */
    private String image6;

    public ArticleTitle.TitleType getTitleType() {
        return titleType;
    }

    public void setTitleType(ArticleTitle.TitleType titleType) {
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