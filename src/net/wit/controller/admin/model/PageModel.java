package net.wit.controller.admin.model;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.wit.Page;
import net.wit.Pageable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jinlesoft on 2017/9/4.
 */
public class PageModel {

    //    绘制计数器
    private int draw;
    //    开始记录号
    private int start;
    //    页显示大小
    private int length;
    //    记录总数
    private long recordsTotal;
    //    过滤后记录总数
    private long recordsFiltered;
    //    返回的数据
    private Object data;

    public static PageModel bind(Page page){
        PageModel model = new PageModel();
        model.start = page.getPageStart();
        model.length = page.getPageSize();
        model.recordsFiltered = page.getTotal();
        model.recordsTotal = page.getTotal();
        model.data = page.getContent();
        return model;
    }

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public long getRecordsTotal() {
        return recordsTotal;
    }

    public void setRecordsTotal(long recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    public long getRecordsFiltered() {
        return recordsFiltered;
    }

    public void setRecordsFiltered(long recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }

     public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

  }
