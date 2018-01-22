package net.wit.dao;

/**
 * Created by Eric on 2018/1/15.
 */
public interface WeixinUpDao {

    /**
     * 根据文章ID 群发文章 给所有关注该公众号用户
     * @param ids 文章id 若不存在 返回NULL
     * @return
     */
    String ArticleUpLoad(Long[] ids,String appID,String appsecret);
}
