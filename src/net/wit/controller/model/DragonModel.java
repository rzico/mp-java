package net.wit.controller.model;

import net.wit.entity.Dragon;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DragonModel extends BaseModel implements Serializable {

    private Long id;
    private Long mainId;
    private Long articleId;
    private Dragon.Type type;
    private Dragon.Status status;
    private String title;
    private Date createDate;
    private Integer orderCount;
    private String nickName;
    private String logo;

    public Dragon.Type getType() {
        return type;
    }

    public void setType(Dragon.Type type) {
        this.type = type;
    }

    public Dragon.Status getStatus() {
        return status;
    }

    public void setStatus(Dragon.Status status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
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

    public Integer getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public void bind(Dragon dragon) {
        this.type = dragon.getType();
        this.title = dragon.getTitle();
        this.createDate = dragon.getCreateDate();
        this.status = dragon.getStatus();
        this.orderCount = dragon.getOrders().size();
        this.logo = dragon.getMember().getLogo();
        this.nickName = dragon.getMember().getNickName();

        this.id = dragon.getId();
        this.articleId = dragon.getArticle().getId();
        if (dragon.getParent()==null) {
            this.mainId = dragon.getId();
        }
    }

    public static List<DragonModel> bindList(List<Dragon> dragons) {
        List<DragonModel> ms = new ArrayList<DragonModel>();
        for (Dragon dragon:dragons) {
            DragonModel m = new DragonModel();
            m.bind(dragon);
            ms.add(m);
        }
        return ms;
    }

}