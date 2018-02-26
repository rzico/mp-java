package net.wit.controller.makey.model;

import net.wit.controller.model.BaseModel;

import java.io.Serializable;
import java.math.BigDecimal;

//文章列表图

public class EvaluationAnswerModel extends BaseModel implements Serializable {
    /** 题目 */
    private Long questionId;
    /** 选择 */
    private Long optionId;
    /** 分数 */
    private BigDecimal score;

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public Long getOptionId() {
        return optionId;
    }

    public void setOptionId(Long optionId) {
        this.optionId = optionId;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

}