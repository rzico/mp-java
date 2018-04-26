package net.wit.controller.model;
import net.wit.entity.LiveGift;
import net.wit.entity.LiveGiftData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GiftDataModel extends BaseModel implements Serializable {

    private Long id;
    /**  名称  */
    private String giftName;
    /**  缩例图  */
    private String thumbnail;

    private Long memberId;
    /**  名称  */
    private String nickname;
    /**  缩例图  */
    private String headpic;

    /**  价格  */
    private Long price;

    private Date createDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeadpic() {
        return headpic;
    }

    public void setHeadpic(String headpic) {
        this.headpic = headpic;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void bind(LiveGiftData gift) {
        this.id = gift.getId();
        this.giftName = gift.getGiftName();
        this.thumbnail = gift.getThumbnail();
        this.createDate = gift.getCreateDate();
        this.price = gift.getPrice();
        this.nickname = gift.getNickname();
        this.headpic = gift.getHeadpic();
        this.memberId = gift.getMember().getId();
    }


    public static List<GiftDataModel> bindList(List<LiveGiftData> liveGifts) {
        List<GiftDataModel> ms = new ArrayList<GiftDataModel>();
        for (LiveGiftData liveGift:liveGifts) {
            GiftDataModel m = new GiftDataModel();
            m.bind(liveGift);
            ms.add(m);
        }
        return ms;
    }
}