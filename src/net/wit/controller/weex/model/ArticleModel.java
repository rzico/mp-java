package net.wit.controller.weex.model;
import net.wit.entity.Area;
import net.wit.entity.Article;
import net.wit.util.JsonUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//文章展示输出模板 H5等
public class ArticleModel implements Serializable {

    private Long id;
    /** 标题 */
    private String title;
    /** 标题模版 */
    private ArticleTitleModel articleTitleModel;
    /** 标题图 */
    private String thumbnial;
    /** 背景音乐 */
    private String music;
    /** 内容 */
    private List<ArticleContentModel> templates = new ArrayList<ArticleContentModel>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnial() {
        return thumbnial;
    }

    public void setThumbnial(String thumbnial) {
        this.thumbnial = thumbnial;
    }

    public String getMusic() {
        return music;
    }

    public void setMusic(String music) {
        this.music = music;
    }

    public List<ArticleContentModel> getTemplates() {
        return templates;
    }

    public void setTemplates(List<ArticleContentModel> templates) {
        this.templates = templates;
    }

    public ArticleTitleModel getArticleTitleModel() {
        return articleTitleModel;
    }

    public void setArticleTitleModel(ArticleTitleModel articleTitleModel) {
        this.articleTitleModel = articleTitleModel;
    }

    public void bind(Article article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.music = article.getMusic();
        this.thumbnial = article.getThumbnial();
        ArticleTitleModel articleTitleModel = new ArticleTitleModel();
        articleTitleModel.bind(article.getArticleTitle());
        this.articleTitleModel = articleTitleModel;
        ArticleContentModel articleContentModel = new ArticleContentModel();
        List<ArticleContentModel> templates = new ArrayList<ArticleContentModel>();
        if (article.getContent()!=null) {
            templates = JsonUtils.toObject(article.getContent(), List.class);
        }
        this.templates = templates;
    }

}