package net.wit.controller.makey.model;

import net.wit.controller.model.BaseModel;
import net.wit.entity.Gauge;
import net.wit.entity.GaugeQuestion;
import net.wit.entity.MemberAttribute;
import net.wit.util.JsonUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//文章列表图

public class GaugeQuestionModel extends BaseModel implements Serializable {

    private Long id;
    /** 题型 */
    private GaugeQuestion.Type type;

    /** 题目 */
    private String title;

    /** 选项
     *  {id:序号,name:"你几岁了",image:"图片地址",score:分数} */
    private List<GaugeQuestionOptionModel> content;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GaugeQuestion.Type getType() {
        return type;
    }

    public void setType(GaugeQuestion.Type type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<GaugeQuestionOptionModel> getContent() {
        return content;
    }

    public void setContent(List<GaugeQuestionOptionModel> content) {
        this.content = content;
    }

    public void bind(GaugeQuestion gaugeQuestion) {
        this.id = gaugeQuestion.getId();
        this.title = gaugeQuestion.getTitle();
        this.content = new ArrayList<GaugeQuestionOptionModel>();
        if (gaugeQuestion.getContent()!=null) {
            this.content = JsonUtils.toObject(gaugeQuestion.getContent(), List.class);
        }
    }


    public static List<GaugeQuestionModel> bindList(List<GaugeQuestion> gaugeQuestions) {
        List<GaugeQuestionModel> ms = new ArrayList<GaugeQuestionModel>();
        for (GaugeQuestion GaugeQuestion:gaugeQuestions) {
            GaugeQuestionModel m = new GaugeQuestionModel();
            m.bind(GaugeQuestion);
            ms.add(m);
        }
        return ms;
    }


}