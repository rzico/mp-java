package net.wit.controller.model;

import net.wit.entity.Goods;
import net.wit.entity.Product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class GoodsViewModel extends BaseModel implements Serializable {

    private Long id;
    /** 会员 */
    private MemberViewModel member;

    /** 名称 */
    private String name;
    /** 单位 */
    private String unit;

    /** 库存 */
    private Integer stock;
    /** 好评 */
    private Long review;
    /** 人气 */
    private Long hits;
    /** 文章 id */
    private Long articleId;
    /** 可用库存 */
    private Integer availableStock;

    /** 可用库存 */
    private Boolean hasFavorite;

    /** 分类 */
    private ProductCategoryModel productCategory;

    /** 分销策略 */
    private DistributionViewModel distribution;

    /** 商品 */
    private List<PromotionListModel> promotions;

    /** 商品 */
    private List<ProductModel> products;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Long getHits() {
        return hits;
    }

    public void setHits(Long hits) {
        this.hits = hits;
    }

    public ProductCategoryModel getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategoryModel productCategory) {
        this.productCategory = productCategory;
    }

    public List<ProductModel> getProducts() {
        return products;
    }

    public void setProducts(List<ProductModel> products) {
        this.products = products;
    }

    public DistributionViewModel getDistribution() {
        return distribution;
    }

    public void setDistribution(DistributionViewModel distribution) {
        this.distribution = distribution;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Long getReview() {
        return review;
    }

    public void setReview(Long review) {
        this.review = review;
    }

    public Integer getAvailableStock() {
        return availableStock;
    }

    public void setAvailableStock(Integer availableStock) {
        this.availableStock = availableStock;
    }

    public List<PromotionListModel> getPromotions() {
        return promotions;
    }

    public void setPromotions(List<PromotionListModel> promotions) {
        this.promotions = promotions;
    }

    public MemberViewModel getMember() {
        return member;
    }

    public void setMember(MemberViewModel member) {
        this.member = member;
    }

    public Boolean getHasFavorite() {
        return hasFavorite;
    }

    public void setHasFavorite(Boolean hasFavorite) {
        this.hasFavorite = hasFavorite;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public void bind(Goods goods) {

        Product mProduct = goods.getProducts().get(0);
        this.id = goods.getId();
        this.name = mProduct.getName();

        ProductCategoryModel productCategoryModel = new ProductCategoryModel();
        if (mProduct.getProductCategory()!=null) {
            productCategoryModel.bind(mProduct.getProductCategory());
        }
        this.productCategory = productCategoryModel;

        DistributionViewModel distributionViewModel = new DistributionViewModel();
        if (mProduct.getDistribution()!=null) {
            distributionViewModel.bind(mProduct.getDistribution());
        }
        this.distribution = distributionViewModel;

        this.unit = mProduct.getUnit();
        this.products = new ArrayList<ProductModel>();
        for (Product product:goods.getProducts()) {
            ProductModel model = new ProductModel();
            model.bind(product);
            this.products.add(model);
        }

        this.stock = mProduct.getStock();
        this.availableStock = mProduct.getAvailableStock();
        this.review = goods.getReview();

        this.promotions = PromotionListModel.bindList(goods.getPromotions());

        MemberViewModel member = new MemberViewModel();
        member.bind(mProduct.getMember());
        this.member = member;

        this.hasFavorite = true;

        this.articleId = 0L;
    }
}