package net.wit.controller.weex.model;

import net.wit.entity.Article;
import net.wit.entity.ArticleOptions;
import net.wit.entity.ArticleTitle;

import java.io.Serializable;

//文章展示输出模板 H5等

public class ArticleOptionModel implements Serializable {

    private Long id;
    /** 是否投稿 */
    private Boolean isPublish;
    /** 是否精选 */
    private Boolean isPitch;
    /** 是否评论 */
    private Boolean isReview;
    /** 是否赞赏 */
    private Boolean isReward;
    /** 谁可见 */
    private ArticleOptions.Authority authority;
    /** 是否样例 */
    private Boolean isExample;
    /** 密码 */
    private String  password;
    /** 是否置顶 */
    private Boolean isTop;

    /** 文章文集 */
    private ArticleCatalogModel articleCatalog;
    /** 文章分类 */
    private ArticleCategoryModel articleCategory;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getPublish() {
        return isPublish;
    }

    public void setPublish(Boolean publish) {
        isPublish = publish;
    }

    public Boolean getPitch() {
        return isPitch;
    }

    public void setPitch(Boolean pitch) {
        isPitch = pitch;
    }

    public Boolean getReview() {
        return isReview;
    }

    public void setReview(Boolean review) {
        isReview = review;
    }

    public Boolean getReward() {
        return isReward;
    }

    public void setReward(Boolean reward) {
        isReward = reward;
    }

    public ArticleOptions.Authority getAuthority() {
        return authority;
    }

    public void setAuthority(ArticleOptions.Authority authority) {
        this.authority = authority;
    }

    public Boolean getExample() {
        return isExample;
    }

    public void setExample(Boolean example) {
        isExample = example;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getTop() {
        return isTop;
    }

    public void setTop(Boolean top) {
        isTop = top;
    }

    public ArticleCatalogModel getArticleCatalog() {
        return articleCatalog;
    }

    public void setArticleCatalog(ArticleCatalogModel articleCatalog) {
        this.articleCatalog = articleCatalog;
    }

    public ArticleCategoryModel getArticleCategory() {
        return articleCategory;
    }

    public void setArticleCategory(ArticleCategoryModel articleCategory) {
        this.articleCategory = articleCategory;
    }

    public void bind(Article article) {
        ArticleOptions options = article.getArticleOptions();
        if (options!=null) {
            this.authority = options.getAuthority();
            this.isPublish = options.getIsPublish();
            this.isPitch = options.getIsPitch();
            this.isReview = options.getIsReview();
            this.isReward = options.getIsReward();
            this.isExample = options.getIsExample();
            this.isTop = options.getIsTop();
        }
        ArticleCatalogModel articleCatalogModel = new ArticleCatalogModel();
        if (article.getArticleCatalog()!=null) {
            articleCatalogModel.setId(article.getArticleCatalog().getId());
            articleCatalogModel.setName(article.getArticleCatalog().getName());
            articleCatalogModel.setCount(0);
        }
        this.articleCatalog = articleCatalogModel;
        ArticleCategoryModel articleCategoryModel = new ArticleCategoryModel();
        if (article.getArticleCategory()!=null) {
            articleCategoryModel.setId(article.getArticleCategory().getId());
            articleCategoryModel.setName(article.getArticleCategory().getName());
            articleCatalogModel.setCount(0);
        }
        this.articleCatalog = articleCatalogModel;
   }

}