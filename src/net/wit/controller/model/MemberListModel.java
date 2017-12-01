package net.wit.controller.model;
import net.wit.entity.Member;
import net.wit.util.MD5Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MemberListModel implements Serializable {

    private Long id;
    /** 昵称 */
    private String nickName;
    /** 姓名 */
    private String name;
    /** 头像 */
    private String logo;
    /** 手机 */
    private String md5;
    /** 标签 */
    private List<TagModel> tags = new ArrayList<TagModel>();

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

     public List<TagModel> getTags() {
        return tags;
    }

    public void setTags(List<TagModel> tags) {
        this.tags = tags;
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

    public void bind(Member member) {
        this.id = member.getId();
        this.nickName = member.getNickName();
        this.name = member.getName();
        this.logo = member.getLogo();
        this.tags = TagModel.bindList(member.getTags());
        this.md5 = MD5Utils.getMD5Str(member.getMobile());
     }
}