package net.wit.controller.model;
import net.wit.entity.Member;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

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
    /** 性别 */
    private Member.Gender gender;
    /** 职业 */
    private String occupation;
    /** 生日 */
    private Date birth;
    /** 余额 */
    private BigDecimal balance;

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

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    public int getFans() {
        return fans;
    }

    public void setFans(int fans) {
        this.fans = fans;
    }

    public int getFollow() {
        return follow;
    }

    public void setFollow(int follow) {
        this.follow = follow;
    }

    public String getAutograph() {
        return autograph;
    }

    public void setAutograph(String autograph) {
        this.autograph = autograph;
    }

    public Member.VIP getVip() {
        return vip;
    }

    public void setVip(Member.VIP vip) {
        this.vip = vip;
    }

    public Member.Gender getGender() {
        return gender;
    }

    public void setGender(Member.Gender gender) {
        this.gender = gender;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
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
        this.birth = member.getBirth();
        this.gender=member.getGender();
        if (member.getOccupation()!=null) {
            this.occupation = member.getOccupation().getName();
        }
        this.balance = member.getBalance();
     }

}