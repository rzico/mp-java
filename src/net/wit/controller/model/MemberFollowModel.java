package net.wit.controller.model;
import net.wit.entity.Article;
import net.wit.entity.Member;
import net.wit.entity.MemberFollow;
import net.wit.util.MD5Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MemberFollowModel extends BaseModel implements Serializable {

    private Long id;
    /** 昵称 */
    private String nickName;
    /** 姓名 */
    private String name;
    /** 签名 */
    private String autograph;
    /** 头像 */
    private String logo;
    /** 我关注了 */
    private Boolean follow;
    /** 关注了我 */
    private Boolean followed;
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

    public String getAutograph() {
        return autograph;
    }

    public void setAutograph(String autograph) {
        this.autograph = autograph;
    }

    public Boolean getFollow() {
        return follow;
    }

    public void setFollow(Boolean follow) {
        this.follow = follow;
    }

    public Boolean getFollowed() {
        return followed;
    }

    public void setFollowed(Boolean followed) {
        this.followed = followed;
    }

    public void bind(Member member) {
        this.id = member.getId();
        this.nickName = member.displayName();
        this.autograph = member.getAutograph();
        this.name = member.getName();
        this.logo = member.getLogo();
        this.tags = TagModel.bindList(member.getTags());
        this.followed = false;
        this.follow = false;
    }


    public static List<MemberFollowModel> bindFollow(List<MemberFollow> memberFollows) {
        List<MemberFollowModel> ms = new ArrayList<MemberFollowModel>();
        for (MemberFollow memberFollow:memberFollows) {
            MemberFollowModel m = new MemberFollowModel();
            m.bind(memberFollow.getFollow());
            ms.add(m);
        }
        return ms;
    }


    public static List<MemberFollowModel> bindFans(List<MemberFollow> memberFans) {
        List<MemberFollowModel> ms = new ArrayList<MemberFollowModel>();
        for (MemberFollow memberFan:memberFans) {
            MemberFollowModel m = new MemberFollowModel();
            m.bind(memberFan.getMember());
            ms.add(m);
        }
        return ms;
    }


}