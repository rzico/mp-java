package net.wit.controller.model;
import net.wit.entity.CouponCode;
import net.wit.entity.Member;
import net.wit.entity.Topic;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class MemberViewModel implements Serializable {

    private Long id;
    /** 昵称 */
    private String nickName;
    /** 收藏 */
    private int favorite;
    /** 粉丝 */
    private int fans;
    /** 关注 */
    private int follow;
    /** 签名 */
    private String autograph;
    /** 头像 */
    private String logo;
    /** 链接 */
    private String url;
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

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
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

    public List<TagModel> getTags() {
        return tags;
    }

    public void setTags(List<TagModel> tags) {
        this.tags = tags;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void bind(Member member) {
        this.id = member.getId();
        this.autograph = member.getAutograph();
        this.fans = member.getFans().size();
        this.favorite = member.getFavorites().size();
        this.follow = member.getFollows().size();
        this.nickName = member.getNickName();
        this.logo = member.getLogo();
        this.tags = TagModel.bindList(member.getTags());
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        Topic topic = member.getTopic();
        if (topic!=null) {
            this.url = "http://"+bundle.getString("weixin.url")+"/website/c"+topic.getTemplate().getSn()+"?id="+member.getId();
        } else {
            this.url = "http://"+bundle.getString("weixin.url")+"/website/c1001?id="+member.getId();
        }
    }
}