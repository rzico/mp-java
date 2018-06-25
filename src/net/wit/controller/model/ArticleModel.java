package net.wit.controller.model;
import net.wit.entity.Article;
import net.wit.entity.Tag;
import net.wit.util.JsonUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//文章展示输出模板 H5等
public class ArticleModel extends BaseModel implements Serializable {

    private Long id;
    /** 最近变动时间 */
    private Date modifyDate;
    /** 标题 */
    private String title;
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
    /** 表单 */
    private List<ArticleFormDataModel> forms = new ArrayList<ArticleFormDataModel>();

    /** 是否草稿 */
    private Boolean isDraft;
    /** 评论数 */
    private Long review;
    /** 阅读数 */
    private Long hits;
    /** 点赞数 */
    private Long laud;

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

    public List<ArticleFormDataModel> getForms() {
        return forms;
    }

    public void setForms(List<ArticleFormDataModel> forms) {
        this.forms = forms;
    }

    public List<ArticleVoteOptionModel> getVotes() {
        return votes;
    }

    public void setVotes(List<ArticleVoteOptionModel> votes) {
        this.votes = votes;
    }

    public ArticleOptionModel getArticleOption() {
        return articleOption;
    }

    public void setArticleOption(ArticleOptionModel articleOption) {
        this.articleOption = articleOption;
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

    public Boolean getIsDraft() {
        return isDraft;
    }

    public void setIsDraft(Boolean draft) {
        isDraft = draft;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public void bind(Article article) {

        this.id = article.getId();
        this.modifyDate = article.getModifyDate();
        this.title = article.getTitle();
        if (article.getMusic()!=null) {
            this.music = JsonUtils.toObject(article.getMusic(), MusicModel.class);
        } else {
            this.music = new MusicModel();
        }
        this.thumbnail = article.getThumbnail();

        ArticleOptionModel articleOption = new ArticleOptionModel();
        articleOption.bind(article);
        this.articleOption = articleOption;
        this.isDraft = article.getIsDraft();

        List<ArticleContentModel> templates = new ArrayList<ArticleContentModel>();
//        System.out.print("articleId:" + article.getId());
        if (article.getContent()!=null) {
            templates = JsonUtils.toObject(article.getContent(), List.class);
        }
        this.templates = templates;

        List<ArticleVoteOptionModel> votes = new ArrayList<ArticleVoteOptionModel>();
        if (article.getVotes()!=null) {
            votes = JsonUtils.toObject(article.getVotes(), List.class);
        }
        this.votes = votes;
        this.hits = article.getHits();
        this.laud = article.getLaud();
        this.review = article.getReview();
        this.articleOption.setIsTop(false);
        for (Tag tag:article.getTags()) {
           if (tag.getId().equals(6L)) {
               this.articleOption.setIsTop(true);
           }
        }

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