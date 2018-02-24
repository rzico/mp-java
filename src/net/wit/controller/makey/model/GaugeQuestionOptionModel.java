package net.wit.controller.makey.model;

import net.wit.controller.model.BaseModel;
import net.wit.entity.GaugeQuestion;
import net.wit.entity.MemberAttribute;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

//文章列表图

public class GaugeQuestionOptionModel extends BaseModel implements Serializable {

    private Long id;
    /** 题目 */
    private String name;
    /** 图片 */
    private String image;
    /** 分数 */
    private BigDecimal score;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

}