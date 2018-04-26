package net.wit.entity.summary;
import com.fasterxml.jackson.annotation.JsonInclude;
import net.wit.entity.Deposit;

import java.io.Serializable;
import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class NihtanDepositSummary implements Serializable {
    //消费类型
    private String type;
    //消费金额
    private BigDecimal amount;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}