package net.wit.controller.model;

import net.wit.entity.Article;

import java.io.Serializable;

//文章展示输出模板 H5等

public class ArticleOptionModel extends BaseModel implements Serializable {

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
    private Article.Authority authority;
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

    public Boolean getIsPublish() {
        return isPublish;
    }

    public void setIsPublish(Boolean publish) {
        isPublish = publish;
    }

    public Boolean getIsPitch() {
        return isPitch;
    }

    public void setIsPitch(Boolean pitch) {
        isPitch = pitch;
    }

    public Boolean getIsReview() {
        return isReview;
    }

    public void setIsReview(Boolean review) {
        isReview = review;
    }

    public Boolean getIsReward() {
        return isReward;
    }

    public void setIsReward(Boolean reward) {
        isReward = reward;
    }

    public Article.Authority getAuthority() {
        return authority;
    }

    public void setAuthority(Article.Authority authority) {
        this.authority = authority;
    }

    public Boolean getIsExample() {
        return isExample;
    }

    public void setIsExample(Boolean example) {
        isExample = example;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getIsTop() {
        return isTop;
    }

    public void setIsTop(Boolean top) {
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
        this.authority = article.getAuthority();
        this.isPublish = article.getIsPublish();
        this.isPitch = article.getIsPitch();
        this.isReview = article.getIsReview();
        this.isReward = article.getIsReward();
        this.isExample = article.getIsExample();
        this.isTop = article.getIsTop();
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
        }
        this.articleCategory = articleCategoryModel;
   }

}