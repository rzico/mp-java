package net.wit.plat.weixin.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.wit.entity.VerifyTicket;
import net.wit.entity.weixin.Domain;
import net.wit.entity.weixin.WeiXinCallBack;
import net.wit.plat.weixin.pojo.*;
import net.wit.util.DateUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 微信公众平台通用接口工具类
 *
 * @author Administrator
 */
public class WeixinApi {

    private static Logger log = LoggerFactory.getLogger(WeixinApi.class);

    private static AccessToken accessToken = null;

    //	private static HashMap<String, ComponentAccessToken> componentAccessTokenHashMap = new HashMap<>();
    private static HashMap<String, AuthAccessToken> authAccessTokenHashMap = new HashMap<>();

    private static ComponentAccessToken componentAccessToken = null;
//	private static AuthAccessToken authAccessToken = null;

    private static Ticket jsapi_ticket = null;

    private static Ticket wxcard_ticket = null;
//
//	public static VerifyTicket verify_ticket = null;

    // 获取access_token的接口地址（GET） 限200（次/天） 
    private static String access_token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

    // 获取oauth2 access_token的接口地址（GET） 限200（次/天） 
    private static String oauth2_access_token_url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";

    // 菜单创建（POST） 限100（次/天）
    private static String menu_create_url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";

    // 菜单查询（GET） 限1000（次/天）
    private static String menu_get_url = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=ACCESS_TOKEN";

    // 菜单删除（GET） 限100（次/天）
    private static String menu_delete_url = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";

    // 获取关注者列表
    private static String user_getlist_url = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN&next_openid=NEXT_OPENID";

    // 使用code换取access_token
    private static String code_at_url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";

    // 使用refresh_token换取access_token
    private static String refresh_at_url = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=APPID&grant_type=refresh_token&refresh_token=REFRESH_TOKEN";


    // 使用access_token获取用户信息
    private static String user_getinfo_url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";


    private static String user_getinfo_byCode_url = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";

    // 创建永久二维码
    private static String create_qrcode = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=ACCESS_TOKEN";

    // 根据OpenID列表群发
    private static String massSendUrl = "https://api.weixin.qq.com/cgi-bin/message/mass/send?access_token=ACCESS_TOKEN";

    // 用户同意授权，获取code
    private static String oauth2_get_code = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=123#wechat_redirect";

    private static String GETJSAPITICKET = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";

    private static String GETWXCARDTICKET = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=wx_card";

    private static String send_message = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";

    //获取小程序信息接口
    private static final String SMALLINFORMATION = "https://api.weixin.qq.com/cgi-bin/component/api_get_authorizer_info?component_access_token=COMPONENT_ACCESS_TOKEN";

    //获取第三方平台component_access_token
    private static final String COMPONENTTOKEN = "https://api.weixin.qq.com/cgi-bin/component/api_component_token";

    //获取预授权码
    private static final String PREAUTHCODE = "https://api.weixin.qq.com/cgi-bin/component/api_create_preauthcode?component_access_token=COMPONENT_TOKEN";

    //授权码换取调用接口调用凭据 authaccesstoken
    private static final String CODEANDTOKEN = "https://api.weixin.qq.com/cgi-bin/component/api_query_auth?component_access_token=COMPONENT_TOKEN";

    //提交小程序代码
    private static final String COMMITCODE = "https://api.weixin.qq.com/wxa/commit?access_token=AUTH_TOKEN";

    //提交审核
    private static final String PUSHCODE = "https://api.weixin.qq.com/wxa/submit_audit?access_token=AUTH_TOKEN";

    //发布已通过审核的小程序
    private static final String RELEASECODE = "https://api.weixin.qq.com/wxa/release?access_token=AUTH_TOKEN";

    //版本回退
    private static final String REVERTCODE = "https://api.weixin.qq.com/wxa/revertcoderelease?access_token=AUTH_TOKEN";

    //设置小程序服务器域名
    public static final String SETDOMAIN1 = "https://api.weixin.qq.com/wxa/modify_domain?access_token=AUTH_TOKEN";

    //设置小程序业务域名（仅供第三方代小程序调用）
    public static final String SETDOMAIN2 = "https://api.weixin.qq.com/wxa/setwebviewdomain?access_token=AUTH_TOKEN";

    //设置小程序隐私设置（是否可被搜索）
    public static final String SETAPPLETSTATUS = "https://api.weixin.qq.com/wxa/changewxasearchstatus?access_token=AUTH_TOKEN";

    // 查询小程序当前隐私设置（是否可被搜索）
    public static final String GETAPPLETSTATUS = "https://api.weixin.qq.com/wxa/getwxasearchstatus?access_token=AUTH_TOKEN";
//	//通过刷新token 获取 authaccesstoken
//	private static final String AUTHTOKEN = "https:// api.weixin.qq.com /cgi-bin/component/api_authorizer_token?component_access_token=COMPONENT_TOKEN";


    /**
     * 发起https请求并获取结果
     *
     * @param requestUrl    请求地址
     * @param requestMethod 请求方式（GET、POST）
     * @param outputStr     提交的数据
     * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
     */
    public static JSONObject httpRequest(String requestUrl, String requestMethod, String outputStr) {
        JSONObject jsonObject = null;
        StringBuffer buffer = new StringBuffer();
        try {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = {new MyX509TrustManager()};
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url = new URL(requestUrl);
            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
            httpUrlConn.setSSLSocketFactory(ssf);

            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            httpUrlConn.setRequestMethod(requestMethod);

            if ("GET".equalsIgnoreCase(requestMethod))
                httpUrlConn.connect();

            // 当有数据需要提交时
            if (null != outputStr) {
                OutputStream outputStream = httpUrlConn.getOutputStream();
                // 注意编码格式，防止中文乱码
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }

            // 将返回的输入流转换成字符串
            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            // 释放资源
            inputStream.close();
            inputStream = null;
            httpUrlConn.disconnect();
            jsonObject = JSONObject.fromObject(buffer.toString());
        } catch (ConnectException ce) {
            log.error("Weixin server connection timed out.");
        } catch (Exception e) {
            log.error("https request error:{}", e);
        }
        return jsonObject;
    }

    /**
     * add添加, delete删除, set覆盖, get获取
     */
    public enum ACTION {
        ADD,
        DELETE,
        SET,
        GET
    }

    /**
     * 获取小程序信息
     *
     * @param token           component_access_token
     * @param authorizerAppid 授权方appid
     * @param componentAppid  第三方平台appid
     */
    public static SmallInformation getSmallInformation(String token, String componentAppid, String authorizerAppid) {
        String string = "{\"component_appid\":\"" + componentAppid + "\" ,\"authorizer_appid\": \"" + authorizerAppid + "\"}";
        JSONObject jsonObject = WeixinApi.httpRequest(WeixinApi.SMALLINFORMATION.replace("COMPONENT_ACCESS_TOKEN", token), "POST", string);
        SmallInformation smallInformation = new SmallInformation();
        if (jsonObject.get("authorization_info") != null) {
            List<FuncInfo> list = new ArrayList<>();
            JSONArray jsonArray = jsonObject.getJSONObject("authorization_info").getJSONArray("func_info");
            for (int i = 0; i < jsonArray.size(); i++) {
                if (jsonArray.get(i) != null) {
                    FuncInfo funcInfo = new FuncInfo();
                    FuncInfo.Category category = new FuncInfo.Category();
                    category.setId(jsonArray.getJSONObject(i).getLong("id"));
                    funcInfo.setFuncscope_category(category);
                    list.add(funcInfo);
                }
            }
            smallInformation.setFuncInfo(list);
        }
        if (jsonObject.getJSONObject("authorizer_info") != null) {
            AuthorizerInfo authorizerInfo = new AuthorizerInfo();
            JSONObject object = jsonObject.getJSONObject("authorizer_info");
            authorizerInfo.setNickName(object.getString("nick_name"));
            authorizerInfo.setHeadImg(object.getString("head_img"));
            authorizerInfo.setUserName("user_name");
            authorizerInfo.setPrincipalName("principal_name");
            authorizerInfo.setQrcodeUrl("qrcode_url");
            authorizerInfo.setSignature("signature");
            if(object.getJSONObject("verify_type_info")!=null){
                AuthorizerInfo.VerifyTypeInfo verifyTypeInfo = new AuthorizerInfo.VerifyTypeInfo();
                verifyTypeInfo.setId(object.getJSONObject("verify_type_info").getLong("id"));
            }
            if(object.getJSONArray("categories")!=null&&object.getJSONArray("categories").size()>0){
                List<AuthorizerInfo.Categories> list =new ArrayList<>();
                JSONArray array=object.getJSONArray("categories");
                for(int i=0;i<array.size();i++){
                    if(array.getJSONObject(i)!=null){
                        AuthorizerInfo.Categories categories = new AuthorizerInfo.Categories();
                        categories.setFirst(array.getJSONObject(i).getString("first"));
                        categories.setSecond(array.getJSONObject(i).getString("second"));
                        list.add(categories);
                    }
                }
                authorizerInfo.setCategories(list);
                smallInformation.setAuthorizerInfo(authorizerInfo);
            }
        }
        return smallInformation;
    }

    /**
     * 设置小程序服务器域名
     */
    public static Domain setDomain1(String authToken, ACTION action) {

        String actionStr = action.name().toLowerCase();
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        String serverUrl = "https://" + bundle.getString("weixin.component.url");
        String mapUrl = "https://apis.map.qq.com";
        String params = "{" +
                "\"action\":\"" + actionStr + "\"";
        if (action == ACTION.GET) {
            params = params + "}";
        } else {
            //目前标准的 是这样 先写死
            params = params +
                    "\"requestdomain\":[\"" + serverUrl + "\",\"" + mapUrl + "\"]," +
                    "\"wsrequestdomain\":[\"" + serverUrl + "\"]," +
                    "\"uploaddomain\":[\"" + serverUrl + "\"]," +
                    "\"downloaddomain\":[\"" + serverUrl + "\"]" +
                    "}";
        }
        JSONObject jsonObject = WeixinApi.httpRequest(SETDOMAIN1.replace("AUTH_TOKEN", authToken), "POST", params);
        Domain domain = null;
        if (jsonObject != null) {
            domain = new Domain();
            domain.setErrmsg(jsonObject.getString("errmsg"));
            domain.setErrcode(jsonObject.getString("errcode"));
            if (action == ACTION.GET) {
                JSONArray array1 = jsonObject.getJSONArray("downloaddomain");
                JSONArray array2 = jsonObject.getJSONArray("uploaddomain");
                JSONArray array3 = jsonObject.getJSONArray("wsrequestdomain");
                JSONArray array4 = jsonObject.getJSONArray("requestdomain");
                int len1 = array1.size();
                int len2 = array2.size();
                int len3 = array3.size();
                int len4 = array4.size();
                String[] strings1 = new String[len1];
                String[] strings2 = new String[len2];
                String[] strings3 = new String[len3];
                String[] strings4 = new String[len4];
                for (int i = 0; i < len1; i++) {
                    strings1[i] = array1.getString(i);
                    domain.setDownloaddomain(strings1);
                }
                for (int i = 0; i < len2; i++) {
                    strings2[i] = array2.getString(i);
                    domain.setUploaddomain(strings2);
                }
                for (int i = 0; i < len3; i++) {
                    strings3[i] = array3.getString(i);
                    domain.setWsrequestdomain(strings3);
                }
                for (int i = 0; i < len4; i++) {
                    strings4[i] = array4.getString(i);
                    domain.setRequestdomain(strings4);
                }
            }
        }
        return domain;
    }

    /**
     * 设置小程序业务域名（仅供第三方代小程序调用）
     */
    public static WeiXinCallBack setDomain2(String authToken) {
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        String serverUrl = "https://" + bundle.getString("weixin.component.url");
        String mapUrl = "https://apis.map.qq.com";
        String params = "{" +
                "\"action\":\"add\"," +
                "\"webviewdomain\":[\"" + serverUrl + "\",\"" + mapUrl + "\"]" +
                "}";
        JSONObject jsonObject = WeixinApi.httpRequest(SETDOMAIN2.replace("AUTH_TOKEN", authToken), "POST", params);

        WeiXinCallBack weiXinCallBack = null;
        if (jsonObject != null) {
            weiXinCallBack = new WeiXinCallBack();
            weiXinCallBack.setErrmsg(jsonObject.getString("errmsg"));
            weiXinCallBack.setErrcode(jsonObject.getString("errcode"));
        }
        return weiXinCallBack;
    }

    /**
     * 设置小程序隐私设置（是否可被搜索）
     * status 1表示不可搜索，0表示可搜索
     */
    public static WeiXinCallBack setAppletStatus(String authToken, int status) {
        String params = "{" +
                "\"status\":" + status + "" +
                "}";
        JSONObject jsonObject = WeixinApi.httpRequest(SETAPPLETSTATUS.replace("AUTH_TOKEN", authToken), "POST", params);

        WeiXinCallBack weiXinCallBack = null;
        if (jsonObject != null) {
            weiXinCallBack = new WeiXinCallBack();
            weiXinCallBack.setErrmsg(jsonObject.getString("errmsg"));
            weiXinCallBack.setErrcode(jsonObject.getString("errcode"));
        }
        return weiXinCallBack;
    }

    /**
     * 查询小程序当前隐私设置（是否可被搜索）
     * status 1表示不可搜索，0表示可搜索
     */

    public static int getAppletStatus(String authToken) {

        JSONObject jsonObject = WeixinApi.httpRequest(GETAPPLETSTATUS.replace("AUTH_TOKEN", authToken), "GET", null);
        int status = 0;
        if (jsonObject != null) {
            status = jsonObject.getInt("status");
        }
        return status;
    }

    /**
     * 发布已经通过审核的 小程序
     */
    public static boolean releaseAppletCode(String authToken) {
        String params = "{}";
        JSONObject jsonObject = WeixinApi.httpRequest(RELEASECODE.replace("AUTH_TOKEN", authToken), "POST", params);
        if (jsonObject != null) {
            /**
             -1	系统繁忙
             85019	没有审核版本
             85020	审核状态未满足发布
             */
            String errcode = jsonObject.getString("errcode");
            String errmsg = jsonObject.getString("errmsg");
            if (errmsg.equals("ok")) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 小程序版本回退
     * REVERTCODE
     */
    public static boolean revertAppletCode(String authToken) {
        JSONObject jsonObject = WeixinApi.httpRequest(REVERTCODE.replace("AUTH_TOKEN", authToken), "GET", null);
        if (jsonObject != null) {
            /**
             0	成功
             -1	系统错误
             87011	现网已经在灰度发布，不能进行版本回退
             87012	该版本不能回退，可能的原因：1:无上一个线上版用于回退 2:此版本为已回退版本，不能回退 3:此版本为回退功能上线之前的版本，不能回退
             */
            String errcode = jsonObject.getString("errcode");
            String errmsg = jsonObject.getString("errmsg");
            if (errmsg.equals("ok")) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }


    /**
     * 提交审核小程序代码
     *
     * @param authToken 第三方token通过接口获取
     * @return
     */
    public static boolean pushAppletCode(String authToken) {

		/*
        * access_token	请使用第三方平台获取到的该小程序授权的authorizer_access_token
item_list	提交审核项的一个列表（至少填写1项，至多填写5项）
address	小程序的页面，可通过“获取小程序的第三方提交代码的页面配置”接口获得
tag	小程序的标签，多个标签用空格分隔，标签不能多于10个，标签长度不超过20
first_class	一级类目名称，可通过“获取授权小程序帐号的可选类目”接口获得
second_class	二级类目(同上)
third_class	三级类目(同上)
first_id	一级类目的ID，可通过“获取授权小程序帐号的可选类目”接口获得
second_id	二级类目的ID(同上)
third_id	三级类目的ID(同上)
title	小程序页面的标题,标题长度不超过32*/
        String params = "{" +
                "    \"item_list\": [" +
                "    {" +
                "        \"address\":\"index\"," +
                "        \"tag\":\"学习 生活\"," +
                "        \"first_class\": \"文娱\"," +
                "        \"second_class\": \"资讯\"," +
                "        \"first_id\":1," +
                "        \"second_id\":2," +
                "        \"title\": \"首页\"" +
                "    }" +
                "    {" +
                "        \"address\":\"page/logs/logs\"," +
                "        \"tag\":\"学习 工作\"," +
                "        \"first_class\": \"教育\"," +
                "        \"second_class\": \"学历教育\"," +
                "        \"third_class\": \"高等\"," +
                "        \"first_id\":3," +
                "        \"second_id\":4," +
                "        \"third_id\":5," +
                "        \"title\": \"日志\"" +
                "    }" +
                "  ]" +
                "}";
        JSONObject jsonObject = WeixinApi.httpRequest(PUSHCODE.replace("AUTH_TOKEN", authToken), "POST", params);
        if (jsonObject != null) {
            /**
             -1	系统繁忙
             86000	不是由第三方代小程序进行调用
             86001	不存在第三方的已经提交的代码
             85006	标签格式错误
             85007	页面路径错误
             85008	类目填写错误
             85009	已经有正在审核的版本
             85010	item_list有项目为空
             85011	标题填写错误
             85023	审核列表填写的项目数不在1-5以内
             85077	小程序类目信息失效（类目中含有官方下架的类目，请重新选择类目）
             86002	小程序还未设置昵称、头像、简介。请先设置完后再重新提交。
             85085	近7天提交审核的小程序数量过多，请耐心等待审核完毕后再次提交
             */
            String errcode = jsonObject.getString("errcode");
            String errmsg = jsonObject.getString("errmsg");
            String auditid = jsonObject.getString("auditid");
            if (errmsg.equals("ok")) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }

    }

    /**
     * 上传小程序代码
     *
     * @param authToken   第三方token通过接口获取
     * @param templateId  小程序的模版id
     * @param userVersion 用户的版本
     * @param userDesc    用户的备注
     * @return
     */
    public static boolean commitAppletCode(String authToken, String templateId, String userVersion, String userDesc) {

        String appjson = "{" +
                "    extAppid:\"\"," +
                "    ext:{" +
                "        \"attr1\":\"value1\"," +
                "        \"attr2\":\"value2\"," +
                "    }," +
                "    extPages:{" +
                "        \"index\":{" +
                "        }," +
                "        \"search/index\":{" +
                "        }," +
                "    }," +
                "    pages:[\"index\",\"search/index\"]," +
                "    \"window\":{" +
                "    }," +
                "    \"networkTimeout\":{" +
                "    }," +
                "    \"tabBar\":{" +
                "    }," +
                "}";

        String params = "{" +
                "\"template_id\":" + templateId + "," +
                "\"ext_json\":\" " + appjson + " \", " +
                "\"user_version\":\"" + userVersion + "\"," +
                "\"user_desc\":\"" + userDesc + "\"," +
                "}";
        JSONObject jsonObject = WeixinApi.httpRequest(COMMITCODE.replace("AUTH_TOKEN", authToken), "POST", params);
        if (jsonObject != null) {
            /**
             * -1	系统繁忙
             85013	无效的自定义配置
             85014	无效的模版编号
             85043	模版错误
             85044	代码包超过大小限制
             85045	ext_json有不存在的路径
             85046	tabBar中缺少path
             85047	pages字段为空
             85048	ext_json解析失败
             */
            String errcode = jsonObject.getString("errcode");
            String errmsg = jsonObject.getString("errmsg");
            if (errmsg.equals("ok")) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }

    }

    /**
     * 获取第三方平台预授权码pre_auth_code
     *
     * @param appId          第三方平台appId
     * @param componentToken 第三方平台access_token
     * @return component_access_token
     * 其他类型的素材消息，则响应的直接为素材的内容，开发者可以自行保存为文件
     */
    public static String getPreAuthCode(String componentToken, String appId) {
        String string = "{\"component_appid\":\"" + appId + "\"}";
        JSONObject jsonObject = WeixinApi.httpRequest(PREAUTHCODE.replace("COMPONENT_TOKEN", componentToken), "POST", string);
        System.out.println("获取的第三方平台的预授权码:" + jsonObject);
        return jsonObject.get("pre_auth_code").toString();
    }


    //刷新token接口
//	public static AuthAccessToken getRefreshAuthorizationCode(String componentToken, String appId, String efresh_token){
//
//		return  null;
//	}
    //获取授权 accesstoken
    public static AuthAccessToken getAuthorizationCode(String componentToken, String appId, String authToken) {

        try {
            if (authAccessTokenHashMap != null && authAccessTokenHashMap.get(appId) != null && authAccessTokenHashMap.get(appId).getExpire().getTime() > (new Date()).getTime() - 2000) {
                return authAccessTokenHashMap.get(appId);
            }
        } catch (Exception e) {

        }

        String string = "{\"component_appid\":\"" + appId + "\" ,\"authorization_code\": \"" + authToken + "\"}";
        JSONObject jsonObject = WeixinApi.httpRequest(CODEANDTOKEN.replace("COMPONENT_TOKEN", componentToken), "POST", string);
        System.out.println("换取的令牌:" + jsonObject);
        if (jsonObject != null) {
            JSONObject info = jsonObject.getJSONObject("authorization_info");
            AuthAccessToken authAccessToken = null;
            if (info != null) {
                authAccessToken = new AuthAccessToken();
                authAccessToken.setAuthorizer_appid(info.getString("authorizer_appid"));
                authAccessToken.setAuthorizer_access_token(info.getString("authorizer_access_token"));
                authAccessToken.setAuthorizer_refresh_token(info.getString("authorizer_refresh_token"));
                authAccessToken.setExpires_in(info.getInt("expires_in"));
                authAccessToken.setExpire(DateUtil.transpositionDate(new Date(), Calendar.SECOND, new Integer(info.getInt("expires_in"))));
                List<FuncInfo> funcInfos = new ArrayList<>();
                JSONArray funcInfoJsonArray = info.getJSONArray("func_info");
                int len = funcInfoJsonArray.size();
                for (int i = 0; i < len; i++) {
                    if (funcInfoJsonArray.getJSONObject(i) != null) {
                        FuncInfo.Category category = new FuncInfo.Category();
                        category.setId(funcInfoJsonArray.getJSONObject(i).getLong("id"));
                        FuncInfo funcInfo = new FuncInfo();
                        funcInfo.setFuncscope_category(category);
                        funcInfos.add(funcInfo);
                    }
                }
                authAccessTokenHashMap.put(appId, authAccessToken);
            }
        }
        return authAccessTokenHashMap.get(appId);
    }

    /**
     * 获取第三方平台component_access_token
     *
     * @param appId     第三方平台appid
     * @param appSecret 第三方平台appsecret
     * @return component_access_token
     * 其他类型的素材消息，则响应的直接为素材的内容，开发者可以自行保存为文件
     */
    public static ComponentAccessToken getComponentToken(String verify_ticket, String appId, String appSecret) {
        try {
            if (componentAccessToken != null && componentAccessToken.getExpire().getTime() > (new Date()).getTime() - 2000) {
                return componentAccessToken;
            }
        } catch (Exception e) {
            componentAccessToken = null;
        }
        if (verify_ticket == null || verify_ticket.equals("")) return null;

        String string = "{\"component_appid\":\"" + appId + "\" ,\"component_appsecret\": \"" + appSecret + "\",\"component_verify_ticket\": \"" + verify_ticket + "\"}";
        JSONObject jsonObject = WeixinApi.httpRequest(COMPONENTTOKEN, "POST", string);
        System.out.println("获取的第三方平台的Token:" + jsonObject);
        if (jsonObject != null) {
            try {
                componentAccessToken = new ComponentAccessToken();
                componentAccessToken.setComponent_access_token(jsonObject.getString("component_access_token"));
                componentAccessToken.setExpires_in(jsonObject.getInt("expires_in"));
                componentAccessToken.setExpire(DateUtil.transpositionDate(new Date(), Calendar.SECOND, new Integer(jsonObject.getInt("expires_in"))));
            } catch (JSONException e) {
                componentAccessToken = null;
                // 获取token失败
                log.error("获取token失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
            }
        }
        return componentAccessToken;
    }

    /**
     *  * 获取access_token  *   * @param appid 凭证  * @param appsecret 密钥  * @return access_token  
     */
    public static AccessToken getAccessToken(String appid, String appsecret) {
        try {
            if (accessToken != null && accessToken.getExpire().getTime() > (new Date()).getTime() - 2000) {
                return accessToken;
            }
        } catch (Exception e) {
            accessToken = null;
        }
        String requestUrl = access_token_url.replace("APPID", appid).replace("APPSECRET", appsecret);
        JSONObject jsonObject = httpRequest(requestUrl, "GET", null);
        // 如果请求成功 
        if (jsonObject != null) {
            try {
                accessToken = new AccessToken();
                accessToken.setToken(jsonObject.getString("access_token"));
                accessToken.setExpiresIn(jsonObject.getInt("expires_in"));
                accessToken.setExpire(DateUtil.transpositionDate(new Date(), Calendar.SECOND, new Integer(jsonObject.getInt("expires_in"))));
            } catch (JSONException e) {
                accessToken = null;
                // 获取token失败
                log.error("获取token失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
            }
        }
        return accessToken;
    }

    /**
     *  * 获取access_token  *   * @param appid 凭证  * @param appsecret 密钥  * @return access_token  
     */
    public static AccessToken getOauth2AccessToken(String appid, String appsecret, String code) {
        AccessToken accessToken = null;

        String requestUrl = oauth2_access_token_url.replace("APPID", appid).replace("SECRET", appsecret).replace("CODE", code);
        JSONObject jsonObject = httpRequest(requestUrl, "GET", null);
        //System.out.println("getAccess==================="+ jsonObject.toString());
        // 如果请求成功 
        if (null != jsonObject) {
            try {
                accessToken = new AccessToken();
                accessToken.setToken(jsonObject.getString("access_token"));
                accessToken.setExpiresIn(jsonObject.getInt("expires_in"));
                accessToken.setOpenid(jsonObject.getString("openid"));
                accessToken.setRefreshToken(jsonObject.getString("refresh_token"));
                accessToken.setScope(jsonObject.getString("scope"));
            } catch (JSONException e) {
                accessToken = null;
                log.error("获取token失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
            }
        }
        return accessToken;
    }

    /**
     * 创建菜单
     *
     * @param menu        菜单实例
     * @param accessToken 有效的access_token
     * @return 0表示成功，其他值表示失败
     */
    public static int createMenu(Menu menu, String accessToken) {
        int result = 0;

        // 拼装创建菜单的url
        String url = menu_create_url.replace("ACCESS_TOKEN", accessToken);
        // 将菜单对象转换成json字符串
        String jsonMenu = JSONObject.fromObject(menu).toString();
        //System.out.println(jsonMenu);
        // 调用接口创建菜单
        JSONObject jsonObject = httpRequest(url, "POST", jsonMenu);

        if (null != jsonObject) {
            if (0 != jsonObject.getInt("errcode")) {
                result = jsonObject.getInt("errcode");
                log.error("创建菜单失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
            }
        }

        return result;
    }

    /**
     * 查询菜单
     *
     * @param accessToken 有效的access_token
     * @return JSONObject
     */
    public static JSONObject createQrcode(String accessToken, String data) {
        // 拼装查询菜单的url
        String url = create_qrcode.replace("ACCESS_TOKEN", accessToken);
        // 调用接口查询菜单
        JSONObject jsonObject = httpRequest(url, "POST", data);

        if (null != jsonObject) {
            if (jsonObject.containsKey("errcode")) {
                if (0 != jsonObject.getInt("errcode")) {
                    System.out.println("创建二维码失败，err:" + jsonObject.getInt("errcode") + " info:" + jsonObject.getString("errmsg"));
                }
            }
        }

        return jsonObject;
    }


    /**
     * 查询菜单
     *
     * @param accessToken 有效的access_token
     * @return JSONObject
     */
    public static JSONObject getMenu(String accessToken) {
        // 拼装查询菜单的url
        String url = menu_get_url.replace("ACCESS_TOKEN", accessToken);
        // 调用接口查询菜单
        JSONObject jsonObject = httpRequest(url, "GET", null);

        return jsonObject;
    }

    /**
     * 删除菜单
     *
     * @param accessToken 有效的access_token
     * @return 0表示成功，其他值表示失败
     */
    public static int deleteMenu(String accessToken) {
        int result = 0;

        // 拼装删除菜单的url
        String url = menu_delete_url.replace("ACCESS_TOKEN", accessToken);
        // 调用接口删除菜单
        JSONObject jsonObject = httpRequest(url, "GET", null);

        if (null != jsonObject) {
            if (0 != jsonObject.getInt("errcode")) {
                result = jsonObject.getInt("errcode");
                log.error("删除菜单失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
            }
        }

        return result;
    }

    /**
     * 查询菜单
     *
     * @param accessToken 有效的access_token
     * @return JSONObject
     */
    public static JSONObject getMenuObject(String accessToken) {
        // 拼装查询菜单的url
        String url = menu_get_url.replace("ACCESS_TOKEN", accessToken);
        // 调用接口查询菜单
        JSONObject jsonObject = httpRequest(url, "GET", null);

        return jsonObject;
    }

    /**
     * 查询关注者列表
     *
     * @param accessToken 有效的access_token
     * @return JSONObject
     */
    public static JSONObject getUserList(String accessToken, String nextOpenid) {
        // 拼装查询菜单的url
        String url = user_getlist_url.replace("ACCESS_TOKEN", accessToken).replace("NEXT_OPENID", nextOpenid);
        // 调用接口查询菜单
        JSONObject jsonObject = httpRequest(url, "GET", null);

        return jsonObject;
    }

    /**
     * 通过code获取acces_token
     *
     * @param appid 凭证  * @param appsecret 密钥
     * @param code  票据
     * @return JSONObject
     */
    public static JSONObject getAccessToken(String appid, String secret, String code) {
        // 拼装查询菜单的url
        String url = code_at_url.replace("APPID", appid).replace("SECRET", secret).replace("CODE", code);
        // 调用接口查询菜单
        JSONObject jsonObject = httpRequest(url, "GET", null);

        return jsonObject;
    }

    /**
     * 通过refresh_token换取access_token
     *
     * @param refresh_token
     * @param appid
     */
    public static JSONObject refreshAccessToken(String appid, String refresh_token) {
        // 拼装查询菜单的url
        String url = refresh_at_url.replace("APPID", appid).replace("SECRET", refresh_token);
        // 调用接口查询菜单
        JSONObject jsonObject = httpRequest(url, "GET", null);

        return jsonObject;
    }

    /**
     * 使用access_token获取用户信息  * @param access_token
     *
     * @param openid
     * @return JSONObject
     */
    public static JSONObject getUserInfo(String access_token, String openid) {
        // 拼装查询菜单的url
        String url = user_getinfo_url.replace("ACCESS_TOKEN", access_token).replace("OPENID", openid);
        // 调用接口查询菜单
        JSONObject jsonObject = httpRequest(url, "GET", null);
        return jsonObject;
    }

    /**
     * 使用用Code获得的access_token获取用户信息  * @param access_token 和上面的不一样
     *
     * @param openid
     * @return JSONObject
     */
    public static JSONObject getUserInfoByCode(String access_token, String openid) {
        // 拼装查询菜单的url
        String url = user_getinfo_byCode_url.replace("ACCESS_TOKEN", access_token).replace("OPENID", openid);
        // 调用接口查询菜单
        JSONObject jsonObject = httpRequest(url, "GET", null);
        return jsonObject;
    }

    /**
     * 判断是否是QQ表情
     *
     * @param content
     * @return
     */
    public static boolean isFace(String content) {
        boolean result = false;
        String faceRegex = "/::\\)|/::~|/::B|/::\\||/:8-\\)|/::<|/::$|/::X|/::Z" + "|/::'\\(|/::-\\||/::@|/::P|/::D|/::O|/::\\(|/::\\+|/:--b|/::Q" + "|/::T|/:,@P|/:,@-D|/::d|/:,@o|/::g|/:\\|-\\)|/::!|/::L|/::>|/::,@"
                + "|/:,@f|/::-S|/:\\?|/:,@x|/:,@@|/::8|/:,@!|/:!!!|/:xx|/:bye|/:wipe" + "|/:dig|/:handclap|/:&-\\(|/:B-\\)|/:<@|/:@>|/::-O|/:>-\\||/:P-\\(" + "|/::'\\||/:X-\\)|/::\\*|/:@x|/:8\\*|/:pd|/:<W>|/:beer|/:basketb|/:oo"
                + "|/:coffee|/:eat|/:pig|/:rose|/:fade|/:showlove|/:heart|/:break|/:cake" + "|/:li|/:bome|/:kn|/:footb|/:ladybug|/:shit|/:moon|/:sun|/:gift|/:hug" + "|/:strong|/:weak|/:share|/:v|/:@\\)|/:jj|/:@@|/:bad|/:lvu|/:no|/:ok"
                + "|/:love|/:<L>|/:jump|/:shake|/:<O>|/:circle|/:kotow|/:turn|/:skip|/:oY" + "|/:#-0|/:hiphot|/:kiss|/:<&|/:&>";
        Pattern p = Pattern.compile(faceRegex);
        if (p.matcher(content).matches())
            result = true;

        return result;
    }

    /**
     *  * 根据 OpenID 列表群发 
     */
    public static int massSend(String appid, String appsecret, String message) {
        int result = 0;
        AccessToken token = WeixinApi.getAccessToken(appid, appsecret);
        String requestUrl = massSendUrl.replace("ACCESS_TOKEN", token.getToken());
        JSONObject jsonObject = httpRequest(requestUrl, "POST", message);
        if (null != jsonObject) {
            if (0 != jsonObject.getInt("errcode")) {
                result = jsonObject.getInt("errcode");
                log.error("群发失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
            }
        }
        return result;
    }


    /**
     *  * 根据OpenID列表群发 
     */
    public static int sendTemplete(String appid, String appsecret, String message) {
        int result = 0;
        AccessToken token = WeixinApi.getAccessToken(appid, appsecret);
        String requestUrl = send_message.replace("ACCESS_TOKEN", token.getToken());
        JSONObject jsonObject = httpRequest(requestUrl, "POST", message);
        if (null != jsonObject) {
            if (0 != jsonObject.getInt("errcode")) {
                result = jsonObject.getInt("errcode");
                log.error("模板消息 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
            }
        }
        return result;
    }

    /**
     *  * 根据OpenID列表群发 
     */
    public static String getOauth2Code(String appid, String redirectUri, String scope) {
        try {
            return WeixinApi.oauth2_get_code.replace("APPID", appid).replace("REDIRECT_URI", redirectUri).replace("SCOPE", scope);
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public static Ticket getTicket() {
        try {
            if (jsapi_ticket != null && jsapi_ticket.getExpire().getTime() > (new Date()).getTime() - 2000) {
                return jsapi_ticket;
            }
        } catch (Exception e) {
            wxcard_ticket = null;
        }
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        AccessToken token = getAccessToken(bundle.getString("weixin.appid"), bundle.getString("weixin.secret"));
        if (token != null) {
            String requestUrl = GETJSAPITICKET.replace("ACCESS_TOKEN", token.getToken());
            JSONObject jsonObject = httpRequest(requestUrl, "GET", null);
            if (jsonObject != null) {
                jsapi_ticket = new Ticket();
                jsapi_ticket.setTicket(jsonObject.getString("ticket"));
                jsapi_ticket.setExpires_in(jsonObject.getInt("expires_in"));
                jsapi_ticket.setExpire(DateUtil.transpositionDate(new Date(), Calendar.SECOND, new Integer(jsonObject.getInt("expires_in"))));
                return jsapi_ticket;
            }
        }
        return null;
    }

    public static Ticket getWxCardTicket() {
        try {
            if (wxcard_ticket != null && wxcard_ticket.getExpire().getTime() > (new Date()).getTime() - 2000) {
                return wxcard_ticket;
            }
        } catch (Exception e) {
            wxcard_ticket = null;
        }
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        AccessToken token = getAccessToken(bundle.getString("weixin.appid"), bundle.getString("weixin.secret"));
        if (token != null) {
            String requestUrl = GETWXCARDTICKET.replace("ACCESS_TOKEN", token.getToken());
            JSONObject jsonObject = httpRequest(requestUrl, "GET", null);
            if (jsonObject != null) {
                wxcard_ticket = new Ticket();
                wxcard_ticket.setTicket(jsonObject.getString("ticket"));
                wxcard_ticket.setExpires_in(jsonObject.getInt("expires_in"));
                wxcard_ticket.setExpire(DateUtil.transpositionDate(new Date(), Calendar.SECOND, new Integer(jsonObject.getInt("expires_in"))));
                return wxcard_ticket;
            }
        }
        return null;
    }

}
