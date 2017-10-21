package net.wit.plat.im;

import net.wit.entity.Message;
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

import java.util.HashMap;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class Push {
    public static Logger logger = LogManager.getLogger(Push.class);

    public static String im="https://console.tim.qq.com/v4/openim/im_push?usersig=USERSIG&identifier=ADMIN&sdkappid=SDKAPPID&random=TIMESTAMP&contenttype=json";
    public static boolean impush(Message message) {
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        String userSig= User.createUserSig("zhangsr");
        int random= StringUtils.Random6Code();
        String url = im.replace("USERSIG",userSig).replace("ADMIN","zhangsr").replace("SDKAPPID",bundle.getString("x-tls-appId")).replace("RANDOM",String.valueOf(random));
        Map<String,String> content = new HashMap<String,String>();
        content.put("Text",message.getContent());
        String data =
                "{\"MsgRandom\": "+message.getId()+",\"Condition\": {"+
                        "\"AttrsOr\": {"+
                               "\"userId\": \"男\""+
                               "}},"+
                "\"MsgBody\": [{"+
                        "\"MsgType\": \"TIMTextElem\","+
                        "\"MsgContent\":"+JsonUtils.toJson(content)+"}"+
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
