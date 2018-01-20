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
import net.wit.service.ArticleService;
import net.wit.service.impl.ArticleServiceImpl;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.io.File;
import java.util.*;

/**
 * Created by Eric on 2018/1/15.
 */
@Repository("weixinUpDaoImpl")
public class WeixinUpDaoImpl implements WeixinUpDao{

    @Resource(name = "articleServiceImpl")
    ArticleService articleService;


    public String ArticleUpLoad(Long[] ids){

        //测试号
        String appID="wxc6da7835649d7db6";
        String appsecret="e3a9dcc14c13e0cab4e462a31bf57e98";

        //1.获取TOKEN
        AccessToken accessToken= WeixinApi.getAccessToken(appID,appsecret);
        UpArticle upArticle=new UpArticle();
        List<Details> detailses=new ArrayList<>();
        //2.查找多篇文章 并对多篇文章进行群发前的上传处理
        for(int i=0;i<ids.length;i++){
            Article article=articleService.find(ids[i]);
            ArticleModel m = new ArticleModel();
            m.bind(article);
            if(article==null||m==null){
                return null;
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
                    map.put(key, value);
                }
                contents.add(map);
            }


            //5.调用freemark模板自动生成相应的图文内容 并更新现有的图文内容
            String s=ArticlePropa.build("H:\\mopian\\mp-java\\WebContent\\WEB-INF\\template\\common","article.ftl",contents);

            System.out.println(s);

            //6.添加图文素材上传信息
            details.setContent_source_url("");
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
}
