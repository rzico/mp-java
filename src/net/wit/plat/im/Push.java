package net.wit.plat.im;

import net.wit.entity.Message;
import net.wit.plat.weixin.util.WeiXinUtils;
import net.wit.plat.weixin.util.WeixinApi;
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

import java.util.*;

public class Push {
    public static Logger logger = LogManager.getLogger(Push.class);

    public static String sendUrl="https://console.tim.qq.com/v4/openim/sendmsg?usersig=USERSIG&identifier=ADMIN&sdkappid=SDKAPPID&random=RANDOM&contenttype=json";
    public static boolean impush(Message message) {
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        String userSig= User.createUserSig(bundle.getString("im.admin"));
        int random= StringUtils.Random6Code();
        String url = sendUrl.replace("USERSIG",userSig).replace("ADMIN",bundle.getString("im.admin")).replace("SDKAPPID",bundle.getString("x-tls-appId")).replace("RANDOM",String.valueOf(random));

        Map<String,Object> data = new HashMap<String,Object>();
        data.put("SyncOtherMachine",2);
        data.put("From_Account",message.getSender().getUsername());
        data.put("To_Account",message.getReceiver().userId());
        data.put("MsgRandom",random);
        data.put("MsgTimeStamp",message.getCreateDate().getTime() / 1000);
        data.put("MsgLifeTime",3600*24);
        Map<String,Object> msgBody = new HashMap<String,Object>();
        List<Object> body = new ArrayList<Object>();
        body.add(msgBody);
        data.put("MsgBody",body);
        msgBody.put("MsgType","TIMTextElem");
        Map<String,Object> MsgContent = new HashMap<String,Object>();
        msgBody.put("MsgContent",MsgContent);
        MsgContent.put("Text",message.getContent());
        Map<String,Object> OfflinePushInfo = new HashMap<String,Object>();
        data.put("OfflinePushInfo",OfflinePushInfo);
        OfflinePushInfo.put("PushFlag",0);
        OfflinePushInfo.put("Desc",message.getContent());
        OfflinePushInfo.put("Ext","");
        Map<String,Object> AndroidInfo = new HashMap<String,Object>();
        OfflinePushInfo.put("AndroidInfo",AndroidInfo);
        AndroidInfo.put("Sound","msg.mp3");
        Map<String,Object> ApnsInfo = new HashMap<String,Object>();
        OfflinePushInfo.put("ApnsInfo",ApnsInfo);
        ApnsInfo.put("Sound","msg.mp3");
        ApnsInfo.put("BadgeMode",1);

        HttpClient httpClient = new DefaultHttpClient();
        try {
            String msg = JsonUtils.toJson(data);
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new StringEntity(msg, "UTF-8"));
            HttpResponse response = httpClient.execute(httpPost);
            String jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
            Map resp = JsonUtils.toObject(jsonStr,Map.class);
            System.out.println(msg);
            System.out.println(resp);
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

    public static boolean taskPush(String sender,String receiver,Long timeStamp,String content) {
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        String userSig= User.createUserSig(bundle.getString("im.admin"));
        int random= StringUtils.Random6Code();
        String url = sendUrl.replace("USERSIG",userSig).replace("ADMIN",bundle.getString("im.admin")).replace("SDKAPPID",bundle.getString("x-tls-appId")).replace("RANDOM",String.valueOf(random));

        Map<String,Object> data = new HashMap<String,Object>();
        data.put("SyncOtherMachine",2);
        data.put("From_Account",sender);
        data.put("To_Account",receiver);
        data.put("MsgRandom",random);
        data.put("MsgTimeStamp",timeStamp / 1000);
        data.put("MsgLifeTime",3600*24);
        Map<String,Object> msgBody = new HashMap<String,Object>();
        List<Object> body = new ArrayList<Object>();
        body.add(msgBody);
        data.put("MsgBody",body);
        msgBody.put("MsgType","TIMTextElem");
        Map<String,Object> MsgContent = new HashMap<String,Object>();
        msgBody.put("MsgContent",MsgContent);
        MsgContent.put("Text",content);
        Map<String,Object> OfflinePushInfo = new HashMap<String,Object>();
        data.put("OfflinePushInfo",OfflinePushInfo);
        OfflinePushInfo.put("PushFlag",0);
        OfflinePushInfo.put("Desc",content);
        OfflinePushInfo.put("Ext","");
        Map<String,Object> AndroidInfo = new HashMap<String,Object>();
        OfflinePushInfo.put("AndroidInfo",AndroidInfo);
        AndroidInfo.put("Sound","msg.mp3");
        Map<String,Object> ApnsInfo = new HashMap<String,Object>();
        OfflinePushInfo.put("ApnsInfo",ApnsInfo);
        ApnsInfo.put("Sound","msg.mp3");
        ApnsInfo.put("BadgeMode",1);

        HttpClient httpClient = new DefaultHttpClient();
        try {
            String msg = JsonUtils.toJson(data);
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new StringEntity(msg, "UTF-8"));
            HttpResponse response = httpClient.execute(httpPost);
            String jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
            Map resp = JsonUtils.toObject(jsonStr,Map.class);
            System.out.println(msg);
            System.out.println(resp);
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
