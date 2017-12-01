package net.wit.controller.model;
import net.wit.entity.Member;
import net.wit.entity.Topic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class MemberOptionModel implements Serializable {

    private Long id;
    /**  黑名单 */
    private int black;
    /**  二维码 */
    private String qrcode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getBlack() {
        return black;
    }

    public void setBlack(int black) {
        this.black = black;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public void bind(Member member) {
        this.id = member.getId();
        this.black = 0;
        this.qrcode = member.getQrcode();
    }


}