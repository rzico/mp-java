package net.wit.controller.model;
import net.wit.entity.Friends;
import net.wit.entity.Member;
import net.wit.util.MD5Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FriendsModel extends BaseModel implements Serializable {

    private Long id;
    /** 昵称 */
    private String nickName;
    /** 头像 */
    private String logo;
    /** 实我 */
    private String name;
    /** 手机号 */
    private String md5;
    /** 类型 */
    private Friends.Type type;
    /** 时间 */
    private Date createDate;
    /** 状态 */
    private Friends.Status status;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Friends.Status getStatus() {
        return status;
    }

    public void setStatus(Friends.Status status) {
        this.status = status;
    }

    public Friends.Type getType() {
        return type;
    }

    public void setType(Friends.Type type) {
        this.type = type;
    }

    public void bind(Friends friends) {
        Member member = friends.getFriend();
        this.id = member.getId();
        this.nickName = member.displayName();
        this.logo = member.getLogo();
        this.name = member.getName();
        if (member.getMobile()!=null) {
            this.md5 = MD5Utils.getMD5Str(member.getMobile());
        }
        this.createDate = friends.getModifyDate();
        this.status = friends.getStatus();
        this.type = friends.getType();
     }

    public static List<FriendsModel> bindList(List<Friends> friends) {
        List<FriendsModel> fms = new ArrayList<FriendsModel>();
        for (Friends friend:friends) {
            FriendsModel model = new FriendsModel();
            model.bind(friend);
            fms.add(model);
        }
        return fms;
    }
}