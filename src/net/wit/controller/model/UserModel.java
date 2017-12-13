package net.wit.controller.model;
import net.wit.entity.Member;

import java.io.Serializable;

public class UserModel extends BaseModel implements Serializable {

    private Long id;
    /** 昵称 */
    private String nickName;
    /** 头像 */
    private String logo;

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

    public void bind(Member member) {
        this.id = member.getId();
        this.nickName = member.getNickName();
        this.logo = member.getLogo();
     }
}