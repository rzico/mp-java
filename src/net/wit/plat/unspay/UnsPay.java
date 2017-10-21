package net.wit.plat.unspay;

import com.tls.tls_sigature.tls_sigature;
import net.wit.entity.Member;
import net.wit.entity.Transfer;
import net.wit.plat.im.User;
import net.wit.util.JsonUtils;
import net.wit.util.MD5Utils;
import net.wit.util.StringUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;
import java.util.zip.DataFormatException;

import static com.tls.tls_sigature.tls_sigature.CheckTLSSignatureEx;
import static com.tls.tls_sigature.tls_sigature.GenTLSSignatureEx;

public class UnsPay {
    public static Logger logger = LogManager.getLogger(User.class);
    public static String url="http://180.166.114.155:8081/unspay-external/delegatePay/pay";
    public static String queryurl="http://180.166.114.155:8081/unspay-external/delegatePay/queryOrderStatus";

    /**
     * 连接Map值
     * @param map Map
     * @return 字符串
     */
    protected static String joinValue(Map<String, Object> map) {
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        List<String> keys = new ArrayList<String>(map.keySet());
        List<String> list = new ArrayList<String>();
        Collections.sort(keys);
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i).substring(2);
            String value = null;
            try {
                value = new String(ConvertUtils.convert(map.get(keys.get(i))).getBytes(),"utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (org.apache.commons.lang.StringUtils.isNotEmpty(value)) {
                list.add(key + "=" + (value != null ? value : ""));
            }
        }
        list.add("key="+bundle.getString("x-unspay-key"));
        return  org.apache.commons.lang.StringUtils.join(list, "&");
    }
    /**
     * GET请求
     * @param url URL
     * @param parameterMap 请求参数
     * @return 返回结果
     */
    public static String get(String url, Map<String, Object> parameterMap) {
        Assert.hasText(url);
        String result = null;
        @SuppressWarnings("resource")
        HttpClient httpClient = new DefaultHttpClient();
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            if (parameterMap != null) {
                List<String> keys = new ArrayList<String>(parameterMap.keySet());
                Collections.sort(keys);
                for (int i = 0; i < keys.size(); i++) {
                    String name = keys.get(i).substring(2);
                    String value = ConvertUtils.convert(parameterMap.get(keys.get(i)));
                    if (org.apache.commons.lang.StringUtils.isNotEmpty(name)) {
                        nameValuePairs.add(new BasicNameValuePair(name, value));
                    }
                }
            }
            String gUrl = url + (org.apache.commons.lang.StringUtils.contains(url, "?") ? "&" : "?") + EntityUtils.toString(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            HttpGet httpGet = new HttpGet(gUrl);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            result = EntityUtils.toString(httpEntity, "UTF-8");
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
    public static BigDecimal queryBalance() {
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("01accountId", bundle.getString("x-unspay-partner"));
        String keystr = UnsPay.joinValue(params);
        params.put("08mac", MD5Utils.getMD5Str(keystr).toUpperCase());
        String resp = get(url, params);
        Map<String, String> data = JsonUtils.toObject(resp, Map.class);
        if ("0000".equals(data.get("result_code"))) {
            return new BigDecimal(data.get("balance"));
        } else {
            return BigDecimal.ZERO;
        }
    }
    public synchronized static boolean submit(Transfer transfer) {
        try {
            BigDecimal bal = queryBalance();
            if (bal.compareTo(transfer.effectiveAmount().add(BigDecimal.ONE))<=0) {
               return false;
            }
            ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("01accountId", bundle.getString("x-unspay-partner"));
            params.put("02name", transfer.getName());
            params.put("03cardNo", transfer.getCardno());
            params.put("04orderId", transfer.getSn());
            params.put("05purpose", transfer.getMemo());
            java.text.DecimalFormat   df   =  new   java.text.DecimalFormat("#.00");
            params.put("06amount", df.format(transfer.effectiveAmount()));
            String notifyUrl = bundle.getString("x-unspay-url") +"payment/transfer/" + transfer.getSn() + ".jhtml";
            params.put("06responseUrl", notifyUrl);
            String keystr = UnsPay.joinValue(params);
            params.put("08mac", MD5Utils.getMD5Str(keystr).toUpperCase());
            String resp = get(url, params);
            Map<String, String> data = JsonUtils.toObject(resp, Map.class);
            if ("0000".equals(data.get("result_code"))) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }

    }
    //00，成功;10，处理中;20，失败  ;其他情况出错了
    public static String query(String sn) {
        try {
            ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
            Map<String,Object> params=new HashMap<String,Object>();
            params.put("01accountId",bundle.getString("x-unspay-partner"));
            params.put("02orderId",sn);
            String keystr = UnsPay.joinValue(params);
            params.put("03mac", MD5Utils.getMD5Str(keystr).toUpperCase());
            String resp =get(queryurl,params);
            Map<String,String> data = JsonUtils.toObject(resp,Map.class);
            if ("0000".equals(data.get("result_code"))) {
                return data.get("status").toString();
            } else {
                return "99."+data.get("result_msg").toString();
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return "99.查询出错了";
        }
    }
    //00，成功;10，处理中;20，失败  ;其他情况出错了
    public static String verifyNotify(String sn,HttpServletRequest request) {
        try {
            String result_code = request.getParameter("result_code");
            String result_msg = request.getParameter("result_msg");
            String amount = request.getParameter("amount");
            String orderId = request.getParameter("orderId");
            String mac = request.getParameter("mac");
            Map<String, Object> params = new HashMap<String, Object>();
            ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
            params.put("01accountId",bundle.getString("x-unspay-partner"));
            params.put("02orderId", orderId);
            params.put("03amount", amount);
            params.put("04result_code", result_code);
            params.put("05result_msg", result_msg);
            String keystr = UnsPay.joinValue(params);
            System.out.println(keystr);
            if (mac.equals(MD5Utils.getMD5Str(keystr).toUpperCase())) {
                System.out.println(result_msg);
                if ("0000".equals(result_code) && sn.equals(orderId)) {
                    return "00";
                } else {
                    return "20";
                }
            } else {
                return "99";
            }
        }
        catch (Exception e ) {
            logger.error(e.getMessage());
            return "99";
        }
    }

}
