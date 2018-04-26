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
import java.util.*;
import java.util.zip.DataFormatException;

import static com.tls.tls_sigature.tls_sigature.CheckTLSSignatureEx;
import static com.tls.tls_sigature.tls_sigature.GenTLSSignatureEx;

public class User {
    public static Logger logger = LogManager.getLogger(User.class);

    public static String privateKey=
            "-----BEGIN PRIVATE KEY-----\n"+
            "MIGHAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBG0wawIBAQQg8s4bgHXrydRo5SWR\n"+
            "OZhCZuSxPizC+pJzq4YnnHQBxkChRANCAAQBT0lZlVjDosCPHMBlJdgw6anE262k\n"+
            "cW8j6d3DUItNIN3RhOWqPEAgdTn0eicb3sdFuikiSfFu6oAdLwACqe1W\n"+
            "-----END PRIVATE KEY-----";
    public static String publicKey=
            "-----BEGIN PUBLIC KEY-----\n"+
            "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEAU9JWZVYw6LAjxzAZSXYMOmpxNut\n"+
            "pHFvI+ndw1CLTSDd0YTlqjxAIHU59HonG97HRbopIknxbuqAHS8AAqntVg==\n"+
            "-----END PUBLIC KEY-----";
//    public static String privateKey=
//              "-----BEGIN PRIVATE KEY-----\n"+
//              "MIGHAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBG0wawIBAQQgpPaVbL3nDotp6NYu\n"+
//              "rRE2t6b0NbqmashEAgP3MF9rxYKhRANCAASoCYP3tz5TOlc1M56Wy505G0CQkZyl\n"+
//              "GkQu5XGhhXEprSlbgQ90afpA/L9TVwoeuWxGlzjE/wrhtn2/0QdVy3DA\n"+
//              "-----END PRIVATE KEY-----";
//    public static String publicKey=
//              "-----BEGIN PUBLIC KEY-----\n"+
//              "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEqAmD97c+UzpXNTOelsudORtAkJGc\n"+
//              "pRpELuVxoYVxKa0pW4EPdGn6QPy/U1cKHrlsRpc4xP8K4bZ9v9EHVctwwA==\n"+
//              "-----END PUBLIC KEY-----";
    public static String im_attr="https://console.tim.qq.com/v4/openim/im_set_attr_name?usersig=USERSIG&identifier=ADMIN&sdkappid=SDKAPPID&random=RANDOM&contenttype=json";
    public static String user_attr="https://console.tim.qq.com/v4/im_open_login_svc/account_import?usersig=USERSIG&identifier=ADMIN&sdkappid=SDKAPPID&random=RANDOM&contenttype=json";
    public static String user_state="https://console.tim.qq.com/v4/openim/querystate?usersig=USERSIG&identifier=ADMIN&sdkappid=SDKAPPID&random=RANDOM&contenttype=json";
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
        String userSig=User.createUserSig("hhmm007");
        int random=StringUtils.Random6Code();

        String url = im_attr.replace("USERSIG",userSig).replace("ADMIN","hhmm007").replace("SDKAPPID",bundle.getString("x-tls-appId")).replace("RANDOM",String.valueOf(random) );

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
        if (member.getLogo()==null) {
            return true ;
        }
        if (member.getNickName()==null) {
            return true;
        }
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        String userSig=User.createUserSig("hhmm007");
        int random=StringUtils.Random6Code();

        String url = user_attr.replace("USERSIG",userSig).replace("ADMIN","hhmm007").replace("SDKAPPID",bundle.getString("x-tls-appId")).replace("RANDOM",String.valueOf(random) );

        Map<String,String> attrs = new HashMap<String,String>();
        attrs.put("Identifier",member.userId());
        attrs.put("FaceUrl",member.getLogo());
        attrs.put("Nick",member.getNickName());
        HttpClient httpClient = new DefaultHttpClient();
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new StringEntity(JsonUtils.toJson(attrs), "UTF-8"));
            HttpResponse response = httpClient.execute(httpPost);
            String jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
            Map resp = JsonUtils.toObject(jsonStr,Map.class);
            System.out.printf(jsonStr);
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
    public static boolean userState(List<Member> members) {
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        String userSig=User.createUserSig("hhmm007");
        int random=StringUtils.Random6Code();

        String url = user_state.replace("USERSIG",userSig).replace("ADMIN","hhmm007").replace("SDKAPPID",bundle.getString("x-tls-appId")).replace("RANDOM",String.valueOf(random) );

        Map<String,Object> data = new HashMap<String,Object>();
        List<String> users = new ArrayList<String>();
        for (Member member:members) {
            users.add(member.userId());
        }
        data.put("To_Account",users);
        HttpClient httpClient = new DefaultHttpClient();
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new StringEntity(JsonUtils.toJson(data), "UTF-8"));
            HttpResponse response = httpClient.execute(httpPost);
            String jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
            Map resp = JsonUtils.toObject(jsonStr,Map.class);
            if ("OK".equals(resp.get("ActionStatus"))) {
                List<Map> states = new ArrayList<>();
                states = JsonUtils.toObject(JsonUtils.toJson(resp.get("QueryResult")),List.class);
                for (Member member:members) {
                    for (Map m:states) {
                        if (m.get("To_Account").toString().equals(member.userId())) {
                            member.setAttributeValue9(m.get("State").toString());
                        }
                    }
                }
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
