package net.wit.controller.model;

import net.wit.entity.Cart;
import net.wit.entity.CartItem;
import net.wit.entity.Notice;
import net.wit.entity.Occupation;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class NoticeModel extends BaseModel implements Serializable {

    private String type;

    private String title;

    private String url;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void bind(Notice notice) {
        this.type = notice.getType().name();
        this.title = notice.getContent();
    }

    public static List<NoticeModel> bindList(List<Notice> notices) {
        List<NoticeModel> ms = new ArrayList<NoticeModel>();
        for (Notice notice:notices) {
            NoticeModel m = new NoticeModel();
            m.bind(notice);
            ms.add(m);
        }
        return ms;
    }

}