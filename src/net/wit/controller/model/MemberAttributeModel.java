package net.wit.controller.model;
import net.wit.entity.Member;

import java.io.Serializable;
import java.util.Date;

public class MemberAttributeModel extends BaseModel implements Serializable {

    private Long id;
    /** 登录名 */
    private String  userId;
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
    /** 推荐人 */
    private String promoter;
    /** 生日 */
    private Date birthday;
    /** 是否绑定手机 */
    private Boolean bindMobile;
    /** 手机号后几位 */
    private String mobile;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPromoter() {
        return promoter;
    }

    public void setPromoter(String promoter) {
        this.promoter = promoter;
    }

    public void bind(Member member) {
        this.id = member.getId();
        this.userId = member.userId();
        this.autograph = member.getAutograph();
        this.nickName = member.getNickName();
        this.username = member.getUsername();
        this.gender = member.getGender();
        this.birthday = member.getBirth();
        AreaModel area = new AreaModel();
        if (member.getArea()!=null) {
            area.bind(member.getArea());
        }
        this.area = area;
        OccupationModel occupation = new OccupationModel();
        if (member.getOccupation()!=null) {
            occupation.bind(member.getOccupation());
        }
        if (member.getPromoter()!=null) {
            if (member.getPromoter().getName()!=null) {
                this.promoter = member.getPromoter().getMobile() + "(" + member.getPromoter().getName() + ")";
            } else {
                this.promoter = member.getPromoter().getMobile() + "(" + member.getPromoter().displayName() + ")";
            }
        }
        this.occupation = occupation;
        this.logo = member.getLogo();
        this.hasPassword = (member.getPassword()!=null);
        this.bindMobile = (member.getMobile()!=null && !member.getMobile().equals("") && member.getMobile().length() == 13);
        if (this.bindMobile) {
            this.mobile = member.getMobile().substring(member.getMobile().length()-4,member.getMobile().length());
        }
    }

}