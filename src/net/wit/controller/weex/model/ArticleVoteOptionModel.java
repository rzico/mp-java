package net.wit.controller.weex.model;

import java.io.Serializable;
import java.util.ArrayList;

import java.util.List;

//文章题库模板 H5等

public class ArticleVoteOptionModel implements Serializable {
    /** 题目 */
    private String  title;
    /** 答案 */
    private List<String> options = new ArrayList<String>();

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
}