package net.wit.controller.makey.model;

import net.wit.controller.model.BaseModel;
import net.wit.entity.Evaluation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//文章列表图

public class EvaluationResultModel extends BaseModel implements Serializable {
    
    private String type;
    /**  结果 */
    private String result;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

}