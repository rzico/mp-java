package net.wit.service;

import net.sf.json.JSONObject;

import java.io.IOException;

/**
 * Created by Eric on 2018/1/15.
 */
public interface WeixinUpService {


    String ArticleUpLoad(Long[] ids,String appID,String appsecret,String templatepath);

    JSONObject DownArticle(String url) throws IOException;
}
