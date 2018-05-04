package net.wit.controller.model;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.wit.entity.Article;
import net.wit.entity.Member;
import net.wit.util.JsonUtils;

import java.io.Serializable;
import java.util.*;

//文章展示输出模板 H5等

public class ArticleViewModel extends BaseModel implements Serializable {
    
    private Long id;
    /** 会员 */
    private MemberViewModel member;
    /** 分享 */
    private String shareNickName;
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
    /** 是否评论 */
    private Boolean isReview;
    /** 是否赞赏 */
    private Boolean isReward;
    /** 是否发布 */
    private Boolean isPublish;
    /** 内容 */
    private List<ArticleContentViewModel> templates = new ArrayList<ArticleContentViewModel>();
    /** 投票 */
    private List<ArticleVoteOptionModel> votes = new ArrayList<ArticleVoteOptionModel>();

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

    public List<ArticleContentViewModel> getTemplates() {
        return templates;
    }

    public void setTemplates(List<ArticleContentViewModel> templates) {
        this.templates = templates;
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

    public String getHtmlTag() {
        return htmlTag;
    }

    public void setHtmlTag(String htmlTag) {
        this.htmlTag = htmlTag;
    }

    public String getShareNickName() {
        return shareNickName;
    }

    public void setShareNickName(String shareNickName) {
        this.shareNickName = shareNickName;
    }

    public Boolean getIsReview() {
        return isReview;
    }

    public void setIsReview(Boolean isReview) {
        this.isReview = isReview;
    }

    public Boolean getIsReward() {
        return isReward;
    }

    public void setIsReward(Boolean isReward) {
        this.isReward = isReward;
    }

    public void bind(Article article, Member shareUser) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.author = article.getAuthor();
        this.music = article.getMusic();
        this.thumbnail = article.getThumbnail();
        this.createDate = article.getCreateDate();
        this.isReview = article.getIsReview();
        this.isReward = article.getIsReward();
        this.hits = article.getHits();
        this.laud = article.getLaud();
        this.review = article.getReview();
        this.isPublish = article.getIsPublish();
        MemberViewModel member = new MemberViewModel();
        member.bind(article.getMember());
        this.member = member;
        List<ArticleContentViewModel> templates = new ArrayList<ArticleContentViewModel>();
        JSONArray jo = JSONArray.fromObject(article.getContent());

        for (int i=0;i<jo.size();i++) {
            JSONObject ob = jo.getJSONObject(i);
            ArticleContentViewModel m = new ArticleContentViewModel();
            m.setContent(ob.getString("content"));
            if (ob.containsKey("id") && !"".equals(ob.getString("id"))) {
                m.setId(ob.getLong("id"));
            } else {
                m.setId(0L);
            }
            m.setMediaType(Article.MediaType.valueOf(ob.getString("mediaType")) );
            m.setThumbnail(ob.getString("thumbnail"));
            m.setOriginal(ob.getString("original"));
            if (ob.containsKey("url")) {
                m.setUrl(ob.getString("url"));
            } else {
                m.setUrl("");
            }
            if (m.getMediaType().equals(Article.MediaType.video)) {
                templates.add(0,m);
            } else {
                templates.add(m);
            }
        }

        List<ArticleVoteOptionModel> votes = new ArrayList<ArticleVoteOptionModel>();
        if (article.getVotes()!=null) {
            votes = JsonUtils.toObject(article.getVotes(), List.class);
        }
        this.htmlTag = article.delHTMLTag();

        this.templates = templates;
        this.votes = votes;

        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        if (shareUser==null) {
            shareUser = article.getMember();
        }
        this.shareNickName = shareUser.displayName();

        if (article.getTemplate()==null) {
            this.url = "http://" + bundle.getString("weixin.url") + "/#/t1001?id=" + article.getId()+"&xuid="+shareUser.getId().toString();
        } else {
            this.url = "http://" + bundle.getString("weixin.url") + "/#/t" + article.getTemplate().getSn() + "?id=" + article.getId()+"&xuid="+shareUser.getId().toString();
        }

    }

}