

/**

 * Alipay.com Inc.

 * Copyright (c) 2004-2014 All Rights Reserved.

 */

package net.wit.plat.alipay.constants;


/**
 * 支付宝服务窗环境常量（demo中常量只是参考，需要修改成自己的常量值）
 * 
 * @author taixu.zqq
 * @version $Id: AlipayServiceConstants.java, v 0.1 2014年7月24日 下午4:33:49 taixu.zqq Exp $
 */
public class AlipayServiceEnvConstants {

    /**支付宝公钥-从支付宝生活号详情页面获取*/
	public static final String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAyZws3kBswVO0bsQZwHgf5lYB5fZtKPy/EdKv5jG6z/CdvAm8sH3iaqC3NaBWc0hOOcAHf3SHp6EZhdBtHQwZ5/oxTF5XhsXnbEFZcsXYuJrTbzfpVHbh7kNCnSKEaZNbmjxaxXtXKCLl0obWH8VV2cnsOX5q0omINjsET1Yf/NxYopcU9SJaxLso+9YFolpoO/BJusdjno4Mx9jpUAp9TXkhgnt4selT0ApAME/np6K48SBFNPwQh2RcFpRfyDsT9Q70P3jBb6jOpiydzEzCALgoPHY7MmKugT6Kd9l/9BHZawGScXmENs1bInOy2rNu2SAK0OvMigfXS02pDLuaRQIDAQAB";
    
    /**签名编码-视支付宝服务窗要求*/
    public static final String SIGN_CHARSET      = "UTF-8";

    /**字符编码-传递给支付宝的数据编码*/
    public static final String CHARSET           = "UTF-8";

    /**签名类型-视支付宝服务窗要求*/
    public static final String SIGN_TYPE         = "RSA2";
    
    /**开发者账号PID*/
    public static final String PARTNER           = "2088911449293974";

    /** 服务窗appId  */
    //TODO !!!! 注：该appId必须设为开发者自己的生活号id  
    public static final String APP_ID            = "2017040406544008";

    //TODO !!!! 注：该私钥为测试账号私钥  开发者必须设置自己的私钥 , 否则会存在安全隐患 
    public static final String PRIVATE_KEY       = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQC//8dqa21C0Z2rHCQPWULQJuyS4Cn34XxEfb+3hvnkNDHXjGb3LMUFRZQWfNAPA0EXHvRqJNFUgHhEokavIDojrESvABQExUzxzPMg3dYv+7l7wYEw3omfkKHZaEZzorSxMw5OvJuDv+5n2e15WCawMaGiCvciEtJsehvzsT+0LmSxCx2E1OKLgo2CFZN4dZ4CfHS1aNlPpKKEnIGnABldkKwSPWZJ3P1+MxcQbLncomxW5IxIhj73hYdlEVcLhR7qnUp7YhPup69X9QAvob81QhsUhF1KB/4nIUkfBHaiqXmF12lVXM5AdDpe9BTbogPOOmam8UqlC2I9HtaEnnALAgMBAAECggEAZyV6dGmSITuLgMl1del2Q20l2yIiadDxebo0rNwOk9KWlD4RcujST6q5i/sxq18MMOeoVv3Al60M75JedubRVSjPHVTlbEqys5RvaVDEKLMLSrkCNsbXGKoSyUGD18upVykenp4kzfrmkVQ6h94bqrx/IRbS/bMu6KgRLDIlDx6NBHwLfzXaHJvYcCc4dvIbRpEzIyrc+1eftUMsm1RCUEX9h3dPWIC3TlNQoGnkC1avlsPrU5hv1Rocx3c2cEz9Nm3WTI2XurmrPR8mISS170lHfi46mZBH1jW58ebgANDGeyUgXKaLN6fXGiK8alo4Z664beqL1cwhs7mqtqa4AQKBgQD91KKvp2Z/j/vFN57r/+BBdkoSjTZONWYoeoobUPtMQO2C0g8XPAQOZD7b0eDbF0VXdwRMg8olMZdEV2rjedBlCqDC6Rm94acJxKLcE10iAcK8mGzT+NBkFOaSC5KregHejT2RL6v7J1Z1geVW6UqYnNc+J9FxCYeLYCl6ljK9gQKBgQDBo9w+M4LLUbBk987zGoGRawIXUgkCiSNZFJRVte6228bzZ7NKdctS7Ph8P+ySeVFAhP25sYMDKdmoFKjaAfJJWh5KiQFlwQYwBokWNLyBmaW2CEt5q+GPb/TpZP2s2u+QpZFxSs9sGDi1yJGhwYxGIC0AngxosyG2Z/T2mW0LiwKBgQCZbrIUWa00iJVY5kEzSM2G5Hjr894SsmaZK1FWJ8JvmoO3Y0Bp/AY7qM7yU1CHPu7UawX3Nma3yt00G19qN5UHrr/0cbD0vXUaC3mibDYMu2G6XKzUCP/2r0ecmlBDRRHiNCA2JcngnCvABg/aVFSaIZxeXvVUTb1YO313M1ligQKBgQC675mbYtcNuVr03lanUhtra0alhMi17fZ19OwOoohFd35lVOM9KuKSt7pbGMslzICoLLtVHQ1BbJF3Nhqb++0O6zT697ClruZuTqDhEyA4bUAQr4lNeKVUd9yTxK8wxFsp5Oy31mTQEcINnAcbhWIKAN3xDqMkIGqXUVynBA7FaQKBgQDbFSevLV7xUSlliHsXLl7v4mN5SMmYkN/427vUPc6En8GiXbfOtGUB6JURUdaf3B2uE0FUoE/3FUgeGdnPpPg5tqGnWG7TNzX5JVcUzq1lkLaHVQz0EGKC0GqZt8rofce76rUXppUVt5ayknPCguMW6iG5NdQYQj+NsqLxlszCug==";
    
    //TODO !!!! 注：该公钥为测试账号公钥  开发者必须设置自己的公钥 ,否则会存在安全隐患
    public static final String PUBLIC_KEY        = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAv//HamttQtGdqxwkD1lC0CbskuAp9+F8RH2/t4b55DQx14xm9yzFBUWUFnzQDwNBFx70aiTRVIB4RKJGryA6I6xErwAUBMVM8czzIN3WL/u5e8GBMN6Jn5Ch2WhGc6K0sTMOTrybg7/uZ9nteVgmsDGhogr3IhLSbHob87E/tC5ksQsdhNTii4KNghWTeHWeAnx0tWjZT6SihJyBpwAZXZCsEj1mSdz9fjMXEGy53KJsVuSMSIY+94WHZRFXC4Ue6p1Ke2IT7qevV/UAL6G/NUIbFIRdSgf+JyFJHwR2oql5hddpVVzOQHQ6XvQU26IDzjpmpvFKpQtiPR7WhJ5wCwIDAQAB";
    /**支付宝网关*/
    public static final String ALIPAY_GATEWAY    = "https://openapi.alipay.com/gateway.do";

    /**授权访问令牌的授权类型*/
    public static final String GRANT_TYPE        = "authorization_code";
}