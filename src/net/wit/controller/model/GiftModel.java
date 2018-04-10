package net.wit.controller.model;
import net.wit.entity.Live;
import net.wit.entity.LiveGift;
import net.wit.entity.LiveTape;
import net.wit.entity.Product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class GiftModel extends BaseModel implements Serializable {

    private Long id;
    /**  名称  */
    private String name;

    /**  特效  */
    private String thumbnail;

    /**  价格  */
    private Long price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public void bind(LiveGift gift) {
        this.id = gift.getId();
        this.name = gift.getName();
        this.thumbnail = gift.getThumbnail();
        this.price = gift.getPrice();
    }


    public static List<GiftModel> bindList(List<LiveGift> liveGifts) {
        List<GiftModel> ms = new ArrayList<GiftModel>();
        for (LiveGift liveGift:liveGifts) {
            GiftModel m = new GiftModel();
            m.bind(liveGift);
            ms.add(m);
        }
        return ms;
    }
}