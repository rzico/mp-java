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

public class TransferListModel extends BaseModel implements Serializable {

    private Long id;
    /** 头像 */
    private String logo;
    /** 昵称 */
    private String nickName;
    private String bankname;
    private String cardno;
    private String name;

    private BigDecimal amount;

    private BigDecimal fee;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
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
        Member member = transfer.getMember();
        this.id = transfer.getId();
        this.nickName = member.displayName();
        this.logo = member.getLogo();
        this.name = transfer.getName();
        this.bankname = transfer.getBankname();
        this.cardno = transfer.getCardno();
        this.amount = transfer.getAmount();
        this.fee = transfer.getFee();
    }


    public static List<TransferListModel> bindList(List<Transfer> transfers) {
        List<TransferListModel> ms = new ArrayList<TransferListModel>();
        for (Transfer transfer:transfers) {
            TransferListModel m = new TransferListModel();
            m.bind(transfer);
            ms.add(m);
        }
        return ms;
    }

}
