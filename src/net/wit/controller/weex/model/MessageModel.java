package net.wit.controller.weex.model;
import net.wit.entity.Friends;
import net.wit.entity.Member;
import net.wit.entity.Message;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class MessageModel implements Serializable {

    private Long id;
    /** 类型 */
    private Message.Type type;
    /** 昵称 */
    private String nickName;
    /** 头像 */
    private String logo;
    /** 内容 */
    private String content;
    /** 已读 */
    private Boolean readed;
    /** 时间 */
    private Date createDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getReaded() {
        return readed;
    }

    public void setReaded(Boolean readed) {
        this.readed = readed;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Message.Type getType() {
        return type;
    }

    public void setType(Message.Type type) {
        this.type = type;
    }

    public void bind(net.wit.entity.Message message) {
        Member member = message.getMember();
        this.id = member.getId();
        this.nickName = member.getNickName();
        this.logo = member.getLogo();
        this.content = message.getContent();
        this.readed = message.getReaded();
        this.createDate = message.getCreateDate();
        this.type = message.getType();
     }

    public static List<MessageModel> bindList(List<net.wit.entity.Message> messages) {
        List<MessageModel> ms = new ArrayList<MessageModel>();
        for (net.wit.entity.Message message:messages) {
            MessageModel model = new MessageModel();
            model.bind(message);
            ms.add(model);
        }
        return ms;
    }

    public static List<MessageModel> bindList(Map<Message.Type,Message> map) {
        List<MessageModel> ms = new ArrayList<MessageModel>();
        for (net.wit.entity.Message message:map.values()) {
            MessageModel model = new MessageModel();
            model.bind(message);
            ms.add(model);
        }
        return ms;
    }

}