package net.wit.controller.model.live;

/**
 * 用于消息群发
 */
public class UserInfo {

    public Long id;//这个是用于查看用户个人信息的
    public String imid;//这个是禁言跟提出成员使用的
    public String nickName;
    //        public String groupId;
    public String headPic;
    public String text;//发送的信息
    public String cmd;//消息类型
    public String time;//被禁言时长
    public String vip;//VIP等级


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImid() {
        return imid;
    }

    public void setImid(String imid) {
        this.imid = imid;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getVip() {
        return vip;
    }

    public void setVip(String vip) {
        this.vip = vip;
    }
}
