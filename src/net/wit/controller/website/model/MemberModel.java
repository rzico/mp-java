package net.wit.controller.website.model;
import net.wit.entity.Member;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class MemberModel implements Serializable {

    private Long id;
    /** 昵称 */
    private String nickName;
    /** 收藏 */
    private int favorite;
    /** 粉丝 */
    private int fans;
    /** 关注 */
    private int follow;
    /** 余额 */
    private BigDecimal balance;
    /** 签名 */
    private String autograph;
    /** 头像 */
    private String logo;
    /** 标签 */
    private Set<TagModel> tags = new HashSet<TagModel>();

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

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getAutograph() {
        return autograph;
    }

    public void setAutograph(String autograph) {
        this.autograph = autograph;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public int getFollow() {
        return follow;
    }

    public void setFollow(int follow) {
        this.follow = follow;
    }

    public int getFans() {
        return fans;
    }

    public void setFans(int fans) {
        this.fans = fans;
    }

    public Set<TagModel> getTags() {
        return tags;
    }

    public void setTags(Set<TagModel> tags) {
        this.tags = tags;
    }

    public void bind(Member member) {
        this.id = member.getId();
        this.autograph = member.getAutograph();
        this.balance = member.getBalance();
        this.fans = member.getFans().size();
        this.favorite = member.getFavorites().size();
        this.follow = member.getFollows().size();
        this.nickName = member.getNickName();
        this.logo = member.getLogo();
        this.tags = TagModel.bindSet(member.getTags());
     }
}