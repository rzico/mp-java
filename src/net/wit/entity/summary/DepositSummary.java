package net.wit.entity.summary;
import com.fasterxml.jackson.annotation.JsonInclude;
import net.wit.entity.Deposit;
import net.wit.entity.PayBill;
import net.wit.entity.Shop;

import java.io.Serializable;
import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DepositSummary extends BaseModel implements Serializable {
    //消费类型
    private Deposit.Type type;
    //消费金额
    private BigDecimal amount;

    public Deposit.Type getType() {
        return type;
    }

    public void setType(Deposit.Type type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}