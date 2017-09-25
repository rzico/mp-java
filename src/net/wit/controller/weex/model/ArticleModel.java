package net.wit.controller.weex.model;
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
    private String thumbnail;
    /** 背景音乐 */
    private String music;
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

    public void bind(Article article) {

        this.id = article.getId();
        this.title = article.getTitle();
        this.music = article.getMusic();
        this.thumbnail = article.getThumbnail();
        ArticleTitleModel articleTitleModel = new ArticleTitleModel();
        if (article.getArticleTitle()!=null) {
            articleTitleModel.bind(article.getArticleTitle());
        }
        this.articleTitleModel = articleTitleModel;

        List<ArticleContentModel> templates = new ArrayList<ArticleContentModel>();
        if (article.getContent()!=null) {
            templates = JsonUtils.toObject(article.getContent(), List.class);
        } else {
            ArticleContentModel m = new ArticleContentModel();
            m.setMediaType(Article.MediaType.image);
            m.setContent("demo");
            m.setOriginal("https://qiniu.easyapi.com/g1.png!icon.jpg");
            m.setThumbnial("https://qiniu.easyapi.com/g1.png!icon.jpg");
            templates.add(m);
        }
        this.templates = templates;

        List<ArticleVoteOptionModel> votes = new ArrayList<ArticleVoteOptionModel>();
        if (article.getVotes()!=null) {
            votes = JsonUtils.toObject(article.getVotes(), List.class);
        } else {
            ArticleVoteOptionModel o = new ArticleVoteOptionModel();
            o.setTitle("测试");
            List<String> os = new ArrayList<String>();
            os.add("答案1");
            os.add("答案2");
            o.setOptions(os);
            votes.add(o);
        }
        this.votes = votes;
        this.products = ProductViewModel.bindSet(article);

    }

}