package net.wit.controller.makey.model;

import net.wit.controller.model.BaseModel;
import net.wit.entity.Evaluation;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

//文章列表图

public class EvaluationModel extends BaseModel implements Serializable {
    
    private Long id;
    /**  结果 */
    private String result;

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

    public void bind(Evaluation evaluation) {
        this.id = evaluation.getId();
        this.result = evaluation.getResult();
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