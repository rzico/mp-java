package net.wit.plat.nihtan;

import net.wit.entity.Member;
import net.wit.util.JsonUtils;
import org.apache.commons.codec.binary.Hex;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.util.Assert;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangsr on 2018/1/29.
 */
public class Crypto {
    public static String vendor_name="ch_test";
    public static String key="F02B3FD022617A7401E88D3D9A3E2A2C";
    public static String sessionURL = "http://api.neo-nihtan.com/api/session";
    public static String gameListURL = "http://api.neo-nihtan.com/api/game/list";
    public static String videoListURL = "http://video-list.neo-nihtan.com/video/list";

    public static String encrypt(String key, String data) {
        String hash = "";
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            byte[] bytes = sha256_HMAC.doFinal(data.getBytes());
            hash = Hex.encodeHexString(bytes);
            System.out.println(hash);
        } catch (Exception e) {
            System.out.println("Error HmacSHA256 ===========" + e.getMessage());
        }
        return hash;
    }

    /**
     * POST请求
     * @param url URL
     * @param data 请求参数
     * @return 返回结果
     */
    public static String post(String url, String data) {
        Assert.hasText(url);
        String result = null;
        HttpClient httpClient = new DefaultHttpClient();
        try {
            HttpPost httpPost = new HttpPost(url);
            StringEntity entity = new StringEntity(data,"UTF-8");
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            result = EntityUtils.toString(httpEntity);
            EntityUtils.consume(httpEntity);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return result;
    }

    /**
     * POST请求
     * @param url URL
     * @param data 请求参数
     * @return 返回结果
     */
    public static String get(String url, String data) {
        Assert.hasText(url);
        String result = null;
        HttpClient httpClient = new DefaultHttpClient();
        try {
            HttpGet httpGet = new HttpGet(url);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            result = EntityUtils.toString(httpEntity);
            EntityUtils.consume(httpEntity);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return result;
    }

    public static String getSession(String ip,Member member) {
        Map<String,String> data = new HashMap<String,String>();
        data.put("user_id",member.getUsername());
        data.put("user_name",member.getUsername());
        data.put("user_ip",ip);
        data.put("vendor_name","ch_test");
        data.put("pc_redirect","http://dev.rzico.com/nihtan");
        data.put("mo_redirect","http://dev.rzico.com/nihtan");
        String dataStr = JsonUtils.toJson(data);
        String hash = encrypt(key,dataStr);

        String resp = post(sessionURL+"?hash="+hash,dataStr);
        return resp;
    }


    public static String gameList() {
        Map<String,String> data = new HashMap<String,String>();
        data.put("vendor_name","ch_test");
        String dataStr = JsonUtils.toJson(data);
        String hash = encrypt(key,dataStr);

        String resp = post(gameListURL+"?hash="+hash,dataStr);
        return resp;
    }

    public static String videoList() {
        String resp = get(videoListURL,"");
        return resp;
    }


    public static void main(String[] args) throws Exception {
        Crypto.videoList();
    }
}
