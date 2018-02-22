package net.wit.dao.impl;

import net.sf.json.JSONObject;
import net.wit.controller.model.ArticleContentModel;
import net.wit.controller.model.ArticleModel;
import net.wit.dao.WeixinUpDao;
import net.wit.entity.Article;
import net.wit.entity.ArticleContent;
import net.wit.entity.ArticleTemplate;
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
import java.rmi.server.ExportException;
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



    public String ArticleUpLoad(Long[] ids,String appID,String appsecret,String templatepath){

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
            File urlToFile=ArticlePropa.UrlToFile(m.getThumbnail(),"jpg",templatepath);
            try {
                deta=ArticlePropa.uploadMedia(urlToFile,accessToken.getToken(),"thumb","jpg","","");
                if (deta==null|deta.getErrcode()!=null){
                    if(deta==null) {
                        System.out.println(m.getId() + "此文章缩略图上传失败,默认跳过该文章上传");
                        continue;
                    }
                    if(deta.getErrcode()!=null){
                        System.out.println(m.getId() + "此文章缩略图上传失败,默认跳过该文章上传错误码:"+deta.getErrcode());
                        continue;
                    }
                    continue;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                urlToFile.delete();
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
//                String type="";
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
            String s=ArticlePropa.build(templatepath+"WEB-INF/template/common","article.ftl",contents);

//            System.out.println(s);

            //6.添加图文素材上传信息
            details.setContent(s);
            details.setTitle(article.getTitle());
            details.setAuthor(article.getAuthor());
            details.setThumb_media_id(deta.getMedia_id());
            details.setDigest("");
            details.setShow_cover_pic("1");
            detailses.add(details);
        }
        upArticle.setArticles(detailses);

        //7.上传图文素材
        Details detail = ArticlePropa.UpNews(upArticle, accessToken.getToken());
        if(detail==null|detail.getErrcode()!=null){
            System.out.println("该多图文消息上传失败错误码:"+detail.getErrcode());
            return "error";
        }

        //8.1测试图文预览
        ArticlePropa.Preview("HitmanTsuna",accessToken.getToken(),detail.getMedia_id(),"mpnews","");
        ArticlePropa.Preview("yk1398222319",accessToken.getToken(),detail.getMedia_id(),"mpnews","");

        //8.2图文群发 发送给所有关注该公众号的用户
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
//        if(returnJson.getErrcode()==0?false:true){
//            System.out.println("群发失败错误码:"+returnJson.getErrcode());
//            return "error";
//        }
        //群发成功返回群发消息ID
//        return String.valueOf(returnJson.getMsg_id());
        return "success";
    }

    public JSONObject DownArticle(String url) throws IOException {
        StringBuffer stringBuffer=new StringBuffer();

        if(!(url.contains("mp.weixin.qq.com")||url.contains("weibo.com/ttarticle")||url.contains("m.eqxiu.com"))){
            return null;
        }

        stringBuffer=downArticle(url);
        if (stringBuffer==null||stringBuffer.equals("")){
            return null;
        }

        if (url.contains("mp.weixin.qq.com")){
            return weixinArticle(stringBuffer);
        }

        if (url.contains("weibo.com/ttarticle")){
//            return weiboArticle(stringBuffer);
        }

        if (url.contains("m.eqxiu.com")){
            return eqixiuArticle(stringBuffer);
        }

        return null;
    }

    public StringBuffer downArticle(String url){
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
            String s=null;
            while((s=bufferedReader.readLine())!=null){
                stringBuffer.append(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return stringBuffer;
    }

    //微信爬虫算法
    public JSONObject weixinArticle(StringBuffer stringBuffer){
        ArticleTemplate articleTemlate=new ArticleTemplate();
        articleTemlate.setTitle(" ");
        articleTemlate.setThumbnail(" ");
        List<ArticleContent> articleContents=new ArrayList<>();
        String tempPath = System.getProperty("java.io.tmpdir");
        Pattern p = Pattern.compile("(<p(.*?)?>)(.*?)(</p>)");
        Matcher m = p.matcher(stringBuffer);
        while(m.find()) {
            ArticleContent articleContent=new ArticleContent();
            articleContent.setMediaType("image");
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

            //过滤换行标签
            if (Pattern.compile("(>([^A-Za-z0-9一-龥_])*?(<( )?(/)?br(.*?)?>)+([^A-Za-z0-9一-龥_])*?<)").matcher(s).find()){
                continue;
            }
            //过滤行空格
            if(Pattern.compile("((<span(.*?)?>)([^A-Za-z0-9一-龥_])*?(( )?&nbsp;( )?)+([^A-Za-z0-9一-龥_])*?(</span>))").matcher(s).find()){
                continue;
            }
            //当span中只有br 和 &nbsp;混合调用时 过滤
            if(Pattern.compile("((<span(.*?)?>)([^A-Za-z0-9一-龥_])*?(( )?&nbsp;( )?)+?([^A-Za-z0-9一-龥_])*?(<( )?(/)?br(.*?)?>)+?([^A-Za-z0-9一-龥_])*?(</span>))").matcher(s).find()){
                continue;
            }
            //当span标签中内容无效 过滤
            //当出现多个span标签嵌套的情况
            int h=0;
            int j=0;
            if((j=s.indexOf("</span>"))>0){
                h++;
                while ((j=s.indexOf("</span>",j+1))>0){
                    h++;
                }
            }
            //END获取多少个SPAN嵌套
            //根据多重span嵌套自动拼装不同正则表达式
            StringBuffer regular=new StringBuffer("(<span(.*?)?>)([^A-Za-z0-9一-龥_])*?");
            for(int i=1;i<h;i++){
                regular.append("(<span(.*?)?>)([^A-Za-z0-9一-龥_])*?(</span>)([^A-Za-z0-9一-龥_])*?");
            }
            regular.append("(</span>)");
            if(Pattern.compile(regular.toString()).matcher(s).find()){
                continue;
            }

            if (s.contains("img")){
                String[] str=s.split(" ");
                for (int i = 0; i < str.length; i++) {

                    if (str[i].contains("data-src")) {
                        //微信图片路径
                        String surl = str[i].replace("data-src=\"", "").replace("\"", "");

                        //说明这个图片是emoji表情,可以不用截取上传也能使用
                        if (surl.contains("emoji")) {
                            articleContent.setThumbnail(surl);
                            articleContent.setOriginal(surl);
                            articleContent.setContent("");
                            continue;
                        }

                        //图片格式
                        if (!str[i + 1].contains("data-type")) {
                            continue;
                        }
                        String shz = str[i + 1].replace("data-type=\"", "").replace("\"", "");

                        //文件下载
                        File file = ArticlePropa.UrlToFile(surl, shz, tempPath);
                        try {
                            FileInputStream in_file = new FileInputStream(file);
                            MultipartFile multi = new MockMultipartFile(System.currentTimeMillis() + "." + shz, in_file);
                            StoragePlugin ossPlugin = pluginService.getStoragePlugin("ossPlugin");
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                            String folder1 = sdf.format(System.currentTimeMillis());
                            String filename = String.valueOf(System.currentTimeMillis() * 1000000 + (int) ((Math.random() * 9 + 1) * 100000));
                            String uppath = "/upload/image/" + folder1 + "/" + filename + "." + shz;
                            ossPlugin.upload(uppath, multi, ossPlugin.getMineType("." + shz));
                            String string=ossPlugin.getUrl(uppath);
                            articleContent.setThumbnail(string);
                            articleContent.setOriginal(string);
                            articleContent.setContent("");
                        }
                        catch (Exception e){

                        }
                        finally {
                            file.delete();
                        }
                    }
                }
            }
            else{
                articleContent.setThumbnail("");
                articleContent.setOriginal("");
                articleContent.setContent(s);
            }
            articleContents.add(articleContent);
        }
        articleTemlate.setTemplates(articleContents);
        return JSONObject.fromObject(articleTemlate);
    }

    //微博爬虫算法
    private JSONObject weiboArticle(StringBuffer stringBuffer) {
        System.out.println(stringBuffer);
        ArticleTemplate articleTemplate=new ArticleTemplate();
        Matcher a;
        //设置文章标题
        if((a=Pattern.compile("(<title>){1}(.*?)(</title>){1}").matcher(stringBuffer)).find()){
            System.out.println(a.group());
            articleTemplate.setTitle(a.group().replace("<title>","").replace("</title>",""));
        }else {
            articleTemplate.setTitle("");
        }

        //设置文章封面
        if((a=Pattern.compile("<img node-type=(.+?)src=(.+?)>").matcher(stringBuffer)).find()){
            System.out.println(a.group()+"  "+a.group(0));
            articleTemplate.setThumbnail(a.group(1).replace("\"",""));
        }else{
            articleTemplate.setThumbnail("");
        }

        //设置文本内容
        List<ArticleContent> articleContents=new ArrayList<>();
        Matcher m=Pattern.compile("(<p(.*?)?>)(.*?)(</p>)").matcher(stringBuffer);
        while (m.find()){
            ArticleContent articleContent=new ArticleContent();
            articleContent.setMediaType("image");
            String s=m.group();
            System.out.println(s);
            //判断是否为图片
            if(Pattern.compile("<img(.+?)>").matcher(s).find()){
                String[] str= s.split(" ");
                for(int i=0;i<str.length;i++){
                    if(str[i].contains("src=")){
                        articleContent.setThumbnail(str[i].replace("src=\"","").replace("\"",""));
                        articleContent.setOriginal(str[i].replace("src=\"","").replace("\"",""));
                        articleContent.setContent("");
                    }
                }
            }
            else{
                articleContent.setThumbnail("");
                articleContent.setOriginal("");
                articleContent.setContent(s);
            }
            articleContents.add(articleContent);
        }
        articleTemplate.setTemplates(articleContents);

        return JSONObject.fromObject(articleTemplate);
    }

    //易企秀爬虫JSON获取地址
    private JSONObject eqixiuArticle( StringBuffer stringBuffer){
        Matcher a;
        StringBuffer stringBuffer1=new StringBuffer("https://a.eqxiu.com/eqs/page/");
        System.out.println(stringBuffer1);
        if((a=Pattern.compile("id:([0-9])*,").matcher(stringBuffer)).find()){
            stringBuffer1.append(a.group().replace("id:","").replace(",",""));
            System.out.println(stringBuffer1);
        }

        if((a=Pattern.compile("code:\"([0-9a-zA-Z])*\",").matcher(stringBuffer)).find()){
            stringBuffer1.append("?code="+a.group().replace("code:\"","").replace("\",",""));
            System.out.println(stringBuffer1);
        }

        if((a=Pattern.compile("createTime:([0-9])*,").matcher(stringBuffer)).find()){
            stringBuffer1.append("&time="+a.group().replace("createTime:","").replace(",",""));
            System.out.println(stringBuffer1);
        }
        return eqxiuArticle(stringBuffer1);
    }

    //易企秀文本爬虫算法
    private JSONObject eqxiuArticle(StringBuffer stringBuffer1) {
        StringBuffer stringBuffer=downArticle(stringBuffer1.toString());

        if(stringBuffer==null||stringBuffer.equals("")){
            return null;
        }

        System.out.println(stringBuffer);
        return JSONObject.fromObject(stringBuffer);
    }
}
