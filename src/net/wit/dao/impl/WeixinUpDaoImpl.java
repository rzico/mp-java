package net.wit.dao.impl;

import net.sf.json.JSONObject;
import net.wit.controller.model.ArticleContentModel;
import net.wit.controller.model.ArticleModel;
import net.wit.dao.WeixinUpDao;
import net.wit.entity.Article;
import net.wit.plat.weixin.FormEntity.*;
import net.wit.plat.weixin.pojo.AccessToken;
import net.wit.plat.weixin.propa.ArticlePropa;
import net.wit.plat.weixin.util.WeixinApi;
import net.wit.plugin.StoragePlugin;
import net.wit.service.ArticleService;
import net.wit.service.PluginService;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Eric on 2018/1/15.
 */
@Repository("weixinUpDaoImpl")
public class WeixinUpDaoImpl implements WeixinUpDao{

    @Resource(name = "articleServiceImpl")
    ArticleService articleService;

    @Resource(name = "pluginServiceImpl")
    private PluginService pluginService;



    public String ArticleUpLoad(Long[] ids,String appID,String appsecret){

        //测试号
//        String appID="wxd570fe49cd2fcc9d";
//        String appsecret="4f6a7438f0b632abdc7f6e58d07f026d";

        //芸店
//        String appID="wx88a1ec3b5c3bc9c3";
//        String appsecret="f5e7d000d00788053c50ca6b3a442d20";

        //1.获取TOKEN
        AccessToken accessToken= WeixinApi.getAccessToken(appID,appsecret);
        UpArticle upArticle=new UpArticle();
        List<Details> detailses=new ArrayList<>();
        //2.查找多篇文章 并对多篇文章进行群发前的上传处理
        List<Article>articles=articleService.findList(ids);
        for(Article article:articles){
            ArticleModel m = new ArticleModel();
            m.bind(article);
            if(m==null){
                continue;
            }

            //3.上传该图文的封面缩略图
            Details deta=null;
            try {
                deta=ArticlePropa.uploadMedia(ArticlePropa.UrlToFile(m.getThumbnail(),"jpg"),accessToken.getToken(),"thumb","jpg","","");
            } catch (Exception e) {
                e.printStackTrace();
            }

            //4.整理文章内容 并把其中所对应的文件先行一步上传入微信服务器 并由微信服务器返回的URL 替换现有的URL
            List<Map<String, String>> contents = new ArrayList<>();
            Details details = new Details();
            List<ArticleContentModel> articleContentModels=m.getTemplates();
            int h=0;
            for (int j=0;j<articleContentModels.size();j++) {
                Map<String, String> map = new HashMap<String, String>();
                JSONObject jsonObject = JSONObject.fromObject(articleContentModels.get(j));
                Iterator<String> iterator = jsonObject.keys();
                String key = null;
                String value = null;
                String type="";
                while (iterator.hasNext()) {
                    key = iterator.next();
                    value = jsonObject.getString(key);
//                    Details details1 = new Details();
//                    //获取媒体类型   微信开发文档中写明会过滤外网URL 实际测试不会 微信服务器会把内容中的图文，图片等素材素材链接过滤掉
//                    //后期微信更新时在修改 一下以下代码就可接着使用
//                    if(key.equals("mediaType")){
//                        if (!value.equals("")) {
//                            type=value;
//                        }
//                    }
//                    if (key.equals("original")&&type.equals("audio")) {
//                        if (!value.equals("")) {
//                            //获取文件的后缀名
//                            String str=value.substring(value.lastIndexOf(".")+1,value.length());
//                            if(type.equals("audio")){
//                                type="voice";
//                            }
//                            try {
//                                File file =ArticlePropa.UrlToFile(value, str);
//                                details1 = ArticlePropa.uploadMedia(file,accessToken.getToken(), type,str, "", "");
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                            if (details1.getMedia_id() != null) {
//                                map.put("thumbnail",details1.getMedia_id());
//                            }else{
//                                System.out.println(article.getId()+""+h+"上传失败");
//                            }
//                            ++h;
//                        }
//                    }
                    if(key.equals("url")&&value!=null&&value.equals("")){
                        details.setContent_source_url(value);
                    }
                    map.put(key, value);
                }
                contents.add(map);
            }


            //5.调用freemark模板自动生成相应的图文内容 并更新现有的图文内容
            String s=ArticlePropa.build("H:\\mopian\\mp-java\\WebContent\\WEB-INF\\template\\common","article.ftl",contents);

            System.out.println(s);

            //6.添加图文素材上传信息
            details.setContent(s);
            details.setTitle(article.getTitle());
            details.setAuthor(article.getAuthor());
            if (deta.getMedia_id() == null) {
                System.out.println(article.getId() + "该文章缩略图上传失败");
                details.setThumb_media_id("");
            } else {
                details.setThumb_media_id(deta.getMedia_id());
            }
            details.setDigest("");
            details.setShow_cover_pic("1");
            detailses.add(details);
        }
        upArticle.setArticles(detailses);

        //7.上传图文素材
        Details detail = ArticlePropa.UpNews(upArticle, accessToken.getToken());

        //8.1测试图文预览
        ArticlePropa.Preview("HitmanTsuna",accessToken.getToken(),detail.getMedia_id(),"mpnews","");
        ArticlePropa.Preview("yk1398222319",accessToken.getToken(),detail.getMedia_id(),"mpnews","");

        //8.2图文群发
//        TagPropa tagPropa=new TagPropa();
//        Filter filter =new Filter();
//        filter.setIs_to_all(true);
//        tagPropa.setFilter(filter);
//        //要发送的素材内容
//        Details mpnews=new Details();
//        mpnews.setMedia_id(detail.getMedia_id());
//        tagPropa.setMpnews(mpnews);
//        //群发的消息类型
//        tagPropa.setMsgtype("mpnews");
//        //图文消息被判定为转载时，是否继续群发
//        tagPropa.setSend_ignore_reprint("1");
//        //群发
//        ReturnJson returnJson=ArticlePropa.TagPropa(accessToken.getToken(),tagPropa);
//        System.out.println(returnJson.getMsg_id()+"-------"+returnJson.getErrcode());
        return "success";
    }

    public StringBuffer DownArticle(String url) throws IOException {
        StringBuffer stringBuffer = new StringBuffer();

        try {
            URL imgurl = new URL(url);
            //打开链接
            HttpURLConnection conn = (HttpURLConnection) imgurl.openConnection();
            //设置请求方式为"GET"
            conn.setRequestMethod("GET");
            //超时响应时间为5秒
            conn.setConnectTimeout(5 * 1000);
            //通过输入流获取数据
            InputStream inStream = conn.getInputStream();
            //将输入流转换成字符输出高效流
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inStream,"UTF-8"));
            while(bufferedReader.readLine()!=null){
                stringBuffer.append(bufferedReader.readLine());
            }
            System.out.println(stringBuffer.toString().length());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //文字保存在<span>中 图片保存在<img>中 这些都是保存在<p>中
        Pattern p = Pattern.compile("(<p(.*?)?>)(.*?)(</p>)");
        Matcher m = p.matcher(stringBuffer);
        StringBuffer stringBuffer1=new StringBuffer("[");
        int h=0;
        while(m.find()) {
            String s=m.group();
            //带有class的P标签都是微信自动生成的 不属于文本内容应过滤
            if (s.contains("class")&&!s.contains("img")){
                continue;
            }else if(s.contains("<p class")){
                continue;
            }
            //带有sction的标签也过滤掉，不属于文本内容，属于微信文章自动生成的文章标题属性等
            //原文中的A标签超链接也过滤掉
            if (s.contains("</sction>")||s.contains("</a>")){
                continue;
            }
            //如果有图片
            if (h!=0){
                stringBuffer1.append(",");
            }
            if (s.contains("img")){
                stringBuffer1.append("{\"mediaType\":\"image\",\"thumbnail\":\"\",\"original\":\"");
                String[] str=s.split(" ");
                for(int i=0;i<str.length;i++){
                    if(str[i].contains("data-src")){
                        //微信图片路径
                        String surl=str[i].replace("data-src=\"","").replace("\"","");
                        //说明这个图片是emoji表情,可以不用截取上传也能使用
                        if(surl.contains("emoji")){
                            stringBuffer1.append(surl);
                            continue;
                        }
                        //图片格式
                        String shz=str[i+1].replace("data-type=\"","").replace("\"","");
                        //文件下载
                        File file= ArticlePropa.UrlToFile(surl,shz);
                        FileInputStream in_file = new FileInputStream(file);
                        MultipartFile multi = new MockMultipartFile(System.currentTimeMillis()+"."+shz, in_file);
                        StoragePlugin ossPlugin = pluginService.getStoragePlugin("ossPlugin");
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                        String folder=sdf.format(System.currentTimeMillis());
                        String filename= String.valueOf(System.currentTimeMillis()*1000000+System.nanoTime());
                        String uppath="/upload/image/"+folder+"/"+filename+"."+shz;
                        ossPlugin.upload(uppath,multi,ossPlugin.getMineType("."+shz));
                        System.out.println(ossPlugin.getUrl(uppath));
                        stringBuffer1.append(ossPlugin.getUrl(uppath));
                    }
                }
                stringBuffer1.append("\",\"content\":\"\"}");
                h++;
                continue;
            }
            stringBuffer1.append("{\"mediaType\":\"html\",\"thumbnail\":\"\",\"original\":\"\",\"content\":\"");
            stringBuffer1.append(s.replace("\"","\\\""));
            stringBuffer1.append("\"}");
            h++;
//            System.out.println(m.group());
        }
        stringBuffer1.append("]");
        System.out.println(stringBuffer1.toString().length());
        System.out.println(stringBuffer1);
        return stringBuffer1;
    }
}
