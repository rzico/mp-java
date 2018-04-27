package net.wit.plat.yundian;

import net.sf.json.JSON;
import net.sf.json.JSONObject;
import net.wit.Message;
import net.wit.entity.Transfer;
import net.wit.plat.im.User;
import net.wit.plat.weixin.util.WeixinApi;
import net.wit.service.RSAService;
import net.wit.service.RedisService;
import net.wit.service.impl.RSAServiceImpl;
import net.wit.util.JsonUtils;
import net.wit.util.MD5Utils;
import net.wit.util.RSAUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.bouncycastle.jcajce.provider.asymmetric.RSA;
import org.bouncycastle.jcajce.provider.asymmetric.rsa.RSAUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.*;

public class UnsPay {
    public static Logger logger = LogManager.getLogger(UnsPay.class);

    // 维护一个本类的静态变量
    public static UnsPay unsPay;
    //魔篇下芸店的商户号（先暂时用我自己的进行调试，日后等魔篇注册了之后进行修改）
    //魔篇在芸店下的用户密码
    private static final String PASSWORD = "123456";
    //魔篇在芸店下的用户ID
    private static final String ID = "909";
    //代付接口
    private static final String UNSPAY = "http://weex.rzico.com/unspay/submit.jhtml";
    //查询接口
    private static final String QUERY = "http://weex.rzico.com/unspay/query.jhtml";
    //公钥接口
    private static final String PUBLICKEY = "http://weex.rzico.com//weex/common/public_key.jhtml";


    /**
     * post请求
     *
     * @param json 请求参数
     * @return 返回结果
     */
    public static JSONObject post(String json) {
        //发起远程芸店链接
        JSONObject jsonObject = null;
        StringBuffer buffer = new StringBuffer();
        try {
            URL rurl = new URL(QUERY);
            HttpURLConnection conn = (HttpURLConnection) rurl.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            if (null != json && json.equals("")) {
                OutputStream outputStream = conn.getOutputStream();
                // 注意编码格式，防止中文乱码
                outputStream.write(json.getBytes("UTF-8"));
                outputStream.close();
            }

            // 将返回的输入流转换成字符串
            InputStream inputStream = conn.getInputStream();
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
            conn.disconnect();
            jsonObject = JSONObject.fromObject(buffer.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static JSONObject submitpost(String json) throws IOException {
        //创建请求
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(PUBLICKEY);
        // 构建请求配置信息
        RequestConfig config = RequestConfig.custom().setConnectTimeout(1000) // 创建连接的最长时间
                .setConnectionRequestTimeout(500) // 从连接池中获取到连接的最长时间
                .setSocketTimeout(10 * 1000) // 数据传输的最长时间
                .setStaleConnectionCheckEnabled(true) // 提交请求前测试连接是否可用
                .build();
        //设置配置请求
        httpGet.setConfig(config);

        CloseableHttpResponse response = null;

        String content = "";
        try {
            // 执行请求
            response = httpClient.execute(httpGet);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                content = EntityUtils.toString(response.getEntity(),
                        "UTF-8");
//                System.out.println("1." + content);
            }
            JSONObject jsonObject = JSONObject.fromObject(content);
            jsonObject = jsonObject.getJSONObject("data");
            String pm = jsonObject.getString("modulus");
            String pe = jsonObject.getString("exponent");
//            System.out.println("2." + pm);
//            System.out.println("3." + pe);
//            String base64=jsonObject.getString("key");
            RSAPublicKey publicKey = RSAUtils.getPublicKey(Base64.decodeBase64(pm), Base64.decodeBase64(pe));
            String body = RSAUtils.encrypt(publicKey, json);
//            System.out.println("4." + body);
            HttpPost httpPost = new HttpPost(UNSPAY);
            httpPost.setConfig(config);
            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            parameters.add(new BasicNameValuePair("body", body));
            parameters.add(new BasicNameValuePair("sign", MD5Utils.getMD5Str(body + PASSWORD)));

            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parameters);

            httpPost.setEntity(formEntity);

            httpPost.setHeader(
                    "User-Agent",
                    "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.118 Safari/537.36");

            response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                // 获取服务端响应的数据
                content = EntityUtils.toString(response.getEntity(),
                        "UTF-8");
//                System.out.println("5. " + content);
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } finally {
            response.close();
        }
        httpClient.close();
        return JSONObject.fromObject(content);
    }

    public synchronized static String submit(Transfer transfer) throws IOException {
        Map<String, String> map = new HashMap<>();
        map.put("sn", transfer.getSn());
        map.put("bankname", transfer.getBankname());
        map.put("cardno", transfer.getCardno());
        map.put("name", transfer.getName());
        map.put("city", transfer.getCity());
        map.put("amount", transfer.getAmount().toString());
        map.put("member", ID);
        JSONObject jsonObject = JSONObject.fromObject(map);
//        System.out.println("0." + jsonObject);
        //发起远程提交芸店代付
        JSONObject jsonObject1 = submitpost(jsonObject.toString());
//        System.out.println("6." + jsonObject1);
        jsonObject1 = jsonObject1.getJSONObject("data");
        if (jsonObject1 == null || 1 > jsonObject1.size()) {
            try {
                throw new Exception("账户余额不足");
            } catch (Exception e) {
                logger.error(e.getMessage());
                return "9999";
            }
        } else {
            return jsonObject1.getString("sn");
        }
    }

    //00，成功;10，处理中;20，失败  ;其他情况出错了
    public static String query(String sn) {
        Map<String, String> map = new HashMap<>();
        map.put("sn", sn);
        map.put("sign", MD5Utils.getMD5Str(sn + PASSWORD));
        //发起远程芸店链接
        JSONObject jsonObject = post(JSONObject.fromObject(map).toString());
        System.out.println(jsonObject);
        jsonObject = JSONObject.fromObject(jsonObject.get("data"));
        if (jsonObject == null || 1 > jsonObject.size()) {
            return null;
        } else {
            return jsonObject.getString("resp");
        }
    }

    public static void main(String[] args) {
        Transfer transfer = new Transfer();
        transfer.setSn("10001");
        transfer.setBankname("中国工商银行");
        transfer.setCardno("66666666666");
        transfer.setName("老大哥");
        transfer.setCity("哈哇一");
        transfer.setAmount(new BigDecimal("0.08").add(BigDecimal.ONE));
        try {
            System.out.println(submit(transfer));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(query("909"));
    }
}
