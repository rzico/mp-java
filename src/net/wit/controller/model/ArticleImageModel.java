package net.wit.controller.model;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.wit.entity.Article;
import net.wit.entity.ArticleFavorite;
import net.wit.entity.Dragon;
import net.wit.entity.Product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

//文章列表图

public class ArticleImageModel extends BaseModel implements Serializable {
    
    private Long id;
    /** 标题图 */
    private String thumbnail;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void bind(Article article) {
        this.id = article.getId();
        this.thumbnail = article.getThumbnail();
    }

    public static List<ArticleImageModel> bindList(List<Article> articles) {
        List<ArticleImageModel> ms = new ArrayList<ArticleImageModel>();
        for (Article article:articles) {
            if (!article.getMediaType().equals(Article.ArticleType.html)) {
                String content = article.getContent();
                JSONArray ja = JSONArray.fromObject(content);
                int w = 0;
                for (int i=0;i<ja.size();i++) {
                    JSONObject jo = ja.getJSONObject(i);
                    if (jo.getString("mediaType").equals("image") && !"".equals(jo.getString("original"))) {
                        ArticleImageModel m = new ArticleImageModel();
                        m.bind(article);
                        m.setThumbnail(jo.getString("original"));
                        ms.add(m);
                        w = w + 1 ;
                    }
                    if (w==3) {
                        break;
                    }
                }
            }
        }
        return ms;
    }

}