package net.wit.controller.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import net.wit.entity.Member;
import net.wit.entity.Topic;
import net.wit.entity.Transfer;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TransferModel extends BaseModel implements Serializable {

    private Long id;
    private String bankname;
    private String cardno;
    private String name;

    private BigDecimal amount;

    private BigDecimal fee;

    private Transfer.Status status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public String getCardno() {
        return cardno;
    }

    public void setCardno(String cardno) {
        this.cardno = cardno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public void bind(Transfer transfer) {
        this.id = transfer.getId();
        this.status = transfer.getStatus();
        this.name = transfer.getName();
        this.bankname = transfer.getBankname();
        this.cardno = transfer.getCardno();
        this.amount = transfer.getAmount();
        this.fee = transfer.getFee();
        this.status = transfer.getStatus();

    }


    public static List<TransferModel> bindList(List<Transfer> transfers) {
        List<TransferModel> ms = new ArrayList<TransferModel>();
        for (Transfer transfer:transfers) {
            TransferModel m = new TransferModel();
            m.bind(transfer);
            ms.add(m);
        }
        return ms;
    }

}
