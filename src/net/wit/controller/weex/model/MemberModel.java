package net.wit.controller.weex.model;
import net.wit.entity.Member;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class MemberModel implements Serializable {

    private Long id;
    private String nickName;
    private int favorite;
    private int fans;
    private int follow;
    private BigDecimal balance;
    private String autograph;
    private String logo;
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