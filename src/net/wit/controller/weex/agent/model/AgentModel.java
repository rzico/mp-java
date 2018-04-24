package net.wit.controller.weex.agent.model;
import net.wit.controller.model.BaseModel;
import net.wit.entity.Member;
import net.wit.entity.Message;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AgentModel extends BaseModel implements Serializable {

    private Long memberId;

    /** 昵称 */
    private String nickName;

    /** 头像 */
    private String logo;

    /** 累计佣金 */
    private BigDecimal rebate;

    /** 分销佣金 */
    private BigDecimal direct;

    /** 渠道佣金 */
    private BigDecimal indirect;


    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
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