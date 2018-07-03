package net.wit.controller.makey.model;

import net.wit.controller.model.ArticleContentModel;
import net.wit.controller.model.BaseModel;
import net.wit.entity.Evaluation;
import net.wit.util.JsonUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//文章列表图

public class EvaluationModel extends BaseModel implements Serializable {
    
    private Long id;
    private String result;
    private String jsonResult;
    private String nickName;
    private String qrcode;
    private String shareBkg;
    private String logo;
    private Date createDate;
    private String title;
    private String subTitle;
//    /**  结果 */
//    private List<EvaluationResultModel> result;
//
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getJsonResult() {
        return jsonResult;
    }

    public void setJsonResult(String jsonResult) {
        this.jsonResult = jsonResult;
    }

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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String getShareBkg() {
        return shareBkg;
    }

    public void setShareBkg(String shareBkg) {
        this.shareBkg = shareBkg;
    }

    //    public List<EvaluationResultModel> getResult() {
//        return result;
//    }

//    public void setResult(List<EvaluationResultModel> result) {
//        this.result = result;
//    }

    public void bind(Evaluation evaluation) {
        this.id = evaluation.getId();
        this.result = evaluation.getResult();
        this.jsonResult = evaluation.getJsonResult();

        this.title = evaluation.getTitle();
        this.subTitle = evaluation.getSubTitle();
        this.createDate = evaluation.getCreateDate();
        this.nickName = evaluation.getMember().displayName();
        this.logo = evaluation.getMember().getLogo();
        this.shareBkg = evaluation.getGauge().getShareBkg();
        String q = "https://weex.meixinshuo.com/q?id="+evaluation.getGauge().getId()+"&xuid="+evaluation.getMember().getId();
        this.qrcode = "http://weixin.rzico.com/q/show.jhtml?url="+ URLEncoder.encode(q);

//
//        List<EvaluationResultModel> templates = new ArrayList<EvaluationResultModel>();
//        if (evaluation.getResult()!=null) {
//            templates = JsonUtils.toObject(evaluation.getResult(), List.class);
//        } else {
//            EvaluationResultModel model = new EvaluationResultModel();
//            model.setType("text");
//            model.setResult("测试结果，你很正常");
//            templates.add(model);
//            EvaluationResultModel model1 = new EvaluationResultModel();
//            model1.setType("image");
//            model1.setResult("http://cdn.rzico.com/upload/image/20180224/1519471294626043302.jpg");
//            templates.add(model1);
//        }
//
//        this.result = templates;
//
    }

    public static List<EvaluationModel> bindList(List<Evaluation> evaluations) {
        List<EvaluationModel> ms = new ArrayList<EvaluationModel>();
        for (Evaluation evaluation:evaluations) {
            EvaluationModel m = new EvaluationModel();
            m.bind(evaluation);
            ms.add(m);
        }
        return ms;
    }

}