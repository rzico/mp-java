package net.wit.controller.admin.model;

import net.wit.Page;
import net.wit.Pageable;

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

    private Object data;

    public void bind(Page page){
        this.draw = this.draw + 1;
        this.recordsFiltered = page.getTotal();
        this.recordsTotal = page.getTotal();
        this.data = page.getContent();
    }

    public int getPageNumber() {
        int p = (int) Math.ceil((double) this.start / (double) this.getLength())+1;
        return p;
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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
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
