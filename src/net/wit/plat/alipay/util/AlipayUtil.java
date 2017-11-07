package net.wit.plat.alipay.util;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.request.AlipayUserUserinfoShareRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayUserUserinfoShareResponse;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.wit.plat.alipay.constants.AlipayServiceEnvConstants;
import net.wit.plat.alipay.factory.AlipayAPIClientFactory;
import net.wit.plat.weixin.pojo.AccessToken;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * Created by Administrator on 2017/5/24.
 * 支付宝平台通用接口工具类
 */
public class AlipayUtil {

    // 用户同意授权，获取code
    private static String oauth2_get_auth_code = "https://openauth.alipay.com/oauth2/publicAppAuthorize.htm?app_id=APPID&scope=SCOPE&redirect_uri=REDIRECT_URI";

    /**同步页面*/
    public static final String return_url = "/alipay/pay/doPay.jhtml";

    /**异步返回结果*/
    public static final String notify_url = "/alipay/pay/notify.jhtml";

    /**订单标题*/
    public static final String subject = "订单支付";

    /**授权访问令牌的授权类型*/
    public static final String product_code = "QUICK_WAP_PAY";

    /**
     *  * 获取access_token
     */
    public static AccessToken getOauth2AccessToken(String auth_code) throws ServletException,
            IOException {
        AccessToken accessToken = null;
            try {
                //3. 利用authCode获得authToken
                AlipaySystemOauthTokenRequest oauthTokenRequest = new AlipaySystemOauthTokenRequest();
                oauthTokenRequest.setCode(auth_code);
                oauthTokenRequest.setGrantType(AlipayServiceEnvConstants.GRANT_TYPE);
                AlipayClient alipayClient = AlipayAPIClientFactory.getAlipayClient();
                AlipaySystemOauthTokenResponse oauthTokenResponse = alipayClient.execute(oauthTokenRequest);
                //成功获得authToken
                if (null != oauthTokenResponse && oauthTokenResponse.isSuccess()) {

                    accessToken = new AccessToken();
                    accessToken.setToken(oauthTokenResponse.getAccessToken());
                    //accessToken.setExpiresIn(oauthTokenResponse.getExpiresIn());
                    accessToken.setOpenid(oauthTokenResponse.getUserId());
                    accessToken.setRefreshToken(oauthTokenResponse.getRefreshToken());
                }
            } catch (AlipayApiException alipayApiException) {
                accessToken = null;
            }
        return accessToken;
    }

    /**
     * 使用auth_code获取用户信息
     * @param request
     * @param response
     * @return JSONObject
     */
    public static JSONObject getUserInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
//1. 解析请求参数
        Map<String, String> params = RequestUtil.getRequestParams(request);
        //2. 获得authCode
        String authCode = params.get("auth_code");
        try {
            //3. 利用authCode获得authToken
            AlipaySystemOauthTokenRequest oauthTokenRequest = new AlipaySystemOauthTokenRequest();
            oauthTokenRequest.setCode(authCode);
            oauthTokenRequest.setGrantType(AlipayServiceEnvConstants.GRANT_TYPE);
            AlipayClient alipayClient = AlipayAPIClientFactory.getAlipayClient();
            AlipaySystemOauthTokenResponse oauthTokenResponse = alipayClient
                    .execute(oauthTokenRequest);

            //成功获得authToken
            if (null != oauthTokenResponse && oauthTokenResponse.isSuccess()) {

                //4. 利用authToken获取用户信息
                AlipayUserUserinfoShareRequest userinfoShareRequest = new AlipayUserUserinfoShareRequest();

                AlipayUserUserinfoShareResponse userinfoShareResponse = alipayClient.execute(
                        userinfoShareRequest, oauthTokenResponse.getAccessToken());
                //成功获得用户信息
                if (null != userinfoShareResponse && userinfoShareResponse.isSuccess()) {
                    //System.out.println("user_info:" + userinfoShareResponse.getBody());
                    String user_info = userinfoShareResponse.getBody();
                    JSONObject jsonObject = JSONObject.fromObject(user_info);
                    return  jsonObject;
                } else {
                    //这里仅是简单打印， 请开发者按实际情况自行进行处理

                }
            } else {
            }
        } catch (AlipayApiException alipayApiException) {
            //自行处理异常
            alipayApiException.printStackTrace();
        }
        return  null;
    }

    /**
     * 使用authToken获取用户信息
     * @param authToken
     * @return JSONObject
     */
    public static JSONObject getUserInfoByToken(String authToken) throws ServletException,
            IOException {
        try {

                AlipayClient alipayClient = AlipayAPIClientFactory.getAlipayClient();
                //4. 利用authToken获取用户信息
                AlipayUserUserinfoShareRequest userinfoShareRequest = new AlipayUserUserinfoShareRequest();

                AlipayUserUserinfoShareResponse userinfoShareResponse = alipayClient.execute(
                        userinfoShareRequest, authToken);
                //成功获得用户信息
                if (null != userinfoShareResponse && userinfoShareResponse.isSuccess()) {
                    //System.out.println("user_info:" + userinfoShareResponse.getBody());
                    String user_info = userinfoShareResponse.getBody();
                    JSONObject jsonObject = JSONObject.fromObject(user_info);
                    return  jsonObject;
                } else {
                    //这里仅是简单打印， 请开发者按实际情况自行进行处理

                }
        } catch (AlipayApiException alipayApiException) {
            //自行处理异常
            alipayApiException.printStackTrace();
        }
        return  null;
    }

    /**
     * 支付
     * @return JSONObject
     */
    public static String pay(String orderId, BigDecimal amount) throws ServletException,
            IOException {
        orderId = "20170530"+String.valueOf((int)((Math.random()*9+1)*10000000));
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        AlipayClient alipayClient = AlipayAPIClientFactory.getAlipayClient();
        AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();//创建API对应的request
        alipayRequest.setReturnUrl(bundle.getString("WeiXinSiteUrl") + return_url);
        alipayRequest.setNotifyUrl(bundle.getString("WeiXinSiteUrl") + notify_url);//在公共参数中设置回跳和通知地址
        alipayRequest.setBizContent("{" +
                "    \"out_trade_no\":\""+orderId+"\"," +
                "    \"total_amount\":\"0.01\"," +
                "    \"subject\":\""+subject+"\"," +
                "    \"product_code\":\""+product_code+"\"" +
                "  }");//填充业务参数
        String form="";
        try {
            form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
            return  form;
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return  form;
    }

    public static String codeUrlO2(String url) {
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        String appId = bundle.getString("alipay.appId");
        // 第三方用户唯一凭证密钥
        try {
            url = AlipayUtil.getOauth2Code(appId,url, "auth_base");//auth_userinfo 获取用户信息
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     *  * 获取code 
     */
    public static String getOauth2Code(String appid, String redirectUri, String scope) {
        try {
            return AlipayUtil.oauth2_get_auth_code.replace("APPID", appid).replace("REDIRECT_URI", redirectUri).replace("SCOPE", scope);
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
