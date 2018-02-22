package net.wit.controller.model;
import net.wit.entity.Friends;
import net.wit.entity.Member;
import net.wit.entity.Topic;
import net.wit.entity.TopicConfig;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class TopicOptionModel extends BaseModel implements Serializable {

    public TopicConfig.PromoterType promoterType;

    public TopicConfig.PromoterType getPromoterType() {
        return promoterType;
    }

    public void setPromoterType(TopicConfig.PromoterType promoterType) {
        this.promoterType = promoterType;
    }

    public void bind(Member member) {
        Topic topic = member.getTopic();
        if (topic!=null) {
            this.promoterType = topic.getConfig().getPromoterType();
        } else {
            this.promoterType = TopicConfig.PromoterType.any;
        }
    }
}
