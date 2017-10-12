package net.wit.controller.weex.model;
import net.wit.entity.Article;
import net.wit.entity.ArticleOptions;
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
    private String thumbnail;
    /** 背景音乐 */
    private MusicModel music;
    /** 文章选项 */
    private ArticleOptionModel articleOption;
    /** 内容 */
    private List<ArticleContentModel> templates = new ArrayList<ArticleContentModel>();
    /** 投票 */
    private List<ArticleVoteOptionModel> votes = new ArrayList<ArticleVoteOptionModel>();
    /** 商品 */
    private List<ProductViewModel> products = new ArrayList<ProductViewModel>();

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

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public MusicModel getMusic() {
        return music;
    }

    public void setMusic(MusicModel music) {
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

    public List<ArticleVoteOptionModel> getVotes() {
        return votes;
    }

    public void setVotes(List<ArticleVoteOptionModel> votes) {
        this.votes = votes;
    }

    public List<ProductViewModel> getProducts() {
        return products;
    }

    public void setProducts(List<ProductViewModel> products) {
        this.products = products;
    }

    public ArticleOptionModel getArticleOption() {
        return articleOption;
    }

    public void setArticleOption(ArticleOptionModel articleOption) {
        this.articleOption = articleOption;
    }

    public void bind(Article article) {

        this.id = article.getId();
        this.title = article.getTitle();
        if (article.getMusic()!=null) {
            this.music = JsonUtils.toObject(article.getMusic(), MusicModel.class);
        } else {
            this.music = new MusicModel();
        }
        this.thumbnail = article.getThumbnail();
        ArticleTitleModel articleTitleModel = new ArticleTitleModel();
        if (article.getArticleTitle()!=null) {
            articleTitleModel.bind(article.getArticleTitle());
        }
        this.articleTitleModel = articleTitleModel;

        ArticleOptionModel articleOption = new ArticleOptionModel();
        if (article.getArticleOptions()!=null) {
            articleOption.bind(article);
        }
        this.articleOption = articleOption;

        List<ArticleContentModel> templates = new ArrayList<ArticleContentModel>();
        if (article.getContent()!=null) {
            templates = JsonUtils.toObject(article.getContent(), List.class);
        }
        this.templates = templates;

        List<ArticleVoteOptionModel> votes = new ArrayList<ArticleVoteOptionModel>();
        if (article.getVotes()!=null) {
            votes = JsonUtils.toObject(article.getVotes(), List.class);
        }
        this.votes = votes;
        this.products = ProductViewModel.bindSet(article);

    }


    public static List<ArticleModel> bindList(List<Article> articles) {
        List<ArticleModel> ms = new ArrayList<ArticleModel>();
        for (Article article:articles) {
            ArticleModel m = new ArticleModel();
            m.bind(article);
            ms.add(m);
        }
        return ms;
    }

}