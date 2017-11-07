/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package net.wit.plat.alipay.executor;

import net.sf.json.JSONObject;
import net.wit.plat.alipay.common.MyException;
import net.wit.plat.alipay.util.AlipayMsgBuildUtil;


/**
 * 默认执行器(该执行器仅发送ack响应)
 * 
 * @author baoxing.gbx
 * @version $Id: InAlipayDefaultExecutor.java, v 0.1 Jul 30, 2014 10:22:11 AM baoxing.gbx Exp $
 */
public class InAlipayDefaultExecutor implements ActionExecutor {

    /** 业务参数 */
    private JSONObject bizContent;

    public InAlipayDefaultExecutor(JSONObject bizContent) {
        this.bizContent = bizContent;
    }

    public InAlipayDefaultExecutor() {
        super();
    }

    /**
     * 
     *
     */
    @Override
    public String execute() throws MyException {

        //取得发起请求的支付宝账号id
        final String fromUserId = bizContent.getString("FromUserId");

        return AlipayMsgBuildUtil.buildBaseAckMsg(fromUserId);
    }
}
