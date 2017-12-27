package net.wit.controller.model;

import net.wit.entity.Goods;
import net.wit.entity.Product;
import net.wit.entity.ProductStock;

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
    /** 库存 */
    private Integer stock;

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

    public void bind(Goods goods) {
        this.id = goods.getId();
        Product product = goods.getProducts().get(0);
        this.name = product.getName();
        List<ProductStock> stocks = product.getProductStocks();
        this.stock = 0;
        for (ProductStock productStock:stocks) {
            if (productStock.getSeller().equals(product.getMember())) {
                this.stock = productStock.getStock();
            }
        }
        this.setThumbnail(product.getThumbnail());
        this.setPrice(product.getPrice());
    }


    public void bindProduct(Product product) {
        this.id = product.getGoods().getId();
        this.name = product.getName();
        List<ProductStock> stocks = product.getProductStocks();
        this.stock = 0;
        for (ProductStock productStock:stocks) {
            if (productStock.getSeller().equals(product.getMember())) {
                this.stock = productStock.getStock();
            }
        }
        this.setThumbnail(product.getThumbnail());
        this.setPrice(product.getPrice());
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

}