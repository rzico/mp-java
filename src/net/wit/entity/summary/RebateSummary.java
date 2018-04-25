package net.wit.entity.summary;
import com.fasterxml.jackson.annotation.JsonInclude;
import net.wit.entity.Member;
import net.wit.entity.PayBill;
import net.wit.entity.Shop;

import java.io.Serializable;
import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RebateSummary implements Serializable {
    private Member member;

    /** 累计佣金 */
    private BigDecimal rebate;

    /** 分销佣金 */
    private BigDecimal direct;

    /** 渠道佣金 */
    private BigDecimal indirect;

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public BigDecimal getRebate() {
        return rebate;
    }

    public void setRebate(BigDecimal rebate) {
        this.rebate = rebate;
    }

    public BigDecimal getDirect() {
        return direct;
    }

    public void setDirect(BigDecimal direct) {
        this.direct = direct;
    }

    public BigDecimal getIndirect() {
        return indirect;
    }

    public void setIndirect(BigDecimal indirect) {
        this.indirect = indirect;
    }
}