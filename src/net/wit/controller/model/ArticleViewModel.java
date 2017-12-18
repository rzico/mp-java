package net.wit.controller.model;
import net.wit.entity.Article;
import net.wit.util.JsonUtils;

import java.io.Serializable;
import java.util.*;

//文章展示输出模板 H5等

public class ArticleViewModel extends BaseModel implements Serializable {
    
    private Long id;
    /** 会员 */
    private MemberViewModel member;
    /** 作者 */
    private String author;
    /** 标题 */
    private String title;
    /** 标题图 */
    private String thumbnail;
    /** 简说明 */
    private String htmlTag;
    /** 创建时间 */
    private Date createDate;
    /** 背景音乐 */
    private String music;
    /** 链接 */
    private String url;
    /** 评论数 */
    private Long review;
    /** 阅读数 */
    private Long hits;
    /** 点赞数 */
    private Long laud;
    /** 是否发布 */
    private Boolean isPublish;
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

    public MemberViewModel getMember() {
        return member;
    }

    public void setMember(MemberViewModel member) {
        this.member = member;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getMusic() {
        return music;
    }

    public void setMusic(String music) {
        this.music = music;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getReview() {
        return review;
    }

    public void setReview(Long review) {
        this.review = review;
    }

    public Long getHits() {
        return hits;
    }

    public void setHits(Long hits) {
        this.hits = hits;
    }

    public Long getLaud() {
        return laud;
    }

    public void setLaud(Long laud) {
        this.laud = laud;
    }

    public List<ArticleContentModel> getTemplates() {
        return templates;
    }

    public void setTemplates(List<ArticleContentModel> templates) {
        this.templates = templates;
    }

    public List<ProductViewModel> getProducts() {
        return products;
    }

    public void setProducts(List<ProductViewModel> products) {
        this.products = products;
    }

    public List<ArticleVoteOptionModel> getVotes() {
        return votes;
    }

    public void setVotes(List<ArticleVoteOptionModel> votes) {
        this.votes = votes;
    }

    public Boolean getIsPublish() {
        return isPublish;
    }

    public void setIsPublish(Boolean publish) {
        isPublish = publish;
    }

    public void bind(Article article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.author = article.getAuthor();
        this.music = article.getMusic();
        this.thumbnail = article.getThumbnail();
        this.createDate = article.getCreateDate();
        this.hits = article.getHits();
        this.laud = article.getLaud();
        this.review = article.getReview();
        this.isPublish = article.getArticleOptions().getIsPublish();
        MemberViewModel member = new MemberViewModel();
        member.bind(article.getMember());
        this.member = member;
        List<ArticleContentModel> templates = new ArrayList<ArticleContentModel>();
        if (article.getContent()!=null) {
            templates = JsonUtils.toObject(article.getContent(), List.class);
        }

        List<ArticleVoteOptionModel> votes = new ArrayList<ArticleVoteOptionModel>();
        if (article.getVotes()!=null) {
            votes = JsonUtils.toObject(article.getVotes(), List.class);
        }
        this.htmlTag = article.delHTMLTag();

        this.templates = templates;
        this.votes = votes;
        this.products = ProductViewModel.bindSet(article);
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        this.url = "http://"+bundle.getString("weixin.url")+"/t"+article.getTemplate().getSn()+"?id="+article.getId();
    }

}