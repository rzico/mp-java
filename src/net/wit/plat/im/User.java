package net.wit.plat.im;

import com.tls.tls_sigature.tls_sigature;
import net.wit.entity.Member;
import net.wit.util.JsonUtils;
import net.wit.util.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.zip.DataFormatException;

import static com.tls.tls_sigature.tls_sigature.CheckTLSSignatureEx;
import static com.tls.tls_sigature.tls_sigature.GenTLSSignatureEx;

public class User {
    public static Logger logger = LogManager.getLogger(User.class);
    public static String privateKey=
            "-----BEGIN PRIVATE KEY-----\n"+
            "MIGEAgEAMBAGByqGSM49AgEGBSuBBAAKBG0wawIBAQQgilzRLbmHzOJTqgpaM5Ln\n"+
            "yyGZLYxr7Q0HGkm8YCz95f+hRANCAAQaFa7sPGBQbxMRrrce87loEfMH5v3+L5Rx\n"+
            "60tHHM/kXD1hh7TTv+dAwyQsfR7dAkoy3KVKxNcHP+OGHK/M1OS0\n"+
            "-----END PRIVATE KEY-----";
    public static String publicKey=
            "-----BEGIN PUBLIC KEY-----\n"+
            "MFYwEAYHKoZIzj0CAQYFK4EEAAoDQgAEGhWu7DxgUG8TEa63HvO5aBHzB+b9/i+U\n"+
            "cetLRxzP5Fw9YYe007/nQMMkLH0e3QJKMtylSsTXBz/jhhyvzNTktA==\n"+
            "-----END PUBLIC KEY-----";
    public static String im_attr="https://console.tim.qq.com/v4/openim/im_set_attr_name?usersig=USERSIG&identifier=ADMIN&sdkappid=SDKAPPID&random=RANDOM&contenttype=json";
    public static String user_attr="https://console.tim.qq.com/v4/openim/im_set_attr?usersig=USERSIG&identifier=ADMIN&sdkappid=SDKAPPID&random=RANDOM&contenttype=json";
    public static boolean checkUserSig(String urlSig,String username) {
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");

        // check signature
        tls_sigature.CheckTLSSignatureResult checkResult = null;
        try {
            checkResult = CheckTLSSignatureEx(urlSig,Long.parseLong(bundle.getString("x-tls-appId")), username, User.publicKey);
            return checkResult.verifyResult == true;
        } catch (DataFormatException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static String createUserSig(String username) {
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        // generate signature
        tls_sigature.GenTLSSignatureResult result = null;
        try {
            result = GenTLSSignatureEx(Long.parseLong(bundle.getString("x-tls-appId")), username, User.privateKey);
            return result.urlSig;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
    public static boolean imAttr() {
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        String userSig=User.createUserSig("zhangsr");
        int random=StringUtils.Random6Code();

        String url = im_attr.replace("USERSIG",userSig).replace("ADMIN","zhangsr").replace("SDKAPPID",bundle.getString("x-tls-appId")).replace("RANDOM",String.valueOf(random) );

        String data ="{ \"AttrNames\": { \"0\": \"userId\", \"1\": \"logo\", \"2\": \"nickName\"}}";

        HttpClient httpClient = new DefaultHttpClient();
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new StringEntity(data, "UTF-8"));
            HttpResponse response = httpClient.execute(httpPost);
            String jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
            Map resp = JsonUtils.toObject(jsonStr,Map.class);
            if ("OK".equals(resp.get("ActionStatus"))) {
                return true;
            } else {
                logger.error(resp.get("ErrorInfo"));
                return false;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        } finally {
            httpClient.getConnectionManager().shutdown();
        }

    }
    public static boolean userAttr(Member member) {
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        String userSig=User.createUserSig("zhangsr");
        int random=StringUtils.Random6Code();

        String url = im_attr.replace("USERSIG",userSig).replace("ADMIN","zhangsr").replace("SDKAPPID",bundle.getString("x-tls-appId")).replace("RANDOM",String.valueOf(random) );

        Map<String,String> attrs = new HashMap<String,String>();
        attrs.put("userId",member.getId());
        attrs.put("logo",member.getLogo());
        attrs.put("nickName",member.getNickName());
        String data ="{\"UserAttrs\":"+
                     "[{\"To_Account\": \""+member.userId()+"\","+
                       "\"Attrs\":"+JsonUtils.toJson(attrs)+"\"}"+
                       "}"+
                     "]}";
        HttpClient httpClient = new DefaultHttpClient();
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new StringEntity(data, "UTF-8"));
            HttpResponse response = httpClient.execute(httpPost);
            String jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
            Map resp = JsonUtils.toObject(jsonStr,Map.class);
            if ("OK".equals(resp.get("ActionStatus"))) {
                return true;
            } else {
                logger.error(resp.get("ErrorInfo"));
                return false;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        } finally {
            httpClient.getConnectionManager().shutdown();
        }

    }
}
