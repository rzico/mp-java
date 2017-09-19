package net.wit.controller.weex.model;
import net.wit.entity.Member;
import net.wit.entity.Occupation;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class MemberAttributeModel implements Serializable {

    private Long id;
    /** 账号 */
    private String  username;
    /** 昵称 */
    private String nickName;
    /** 签名 */
    private String autograph;
    /** 头像 */
    private String logo;
    /** 性别 */
    private Member.Gender gender;
    /** 所在地 */
    private AreaModel area;
    /** 职业 */
    private OccupationModel occupation;
    /** 是否绑定手机 */
    private Boolean bindMobile;
    /** 是否绑定微信 */
    private Boolean bindWeiXin;
    /** 是否设置密码 */
    private Boolean hasPassword;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
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

    public Member.Gender getGender() {
        return gender;
    }

    public void setGender(Member.Gender gender) {
        this.gender = gender;
    }

    public AreaModel getArea() {
        return area;
    }

    public void setArea(AreaModel area) {
        this.area = area;
    }

    public OccupationModel getOccupation() {
        return occupation;
    }

    public void setOccupation(OccupationModel occupation) {
        this.occupation = occupation;
    }

    public Boolean getBindMobile() {
        return bindMobile;
    }

    public void setBindMobile(Boolean bindMobile) {
        this.bindMobile = bindMobile;
    }

    public Boolean getBindWeiXin() {
        return bindWeiXin;
    }

    public void setBindWeiXin(Boolean bindWeiXin) {
        this.bindWeiXin = bindWeiXin;
    }

    public Boolean getHasPassword() {
        return hasPassword;
    }

    public void setHasPassword(Boolean hasPassword) {
        this.hasPassword = hasPassword;
    }

    public void bind(Member member) {
        this.id = member.getId();
        this.autograph = member.getAutograph();
        this.nickName = member.getNickName();
        this.username = member.getUsername();
        this.gender = member.getGender();
        AreaModel area = new AreaModel();
        if (member.getArea()!=null) {
            area.bind(member.getArea());
        }
        this.area = area;
        OccupationModel occupation = new OccupationModel();
        if (member.getOccupation()!=null) {
            occupation.bind(member.getOccupation());
        }
        this.occupation = occupation;
        this.logo = member.getLogo();
        this.hasPassword = (member.getPassword()!=null);
        this.bindMobile = (member.getMobile()!=null);
    }

}