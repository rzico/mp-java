package net.wit.controller.makey.model;

import net.wit.controller.model.ArticleContentModel;
import net.wit.controller.model.BaseModel;
import net.wit.entity.Evaluation;
import net.wit.util.JsonUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

//文章列表图

public class EvaluationModel extends BaseModel implements Serializable {
    
    private Long id;
    private String result;
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

    //    public List<EvaluationResultModel> getResult() {
//        return result;
//    }

//    public void setResult(List<EvaluationResultModel> result) {
//        this.result = result;
//    }

    public void bind(Evaluation evaluation) {
        this.id = evaluation.getId();
        this.result = evaluation.getResult();
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