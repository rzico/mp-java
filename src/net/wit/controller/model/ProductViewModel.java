package net.wit.controller.model;

import net.wit.entity.Article;
import net.wit.entity.ArticleProduct;
import net.wit.entity.Product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

//关联商品展示模版等
public class ProductViewModel extends BaseModel implements Serializable {

    private Long id;
    /** 商品名称 */
    private String name;
    /** 销售价 */
    private BigDecimal price;
    /** 市场价 */
    private BigDecimal marketPrice;
    /** 标题图 */
    private String thumbnail;

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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(BigDecimal marketPrice) {
        this.marketPrice = marketPrice;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void bind(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.marketPrice = product.getMarketPrice();
        this.thumbnail =product.getThumbnail();
    }

    public static List<ProductViewModel> bindSet(Article article) {
        List<ProductViewModel> ls=new ArrayList<>();

        if (article.getProducts()!=null) {
            for (ArticleProduct product : article.getProducts()) {
                ProductViewModel model = new ProductViewModel();
                model.bind(product.getProduct());
                ls.add(model);
            }
        }
        return ls;
    }

}