package net.wit.controller.model;
import net.wit.entity.Member;
import net.wit.entity.Message;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessageModel implements Serializable {

    private String userId;
    /** 类型 */
    private Message.Type type;
    /** 昵称 */
    private String nickName;
    /** 头像 */
    private String logo;
    /** 内容 */
    private String content;
    /** 未读数 */
    private Integer unRead;
    /** 时间 */
    private Date createDate;
    /** 来源 */
    private Long srcId;

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

    public Integer getUnRead() {
        return unRead;
    }

    public void setUnRead(Integer unRead) {
        this.unRead = unRead;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getSrcId() {
        return srcId;
    }

    public void setSrcId(Long srcId) {
        this.srcId = srcId;
    }

    public void bind(net.wit.entity.Message message) {
        Member member = message.getMember();
        this.userId = member.userId();
        this.nickName = member.getNickName();
        this.logo = member.getLogo();
        this.content = message.getContent();
        if (message.getReaded()) {
            this.unRead = 0;
        } else {
            this.unRead = 1;
        }
        this.createDate = message.getCreateDate();
        this.type = message.getType();
        this.srcId = message.getSrcId();
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

    public static List<MessageModel> bindDialogue(List<net.wit.entity.Message> messages) {
        List<MessageModel> ms = new ArrayList<MessageModel>();
        for (net.wit.entity.Message message:messages) {
            MessageModel model = null;
            for (int i=0;i<ms.size();i++) {
                if (ms.get(i).getType().equals(message.getType())) {
                    model = ms.get(i);
                    break;
                }
            }
            if (model==null) {
                model = new MessageModel();
                model.bind(message);
                ms.add(model);
            } else {
                model.setUnRead(model.getUnRead()+1);
            }
        }
        return ms;
    }

}