/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package net.wit.plat.alipay.msg;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayOpenPublicMessageTotalSendRequest;
import com.alipay.api.response.AlipayOpenPublicMessageTotalSendResponse;
import net.wit.plat.alipay.factory.AlipayAPIClientFactory;
import net.wit.plat.alipay.util.AlipayMsgBuildUtil;

/**
 * 开发者群发消息接口（群发纯文本消息）
 * 
 * @author baoxing.gbx
 * @version $Id: ToAlipayGroupSendTextMsg.java, v 0.1 Jul 25, 2014 3:42:09 PM baoxing.gbx Exp $
 */
public class ToAlipayGroupSendTextMsg {

    /**
     * 
     * @param args
     */
    public static void main(String[] args) {

        AlipayClient alipayClient = AlipayAPIClientFactory.getAlipayClient();

        // 使用SDK，构建群发请求模型
        AlipayOpenPublicMessageTotalSendRequest request = new AlipayOpenPublicMessageTotalSendRequest();
        request.setBizContent(AlipayMsgBuildUtil.buildGroupTextMsg());

        try {

            // 使用SDK，调用群发接口发送纯文本消息
        	AlipayOpenPublicMessageTotalSendResponse response = alipayClient.execute(request);

            //这里只是简单的打印，请开发者根据实际情况自行进行处理
            if (null != response && response.isSuccess()) {
                System.out.println("消息发送成功 : response = " + response.getBody());
            } else {
                System.out
                    .println("消息发送失败 code=" + response.getCode() + "msg=" + response.getMsg());
            }
        } catch (AlipayApiException e) {
            System.out.println("消息发送失败");
        }
    }

}
