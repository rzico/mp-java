package net.wit.controller.model;

import java.util.ArrayList;
import java.util.List;

public class ArticleFormDataModel {

    /** 表单名称 */
    private String title;
    /** 表单字段 */
    private List<String> options = new ArrayList<String>();
    /** 表单字段的信息 */
    private List<String> values = new ArrayList<String>();

    /** 按钮名称 */
    private String buttonName;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public String getButtonName() {
        return buttonName;
    }

    public void setButtonName(String buttonName) {
        this.buttonName = buttonName;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }
}
