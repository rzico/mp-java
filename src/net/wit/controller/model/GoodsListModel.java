package net.wit.controller.model;

import net.wit.entity.Goods;
import net.wit.entity.Product;
import net.wit.entity.ProductStock;
import net.wit.entity.Promotion;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class GoodsListModel extends BaseModel implements Serializable {

    private Long id;

    /** 名称 */
    private String name;
    /** 缩例图 */
    private String thumbnail;

    /** 销售价 */
    private BigDecimal price;
    /** 市场价 */
    private BigDecimal marketPrice;

    /** 库存 */
    private Integer stock;
    /** 好评 */
    private Long review;
    /** 可用库存 */
    private Integer availableStock;
    /** 标签名 */
    private List<TagModel> tags = new ArrayList<TagModel>();
    /** 商品 */
    private List<PromotionListModel> promotions;

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

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getAvailableStock() {
        return availableStock;
    }

    public void setAvailableStock(Integer availableStock) {
        this.availableStock = availableStock;
    }

    public BigDecimal getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(BigDecimal marketPrice) {
        this.marketPrice = marketPrice;
    }

    public Long getReview() {
        return review;
    }

    public void setReview(Long review) {
        this.review = review;
    }


    public List<TagModel> getTags() {
        return tags;
    }

    public void setTags(List<TagModel> tags) {
        this.tags = tags;
    }

    public List<PromotionListModel> getPromotions() {
        return promotions;
    }

    public void setPromotions(List<PromotionListModel> promotions) {
        this.promotions = promotions;
    }

    public void bind(Goods goods) {

        this.id = goods.getId();
        Product product = goods.product();
        this.name = product.getName();
        this.stock = product.getStock();
        this.availableStock = product.getAvailableStock();

//        List<ProductStock> stocks = product.getProductStocks();
//        this.stock = 0;
//        for (ProductStock productStock:stocks) {
//            if (productStock.getSeller().equals(product.getMember())) {
//                this.stock = productStock.getStock();
//            }
//        }

        this.setThumbnail(product.getThumbnail());
        this.setPrice(product.getPrice());
        this.setMarketPrice(product.getMarketPrice());
        this.review = goods.getReview();

        this.tags = TagModel.bindList(product.getTags());

        this.promotions = PromotionListModel.bindList(goods.getPromotions());

    }


    public void bindProduct(Product product) {
        this.id = product.getGoods().getId();
        this.name = product.getName();
        this.stock = product.getStock();
        this.availableStock = product.getAvailableStock();
//        List<ProductStock> stocks = product.getProductStocks();
//        this.stock = 0;
//        for (ProductStock productStock:stocks) {
//            if (productStock.getSeller().equals(product.getMember())) {
//                this.stock = productStock.getStock();
//            }
//        }
        this.setThumbnail(product.getThumbnail());
        this.setPrice(product.getPrice());
        this.setMarketPrice(product.getMarketPrice());
        this.review = product.getGoods().getReview();

        this.tags = TagModel.bindList(product.getTags());
    }


    public static List<GoodsListModel> bindList(List<Product> products) {
        List<GoodsListModel> ms = new ArrayList<GoodsListModel>();
        for (Product product:products) {
            GoodsListModel m = new GoodsListModel();
            m.bindProduct(product);
            ms.add(m);
        }
        return ms;
    }



    public static List<GoodsListModel> bindPromotion(List<Promotion> promotions) {
        List<GoodsListModel> ms = new ArrayList<GoodsListModel>();
        for (Promotion promotion:promotions) {
            GoodsListModel m = new GoodsListModel();
            m.bind(promotion.getGoods());
            ms.add(m);
        }
        return ms;
    }
}