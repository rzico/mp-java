package net.wit.plat.weixin.propa;


import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;
import net.wit.plat.weixin.FormEntity.*;
import net.wit.plat.weixin.util.WeixinApi;
import org.apache.commons.io.IOUtils;
import org.springframework.util.Assert;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

/**
 * Created by Eric on 2018/1/10.
 */
public class ArticlePropa {

    //图片上传永久素材
    private static final String UPLOAD_IMG = "https://api.weixin.qq.com/cgi-bin/media/uploadimg?access_token=ACCESS_TOKEN";

    //上传其他永久素材
    private static final String UPLOAD_DATA="https://api.weixin.qq.com/cgi-bin/material/add_material?access_token=ACCESS_TOKEN&type=TYPE";

    //上传永久图文消息素材
    private static final String UPLOAD_NEWS = "https://api.weixin.qq.com/cgi-bin/material/add_news?access_token=ACCESS_TOKEN";

    //获取永久素材
    private static final String DOWNLOAD_DATA = "https://api.weixin.qq.com/cgi-bin/material/get_material?access_token=ACCESS_TOKEN";

    //预览
    private static final String PREVIEW="https://api.weixin.qq.com/cgi-bin/message/mass/preview?access_token=ACCESS_TOKEN";

    //标签群发
    private static final String TAGMASS="https://api.weixin.qq.com/cgi-bin/message/mass/sendall?access_token=ACCESS_TOKEN";

    //获取群发速度
    private static final String GETSPEED="https://api.weixin.qq.com/cgi-bin/message/mass/speed/get?access_token=ACCESS_TOKEN";

    //设置群发速度
    private static final String SETSPEED="https://api.weixin.qq.com/cgi-bin/message/mass/speed/set?access_token=ACCESS_TOKEN";

    //查询群发消息发送状态
    private static final String GETMASS="https://api.weixin.qq.com/cgi-bin/message/mass/get?access_token=ACCESS_TOKEN";

    //删除群发
    private static final String DELETEMASS="https://api.weixin.qq.com/cgi-bin/message/mass/delete?access_token=ACCESS_TOKEN";

    //删除永久素材
    private static final String DELETE="https://api.weixin.qq.com/cgi-bin/material/del_material?access_token=ACCESS_TOKEN";

    //获取素材数量
    private static final String MATERIAL="https://api.weixin.qq.com/cgi-bin/material/get_materialcount?access_token=ACCESS_TOKEN";

    //获取素材列表详情
    private static final String GETLIST="https://api.weixin.qq.com/cgi-bin/material/batchget_material?access_token=ACCESS_TOKEN";

    //上传视频新地址
    //没用 上传报错45002
    private static final String UPVIDEO="https://api.weixin.qq.com/cgi-bin/media/uploadvideo?access_token=ACCESS_TOKEN";


    /**
     * 把二进制流转化为byte字节数组
     *
     * @param instream
     * @return byte[]
     * @throws Exception
     */
    public static byte[] readInputStream(InputStream instream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = instream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        instream.close();
        return outStream.toByteArray();
    }

    /**
     * 把URL转换成文件
     * @param url 文件路径
     * @param type 文件后缀格式
     */
    public static File UrlToFile(String url,String type, String path) {
        //new一个文件对象用来保存文件，默认保存当前工程根目录
        File material =new File(path+"faker."+type);
        try {
            if(!material.exists()) {
                material.getParentFile().mkdir();
                material.createNewFile();
            }
            //new一个URL对象
            URL imgurl = new URL(url);
            //打开链接
            HttpURLConnection conn = (HttpURLConnection) imgurl.openConnection();
            //设置请求方式为"GET"
            conn.setRequestMethod("GET");
            //超时响应时间为5秒
            conn.setConnectTimeout(5 * 1000);
            //通过输入流获取图片数据
            InputStream inStream = conn.getInputStream();
            //得到图片的二进制数据，以二进制封装得到数据，具有通用性
            byte[] data = readInputStream(inStream);
            FileOutputStream outStream = new FileOutputStream(material);
            //写入数据
            outStream.write(data);
            //关闭输出流
            inStream.close();
            outStream.close();
            return material;
        } catch (Exception e) {
            return material;
        }
    }

    /**
     * 删除文件
     * @param file 文件路径
     */
    public static void deleteFile(File file) {

        File[] files = file.listFiles();

        //2.对该数组进行遍历
        for (File f : files) {
            //3.判断是否有目录，如果有，继续使用该功能遍历，如果不是文件夹，直接删除
            if (f.isDirectory()) {
                deleteFile(f);
            } else {
                f.delete();//文件删除
            }
        }
        file.delete();//最后删除文件夹
    }


    /**
     * 微信服务器永久素材上传
     *
     * @param file  所需上传的素材文件
     * @param token access_token
     * @param type TYPE只支持四种类型素材(video/image/voice/thumb)
     *             图片（image）: 2M，支持bmp/png/jpeg/jpg/gif格式
                   语音（voice）：2M播放长度不超过60s，mp3/wma/wav/amr格式
                   视频（video）：10MB，支持MP4格式
                   缩略图（thumb）：64KB，支持JPG格式
     * 如果不需要上传视频文件以下两个字段可以为空
     * @param style 文件后缀名
     * @param title 视频标题
     * @param introduction 视频描述
     */
    public static Details uploadMedia(File file, String token, String type,String style, String title, String introduction) throws Exception {
        if(file==null||token==null){
            return null;
        }
        if(!file.exists()){
            return null;
        }
        URL url;
        url = new URL(UPLOAD_DATA.replace("ACCESS_TOKEN",token).replace("TYPE",type));
        HttpURLConnection conn = null;
        conn = (HttpURLConnection) url.openConnection();
        //设置请求参数
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(30000);
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setUseCaches(false);
        conn.setRequestMethod("POST");
        //请求头
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setRequestProperty("Content-type", "application/x-java-serialized-object");
        conn.setRequestProperty("Charset", "UTF-8");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
        //设置边界
        String BOUNDARY = "----------" + System.currentTimeMillis();
        conn.setRequestProperty("Content-Type", "multipart/form-data; boundary="
                + BOUNDARY);
        // 请求正文信息
        OutputStream out;
        StringBuffer upimg = new StringBuffer();
        upimg.append("--");// 必须多两道线
        upimg.append(BOUNDARY);
        upimg.append("\r\n");
        upimg.append("Content-Disposition: form-data;name=\"media\";filename=\""
                + System.currentTimeMillis() + "."+style+"\"\r\n");//根据系统时间定义上传文件名字防重复
        upimg.append("Content-Type:application/octet-stream\r\n\r\n");

        byte[] head = upimg.toString().getBytes("utf-8");
        // 获得输出流
        out = new DataOutputStream(conn.getOutputStream());
        //写入POST请求参数
        out.write(head);

        // 把文件已流文件的方式 推入到url中
        DataInputStream in = new DataInputStream(new FileInputStream(file));
        int bytes = 0;
        byte[] bufferOut = new byte[1024];
        while ((bytes = in.read(bufferOut)) != -1) {
            out.write(bufferOut, 0, bytes);
        }
        in.close();
        if (type.equals("video")&&!title.equals("")&&!introduction.equals("")) { //判断上传视频时所需要的数据是否全部到位
            out.write(("--" + BOUNDARY + "\r\n").getBytes());
            out.write("Content-Disposition: form-data; name=\"description\";\r\n\r\n".getBytes());
            out.write(String.format("{\"title\":\"%s\", \"introduction\":\"%s\"}", title, introduction).getBytes());
        }
        out.write(("\r\n--" + BOUNDARY + "--\r\n\r\n").getBytes());
        out.flush();
        out.close();
        StringBuffer buffer = new StringBuffer();
        BufferedReader reader = null;
        String result=null;
        try {
            reader = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            result = buffer.toString();
        } catch (IOException e) {
            System.out.println("发送POST请求出错。" + UPLOAD_DATA);
            e.printStackTrace();
        } finally {
            if (conn != null||reader!=null) {
                conn.disconnect();
                conn = null;
                reader.close();
            }
        }
        JSONObject jsonObject=JSONObject.fromObject(result);
//        System.out.println(jsonObject);
        return (Details) JSONObject.toBean(jsonObject,Details.class);
    }
    /**
     * 微信服务器永久图文素材上传
     *
     * @param details 上传图文所对应实体类
     * @param token access_token
     *
     */
    public static Details UpNews(UpArticle details,String token){
        //过滤空值项
        JsonConfig jsonConfig = new JsonConfig();
        PropertyFilter filter = new PropertyFilter() {
            public boolean apply(Object object, String fieldName, Object fieldValue) {
                return null == fieldValue;
            }
        };
        jsonConfig.setJsonPropertyFilter(filter);
//        System.out.println(JSONObject.fromObject(details, jsonConfig).toString());
//        JSONObject jsonObject=JSONObject.fromObject(details, jsonConfig);
        JSONObject jsonObject = WeixinApi.httpRequest(UPLOAD_NEWS.replace("ACCESS_TOKEN",token),"POST",JSONObject.fromObject(details, jsonConfig).toString());
        System.out.println(jsonObject.toString()+"upnews的");
        return (Details) JSONObject.toBean(jsonObject,Details.class);
    }

    /**
     * 微信服务器素材预览
     *
     * @param openId 发给所要预览的单独用户
     * @param token access_token
     * @param mediaId 需要预览的图文视频唯一标识
     * @param type 需要预览的素材类型
     *             图文消息为mpnews，文本消息为text，语音为voice，图片为image，视频为mpvideo，卡券为wxcard
     */
    public static JSONObject Preview(String openId,String token,String mediaId,String type,String content){
        String str="";
        if(type.equals("text")){
            str="{\"touser\":\""+openId+"\",\""+type+"\":{\"content\":\""+ content +"\"},\"msgtype\":\""+type+"\"}";
        }else if(type.equals("wxcard")){
            //预览卡券
        }else {
            str = "{\"towxname\":\"" + openId + "\",\"" + type + "\":{\"media_id\": \"" + mediaId + "\"},\"msgtype\":\"" + type + "\"}";
        }
        JSONObject jsonObject = WeixinApi.httpRequest(PREVIEW.replace("ACCESS_TOKEN",token),"POST",str);
        System.out.println(jsonObject.toString());
        return jsonObject;
    }

    /**
     * 微信服务器图文素材自动生成
     *
     * @param templatePath 模板路径
     * @param filename 模板文件名包括后缀
     * @param articles 需要放入模板的文件
     */
    public static String build(String templatePath,String filename, List<Map<String, String>> articles) {
        Assert.hasText(templatePath);

        Writer writer = null;
        StringWriter stringWriter=null;
        Configuration config = new Configuration();
        try {
            config.setDirectoryForTemplateLoading(new File(templatePath));
            // 设置对象包装器
            config.setObjectWrapper(new DefaultObjectWrapper());
            config.setLocale(Locale.CHINA);
            config.setEncoding(Locale.CHINA, "UTF-8");
            // 设置异常处理器
            config.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
            Template template=config.getTemplate(filename);
            stringWriter =new StringWriter();
            writer = new BufferedWriter(stringWriter);
            Map<String, Object> model =new HashMap<>();
            model.put("articles",articles);
            //不知道为什么 一定要用hashmap传值
            template.process(model, writer);
            writer.flush();
            return stringWriter.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(writer);
//            IOUtils.closeQuietly(outputStreamWriter);
            IOUtils.closeQuietly(stringWriter);
        }
        return null;
    }

    /**
     * 微信公众号按标签群发
     *
     * @param token access_token
     * @param tagPropa 群发属性所对应的实体类
     */
    public static ReturnJson TagPropa(String token, TagPropa tagPropa){
        JsonConfig jsonConfig = new JsonConfig();
        PropertyFilter filter = new PropertyFilter() {
            public boolean apply(Object object, String fieldName, Object fieldValue) {
                return null == fieldValue;
            }
        };
        jsonConfig.setJsonPropertyFilter(filter);
        JSONObject jsonObject = WeixinApi.httpRequest(TAGMASS.replace("ACCESS_TOKEN",token),"POST",JSONObject.fromObject(tagPropa, jsonConfig).toString());
        return (ReturnJson) JSONObject.toBean(jsonObject,ReturnJson.class);
    }


    /**
     * 微信公众号获取群发速度
     *
     * @param token access_token
     */
    public static ReturnJson getSpeed(String token){
        JSONObject jsonObject=WeixinApi.httpRequest(ArticlePropa.GETSPEED.replace("ACCESS_TOKEN",token),"POST","");
        return (ReturnJson) JSONObject.toBean(jsonObject,ReturnJson.class);
    }


    /**
     * 微信公众号设置群发速度
     *
     * @param token access_token
     * @param speed 群发速度的级别 分为0-4   5个级别 0最快 4最慢
     */
    public static ReturnJson setSpeed(String token,int speed){
        String string="{\"speed\":"+speed+"}";
        JSONObject jsonObject=WeixinApi.httpRequest(ArticlePropa.SETSPEED.replace("ACCESS_TOKEN",token),"POST",string);
        return (ReturnJson) JSONObject.toBean(jsonObject,ReturnJson.class);
    }


    /**
     * 微信公众号查询群发消息发送状态
     *
     * @param token access_token
     * @param messid 群发发送成功时的回调mess_id
     */
    public static ReturnJson getMass(String token,int messid){
        String string="{\"msg_id\":\""+messid+"\"}";
        JSONObject jsonObject=WeixinApi.httpRequest(ArticlePropa.GETMASS.replace("ACCESS_TOKEN",token),"POST",string);
        return (ReturnJson) JSONObject.toBean(jsonObject,ReturnJson.class);
    }

    /**
     * 微信公众号删除群发
     *
     * @param token access_token
     * @param messid 群发发送成功时的回调mess_id
     */
    public static ReturnJson deleteMass(String token,int messid,int articleIdx ){
        String string="{\"msg_id\": "+messid+",\"article_idx\": "+ articleIdx+"}";
        JSONObject jsonObject=WeixinApi.httpRequest(ArticlePropa.DELETEMASS.replace("ACCESS_TOKEN",token),"POST",string);
        return (ReturnJson) JSONObject.toBean(jsonObject,ReturnJson.class);
    }


    /**
     * 微信公众号永久素材的删除
     *
     * @param token access_token
     * @param mediaId 需要删除素材的MediaId
     */
    public static ReturnJson mediaDelete(String token,String mediaId){
        String string ="{\"media_id\":\""+ mediaId+"\" }";
        JSONObject jsonObject=WeixinApi.httpRequest(ArticlePropa.DELETE.replace("ACCESS_TOKEN",token),"POST",string);
        return (ReturnJson) JSONObject.toBean(jsonObject,ReturnJson.class);
    }


    /**
     * 微信公众号获取永久素材的列表及素材详情
     *
     * @param token access_token
     * @param type 素材的类型，图片（image）、视频（video）、语音 （voice）、图文（news）
     * @param offset 从全部素材的该偏移位置开始返回，0表示从第一个素材 返回
     * @param count 返回素材的数量，取值在1到20之间
     */
    public static ReturnJson listing(String token,String type,int offset,int count){
        String string = "{\"type\": \"" + type + "\",\"offset\":" + offset + ",\"count\":" + count + "}";
        JSONObject jsonObject=WeixinApi.httpRequest(ArticlePropa.GETLIST.replace("ACCESS_TOKEN",token),"POST",string);
        System.out.println(jsonObject);
        Map<String, Class> m = new HashMap<String, Class>();
        m.put("item",Minutia.class);
        m.put("content",NewsItem.class);
        m.put("news_item",Content.class);
        ReturnJson returnJson=(ReturnJson) JSONObject.toBean(jsonObject , ReturnJson.class,m);
        return returnJson;
    }


    /**
     * 微信公众号获取永久素材数量清单
     *
     * @param token access_token
     */
    public static ReturnJson getCount(String token){
        JSONObject jsonObject=WeixinApi.httpRequest(ArticlePropa.MATERIAL.replace("ACCESS_TOKEN",token),"GET","");
        return (ReturnJson) JSONObject.toBean(jsonObject,ReturnJson.class);
    }


    /**
     * 微信公众号获取单个永久素材
     *
     * @param token access_token
     * @param mediaId 需要获取的素材的唯一标识（只可使用视频和图文的media_id）
     * @return  只可用于查询视频素材和图文素材
     * 其他类型的素材消息，则响应的直接为素材的内容，开发者可以自行保存为文件
     */
    public static JSONObject detailed(String token,String mediaId){
        String string = "{\"media_id\":\""+ mediaId+"\"}";
        return WeixinApi.httpRequest(ArticlePropa.DOWNLOAD_DATA.replace("ACCESS_TOKEN",token),"POST",string);
    }


}
