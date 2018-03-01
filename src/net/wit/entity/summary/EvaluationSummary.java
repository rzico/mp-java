package net.wit.entity.summary;

import net.wit.entity.Card;
import net.wit.entity.Member;

import java.io.Serializable;
import java.math.BigDecimal;

public class EvaluationSummary extends BaseModel implements Serializable {
    private Member member;
    /**  单数 */
    private Long count;

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}