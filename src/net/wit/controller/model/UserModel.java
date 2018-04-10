package net.wit.controller.model;
import net.wit.entity.Member;

import java.io.Serializable;

public class UserModel extends BaseModel implements Serializable {

    private Long id;
    /** 昵称 */
    private String nickName;
    /** 头像 */
    private String logo;
    /** 收藏 */
    private int favorite;
    /** 粉丝 */
    private int fans;
    /** 关注 */
    private int follow;
    /** 签名 */
    private String autograph;
    /** 星级 */
    private Member.VIP vip;

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
        this.autograph = member.getAutograph();
        this.fans = member.getFans().size();
        this.favorite = member.getFavorites().size();
        this.follow = member.getFollows().size();
        this.nickName = member.displayName();
        this.logo = member.getLogo();
        this.vip = member.getVip();
     }
}