package net.wit.controller.admin.model;

import net.wit.Page;
import net.wit.Pageable;

/**
 * Created by Jinlesoft on 2017/9/4.
 */
public class PageModel {

    //    当前页数
    private int draw;
    //    当前页数
    private long recordsTotal;
    //    当前页数
    private long recordsFiltered;

    private Object data;

    public static PageModel bindPage(Page page){
        PageModel pageModel = new PageModel();
        pageModel.setData(page.getPageNumber());
        pageModel.setRecordsTotal(page.getTotal());
        pageModel.setData( page.getContent() );
        return pageModel;
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
}
