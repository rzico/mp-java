package net.wit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.search.annotations.DateBridge;
import org.hibernate.search.annotations.Resolution;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Eric on 2018/4/30.
 */
@Entity
@Table(name = "wx_operation_record")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "wx_operation_record_sequence")
public class OperationRecord extends BaseEntity {

    /** 第一张图 */
    @Column(columnDefinition="varchar(255) comment '第一张图片地址'")
    private String oneImgUrl;

    /** 第二张图 */
    @Column(columnDefinition="varchar(255) comment '第二张图片地址'")
    private String twoImgUrl;

    /** 合成图片地址 */
    @Column(columnDefinition="varchar(255) comment '合成图片地址'")
    private String synthesisImg;

    public String getOneImgUrl() {
        return oneImgUrl;
    }

    public void setOneImgUrl(String oneImgUrl) {
        this.oneImgUrl = oneImgUrl;
    }

    public String getTwoImgUrl() {
        return twoImgUrl;
    }

    public void setTwoImgUrl(String twoImgUrl) {
        this.twoImgUrl = twoImgUrl;
    }

    public String getSynthesisImg() {
        return synthesisImg;
    }

    public void setSynthesisImg(String synthesisImg) {
        this.synthesisImg = synthesisImg;
    }
}
