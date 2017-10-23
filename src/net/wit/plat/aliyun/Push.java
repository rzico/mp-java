package net.wit.plat.aliyun;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.push.model.v20160801.*;
import net.wit.entity.Member;
import net.wit.entity.Message;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.*;

public class Push {
    public static Logger logger = LogManager.getLogger(Push.class);
    // 目前只有"cn-hangzhou"这个region可用, 不要使用填写其他region的值
    public static final String REGION_CN_HANGZHOU = "cn-hangzhou";
    // 当前 STS API 版本
    public static final String STS_API_VERSION = "2015-04-01";
    public static boolean aliPushToAndriod(Message message) {
        try {
            ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        // 创建一个 Aliyun Acs Client, 用于发起 OpenAPI 请求
        IClientProfile profile = DefaultProfile.getProfile(REGION_CN_HANGZHOU, bundle.getString("x-sts-accessKey"), bundle.getString("x-sts-accessSecret"));
        DefaultAcsClient client = new DefaultAcsClient(profile);
        PushNoticeToAndroidRequest androidRequest = new PushNoticeToAndroidRequest();
        //安全性比较高的内容建议使用HTTPS
        androidRequest.setProtocol(ProtocolType.HTTPS);
        //内容较大的请求，使用POST请求
        androidRequest.setMethod(MethodType.POST);
        androidRequest.setAppKey(Long.parseLong(bundle.getString("x-push-andriod-appKey")));
        androidRequest.setTarget("DEVICE");
        androidRequest.setTargetValue(message.getMember().getUuid());
        androidRequest.setTitle(message.getTitle());
        androidRequest.setBody(message.getContent());
        androidRequest.setExtParameters("{\"type\":\""+message.getType()+"\"}");

        PushNoticeToAndroidResponse pushNoticeToAndroidResponse = null;
            pushNoticeToAndroidResponse = client.getAcsResponse(androidRequest);
            return true;
        } catch (ClientException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean aliPushToIOS(Message message) {
        try {
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        // 创建一个 Aliyun Acs Client, 用于发起 OpenAPI 请求
        IClientProfile profile = DefaultProfile.getProfile(REGION_CN_HANGZHOU, bundle.getString("x-sts-accessKey"), bundle.getString("x-sts-accessSecret"));
        DefaultAcsClient client = new DefaultAcsClient(profile);
        PushNoticeToiOSRequest iOSRequest = new PushNoticeToiOSRequest();
        //安全性比较高的内容建议使用HTTPS
        iOSRequest.setProtocol(ProtocolType.HTTPS);
        //内容较大的请求，使用POST请求
        iOSRequest.setMethod(MethodType.POST);
        iOSRequest.setAppKey(Long.parseLong(bundle.getString("x-push-ios-appKey")));
        // iOS的通知是通过APNS中心来发送的，需要填写对应的环境信息. DEV :表示开发环境, PRODUCT: 表示生产环境
        iOSRequest.setApnsEnv("PRODUCT");
        iOSRequest.setTarget("DEVICE");
        iOSRequest.setTargetValue(message.getMember().getUuid());
        //iOSRequest.setTitle("eewwewe");
        iOSRequest.setBody(message.getContent());
        iOSRequest.setExtParameters("{\"type\":\""+message.getType()+"\"}");

            PushNoticeToiOSResponse pushNoticeToiOSResponse = client.getAcsResponse(iOSRequest);
            return true;
        } catch (ClientException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean aliPush(Message message) {
        Member member = message.getReceiver();
        if (member==null) {
            return false;
        }
        if (member.getUuid()==null) {
            return false;
        }

        try {
            String ua = member.getScene();
            if (ua != null) {
                if (ua.contains("Andriod")) {
                    aliPushToAndriod(message);
                }
                if (ua.contains("IOS")) {
                    aliPushToIOS(message);
                }
            } else {
                aliPushToAndriod(message);
                aliPushToIOS(message);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }

        return true;

    }
}
