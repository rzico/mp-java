package net.wit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric on 2018/4/30.
 */
@Entity
@Table(name = "wx_small_range")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_small_range_sequence")
public class SmallRange extends BaseEntity{

    /** 昵称 */
    @Column(columnDefinition="varchar(255) comment '昵称'")
    private String nickName;

    /** 头像 */
    @Column(columnDefinition="varchar(255) comment '头像地址'")
    private String avatarUrl;

    /** 性别 */
    @Column(columnDefinition="int(11) comment '性别 {male:男,female:女,secrecy:保密}'")
    private Member.Gender gender;

    /** 语言 */
    @Column(columnDefinition="varchar(255) comment '语言'")
    private String language;

    /** 城市 */
    @Column(columnDefinition="varchar(255) comment '城市'")
    private String city;

    /** 省份 */
    @Column(columnDefinition="varchar(255) comment '省份'")
    private String province;

    /** 国家 */
    @Column(columnDefinition="varchar(255) comment '国家'")
    private String country;

    /** 操作记录*/
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
    @OrderBy("modifyDate desc")
    @JsonIgnore
    private List<OperationRecord> operationRecords =new ArrayList<OperationRecord>();

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Member.Gender getGender() {
        return gender;
    }

    public void setGender(Member.Gender gender) {
        this.gender = gender;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<OperationRecord> getOperationRecords() {
        return operationRecords;
    }

    public void setOperationRecords(List<OperationRecord> operationRecords) {
        this.operationRecords = operationRecords;
    }
}
