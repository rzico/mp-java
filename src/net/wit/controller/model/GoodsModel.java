package net.wit.controller.model;

import net.wit.entity.Barrel;
import net.wit.entity.Goods;
import net.wit.entity.Product;
import net.wit.entity.ProductStock;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class GoodsModel extends BaseModel implements Serializable {

    private Long id;
    /** 名称 */
    private String name;
    /** 单位 */
    private String unit;

    /** 库存 */
    private Integer stock;
    /** 可用库存 */
    private Integer availableStock;

    /** 分类 */
    private ProductCategoryModel productCategory;

    /** 分销策略 */
    private DistributionViewModel distribution;

    /** 配送方式 */
    private Product.Type type;

    /** 品牌 */
    private BarrelModel barrel;

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

    public Integer getAvailableStock() {
        return availableStock;
    }

    public void setAvailableStock(Integer availableStock) {
        this.availableStock = availableStock;
    }

    public Product.Type getType() {
        return type;
    }

    public void setType(Product.Type type) {
        this.type = type;
    }

    public BarrelModel getBarrel() {
        return barrel;
    }

    public void setBarrel(BarrelModel barrel) {
        this.barrel = barrel;
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

        BarrelModel barrelModel = new BarrelModel();
        if (mProduct.getBarrel()!=null) {
            barrelModel.bind(mProduct.getBarrel());
        }
        this.barrel = barrelModel;

        this.type = mProduct.getType();

        this.unit = mProduct.getUnit();
        this.products = new ArrayList<ProductModel>();
        for (Product product:goods.getProducts()) {
            ProductModel model = new ProductModel();
            model.bind(product);
            this.products.add(model);
        }
        this.stock = mProduct.getStock();
        this.availableStock = mProduct.getAvailableStock();


    }
}