package net.wit.controller.model;
import net.wit.entity.Friends;
import net.wit.entity.Member;
import net.wit.entity.Topic;
import net.wit.entity.TopicConfig;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class TopicOptionModel extends BaseModel implements Serializable {

    public TopicConfig.PromoterType promoterType;
    public TopicConfig.Pattern pattern;
    public BigDecimal amount;

    public TopicConfig.PromoterType getPromoterType() {
        return promoterType;
    }

    public void setPromoterType(TopicConfig.PromoterType promoterType) {
        this.promoterType = promoterType;
    }

    public TopicConfig.Pattern getPattern() {
        return pattern;
    }

    public void setPattern(TopicConfig.Pattern pattern) {
        this.pattern = pattern;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void bind(Member member) {
        Topic topic = member.getTopic();
        if (topic!=null) {
            this.promoterType = topic.getConfig().getPromoterType();
            this.pattern = topic.getConfig().getPattern();
            this.amount = topic.getConfig().getAmount();
        } else {
            this.promoterType = TopicConfig.PromoterType.any;
            this.pattern = TopicConfig.Pattern.pattern1;
            this.amount = BigDecimal.ZERO;
        }
    }
}
