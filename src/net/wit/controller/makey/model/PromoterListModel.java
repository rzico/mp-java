package net.wit.controller.makey.model;

import net.wit.controller.model.BaseModel;
import net.wit.entity.Evaluation;
import net.wit.entity.summary.EvaluationSummary;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//文章列表图

public class PromoterListModel extends BaseModel implements Serializable {

    /** 标题 */
    private String nickName;
    /**  子标题 */
    private String logo;
    /** 完成 */
    private Long count;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public void bind(EvaluationSummary summary) {
        this.nickName = summary.getMember().getNickName();
        this.logo = summary.getMember().getLogo();
        this.count = summary.getCount();
    }

    public static List<PromoterListModel> bindList(List<EvaluationSummary> summarys) {
        List<PromoterListModel> ms = new ArrayList<PromoterListModel>();
        for (EvaluationSummary summary:summarys) {
            PromoterListModel m = new PromoterListModel();
            m.bind(summary);
            ms.add(m);
        }
        return ms;
    }

}