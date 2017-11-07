/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package net.wit.plat.alipay.factory;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import net.wit.plat.alipay.constants.AlipayServiceEnvConstants;

import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;


/**
 * API调用客户端工厂
 * 
 * @author taixu.zqq
 * @version $Id: AlipayAPIClientFactory.java, v 0.1 2014年7月23日 下午5:07:45 taixu.zqq Exp $
 */
public class AlipayAPIClientFactory {

    /** API调用客户端 */
    private static AlipayClient alipayClient;
    
    /**
     * 获得API调用客户端
     * 
     * @return
     */
    public static AlipayClient getAlipayClient(){
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        if(null == alipayClient){
            alipayClient = new DefaultAlipayClient(AlipayServiceEnvConstants.ALIPAY_GATEWAY,
                    bundle.getString("alipay.appid"),
                    bundle.getString("alipay.privateKey"),
                    "json",
                    AlipayServiceEnvConstants.CHARSET,bundle.getString("alipay.publicKey"),
                    AlipayServiceEnvConstants.SIGN_TYPE);
        }
        return alipayClient;
    }
}
