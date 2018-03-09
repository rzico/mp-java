package net.wit.plat.nihtan;

import net.wit.entity.Member;
import net.wit.util.JsonUtils;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.StringUtils;
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
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangsr on 2018/1/29.
 */
public class Kaga {

    public static String vendor_name="rzico";
    public static String key="F02B3FD022617A7401E88D3D9A3E2A2C";
    public static String sessionURL = "http://api.wapceo.com/api/kaga/open";
    public static String gameListURL = "http://api.wapceo.com/api/game/kaga";

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

    /**
     * 去掉url中的路径，留下请求参数部分
     * @param strURL url地址
     * @return url请求参数部分
     * @author lzf
     */
    private static String TruncateUrlPage(String strURL){
        String strAllParam=null;
        String[] arrSplit=null;
        strURL=strURL.trim();
        arrSplit=strURL.split("[?]");
        if(strURL.length()>1){
            if(arrSplit.length>1){
                for (int i=1;i<arrSplit.length;i++){
                    strAllParam = arrSplit[i];
                }
            }
        }
        return strAllParam;
    }

    /**
     * 解析出url参数中的键值对
     * 如 "index.jsp?Action=del&id=123"，解析出Action:del,id:123存入map中
     * @param URL  url地址
     * @return  url请求参数部分
     * @author lzf
     */
    public static Map<String, String> urlSplit(String URL){
        Map<String, String> mapRequest = new HashMap<String, String>();
        String[] arrSplit=null;
        String strUrlParam=TruncateUrlPage(URL);
        if(strUrlParam==null){
            return mapRequest;
        }
        arrSplit=strUrlParam.split("[&]");
        for(String strSplit:arrSplit){
            String[] arrSplitEqual=null;
            arrSplitEqual= strSplit.split("[=]");
            //解析出键值
            if(arrSplitEqual.length>1){
                //正确解析
                mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);
            }else{
                if(arrSplitEqual[0]!=""){
                    //只有参数没有值，不加入
                    mapRequest.put(arrSplitEqual[0], "");
                }
            }
        }
        return mapRequest;
    }

    public static String getQueryString(String url, String name) {
        return urlSplit(url).get(name);
    }

    public static String getSession(String game,String ip,Member member) {
        Map<String,String> data = new HashMap<String,String>();
        data.put("user_id",member.getUsername());

        if (member.getNickName()!=null) {
            data.put("user_name", member.getNickName());
        } else {
            data.put("user_name", member.getUsername());
        }
        data.put("user_ip",ip);
        data.put("vendor_name",vendor_name);
        data.put("mobile","1");
        data.put("game_id",game);
        data.put("pc_redirect", URLEncoder.encode("http://weex.udzyw.com/home"));
        data.put("mo_redirect", URLEncoder.encode("http://weex.udzyw.com/home"));
        String dataStr = JsonUtils.toJson(data);
        System.out.println("data="+JsonUtils.toJson(data));
        String hash = encrypt(key,dataStr);
        System.out.println("hash="+hash);
        System.out.println("api.url="+"http://api.wapceo.com/api/kaga/open?hash="+hash);
        String resp = post("http://api.wapceo.com/api/kaga/open?hash="+hash,dataStr);
        System.out.println("resp="+resp);
        return resp;
    }


    public static String gameList() {
        Map<String,String> data = new HashMap<String,String>();
        data.put("vendor_name",vendor_name);
        String dataStr = JsonUtils.toJson(data);
        String hash = encrypt(key,dataStr);

        String resp = post(gameListURL+"?hash="+hash,dataStr);
        return resp;
    }


    public static void main(String[] args) throws Exception {
        String resp = "https://gmtestcdn.kga8.com/?p=NIHTANZH&g=Stonehenge&cr=CNY&t=9da062f2-ac08-4d78-995c-a5c626c8843e&u=13860431130&loc=zh-cn&ak=833E33CE84EE4719AD8763C911719C4A&l=exit";
        String url = "https://gmtestcdn.kga8.com/?p="+
                URLEncoder.encode(getQueryString(resp,"p"))+"&g="+
                URLEncoder.encode(getQueryString(resp,"g"))+"&cr="+
                URLEncoder.encode(getQueryString(resp,"cr"))+"&t="+
                URLEncoder.encode(getQueryString(resp,"t"))+"&u="+
                URLEncoder.encode(getQueryString(resp,"u"))+"&loc="+
                URLEncoder.encode(getQueryString(resp,"loc"))+"&ak="+
                URLEncoder.encode(getQueryString(resp,"ak"))+"&l="+
                URLEncoder.encode(getQueryString(resp,"l"));
        System.out.println(url);

    }
}
