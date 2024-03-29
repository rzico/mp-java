package net.wit.service.impl;

import net.sf.json.JSONObject;
import net.wit.dao.WeixinUpDao;
import net.wit.service.WeixinUpService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Eric on 2018/1/15.
 */
@Service("weixinUpServiceImpl")
public class WeixinUpServiceImpl implements WeixinUpService {

    @Resource(name = "weixinUpDaoImpl")
    private WeixinUpDao weixinUpDao;

    @Override
    @Transactional
    public String ArticleUpLoad(Long[] ids,String appID,String appsecret,String templatepath){
        return weixinUpDao.ArticleUpLoad(ids,appID,appsecret,templatepath);
    }

    @Override
    public JSONObject DownArticle(String url) throws IOException {
        return weixinUpDao.DownArticle(url);
    }
}
