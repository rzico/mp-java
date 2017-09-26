package net.wit.controller.weex.model;
import net.wit.entity.Friends;
import net.wit.entity.Member;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FriendsModel implements Serializable {

    private Long id;
    /** 昵称 */
    private String nickName;
    /** 头像 */
    private String logo;
    /** 实我 */
    private String name;

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

    public void bind(Friends friends) {
        Member member = friends.getMember();
        this.id = member.getId();
        this.nickName = member.getNickName();
        this.logo = member.getLogo();
        this.name = member.getName();
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