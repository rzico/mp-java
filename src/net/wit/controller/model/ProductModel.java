package net.wit.controller.model;

import net.wit.entity.Product;
import net.wit.entity.ProductStock;
import net.wit.entity.Shop;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductModel extends BaseModel implements Serializable {

    private Long productId;

    /** 缩例图 */
    private String thumbnail;

    /** 规格1 */
    private String spec1;

    /** 规格2 */
    private String spec2;

//    /** 重量 */
//    private Integer weight;
    /** 销售价 */
    private BigDecimal price;
    /** 市场价 */
    private BigDecimal marketPrice;
//    /** vip1 */
//    private BigDecimal vip1Price;
//    /** vip2 */
//    private BigDecimal vip2Price;
//    /** vip3 */
//    private BigDecimal vip3Price;
//    /** 成本价 */
//    private BigDecimal cost;
    /** 库存 */
    private Integer stock;
    /** 可用库存 */
    private Integer availableStock;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getSpec1() {
        return spec1;
    }

    public void setSpec1(String spec1) {
        this.spec1 = spec1;
    }

    public String getSpec2() {
        return spec2;
    }

    public void setSpec2(String spec2) {
        this.spec2 = spec2;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void bind(Product product) {
        this.productId = product.getId();
        this.thumbnail = product.getThumbnail();
        this.spec1 = product.getSpec1();
        this.spec2 = product.getSpec2();
        this.price = product.getPrice();
        this.marketPrice = product.getMarketPrice();
        this.stock = product.getStock();
        this.availableStock = product.getAvailableStock();
//        this.weight = product.getWeight();
//        this.vip1Price = product.getVip1Price();
//        this.vip2Price = product.getVip2Price();
//        this.vip3Price = product.getVip3Price();
//        this.cost = product.getCost();
//        List<ProductStock> stocks = product.getProductStocks();
//        this.stock = 0;
//        for (ProductStock productStock:stocks) {
//            if (productStock.getSeller().equals(product.getMember())) {
//                this.stock = productStock.getStock();
//            }
//        }
    }

    public static List<ProductModel> bindList(List<Product> products) {
        List<ProductModel> ms = new ArrayList<ProductModel>();
        for (Product product:products) {
            ProductModel m = new ProductModel();
            m.bind(product);
            ms.add(m);
        }
        return ms;
    }
}