package net.wit.controller.admin;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;

import javax.annotation.Resource;
import net.wit.Filter;
import net.wit.Message;
import net.wit.Pageable;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Filters;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import net.wit.entity.BaseEntity.Save;
import net.wit.entity.Product;
import net.wit.service.ProductService;

import java.util.*;

import net.wit.*;

import net.wit.entity.*;
import net.wit.service.*;
import net.wit.controller.admin.model.*;



/**
 * @ClassName: ProductController
 * @author 降魔战队
 * @date 2017-10-11 15:37:12
 */
 
@Controller("adminProductController")
@RequestMapping("/admin/product")
public class ProductController extends BaseController {
	@Resource(name = "productServiceImpl")
	private ProductService productService;
	
	@Resource(name = "goodsServiceImpl")
	private GoodsService goodsService;

	@Resource(name = "productCategoryServiceImpl")
	private ProductCategoryService productCategoryService;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;



	/**
	 * 主页
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model) {

		model.addAttribute("goodss",goodsService.findAll());

		model.addAttribute("productCategorys",productCategoryService.findAll());

		return "/admin/product/list";
	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {

		model.addAttribute("goodss",goodsService.findAll());

		model.addAttribute("productCategorys",productCategoryService.findAll());

		return "/admin/product/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(Product product, Long goodsId, Long productCategoryId){
		Product entity = new Product();	

		entity.setCreateDate(product.getCreateDate());

		entity.setModifyDate(product.getModifyDate());

		entity.setCost(product.getCost());

		entity.setDeleted(product.getDeleted());

		entity.setIsList(product.getIsList());

		entity.setIsMarketable(product.getIsMarketable());

		entity.setMarketPrice(product.getMarketPrice());

		entity.setName(product.getName());

		entity.setPoint(product.getPoint() == null ? 0 : product.getPoint());

		entity.setPrice(product.getPrice());

		entity.setSn(product.getSn());

		entity.setSpec(product.getSpec());

		entity.setUnit(product.getUnit());

		entity.setVip1Price(product.getVip1Price());

		entity.setVip2Price(product.getVip2Price());

		entity.setVip3Price(product.getVip3Price());

		entity.setWeight(product.getWeight() == null ? 0 : product.getWeight());

		entity.setGoods(goodsService.find(goodsId));

		entity.setProductCategory(productCategoryService.find(productCategoryId));

		entity.setThumbnial(product.getThumbnial());
		
		if (!isValid(entity, Save.class)) {
            return Message.error("admin.data.valid");
        }
        try {
            productService.save(entity);
            return Message.success(entity,"admin.save.success");
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error("admin.save.error");
        }
	}


	/**
     * 删除
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public @ResponseBody
    Message delete(Long[] ids) {
        try {
            productService.delete(ids);
            return Message.success("admin.delete.success");
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error("admin.delete.error");
        }
    }
	
	
	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {

		model.addAttribute("goodss",goodsService.findAll());

		model.addAttribute("productCategorys",productCategoryService.findAll());

		model.addAttribute("data",productService.find(id));

		return "/admin/product/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(Product product, Long goodsId, Long productCategoryId){
		Product entity = productService.find(product.getId());
		
		entity.setCreateDate(product.getCreateDate());

		entity.setModifyDate(product.getModifyDate());

		entity.setCost(product.getCost());

		entity.setDeleted(product.getDeleted());

		entity.setIsList(product.getIsList());

		entity.setIsMarketable(product.getIsMarketable());

		entity.setMarketPrice(product.getMarketPrice());

		entity.setName(product.getName());

		entity.setPoint(product.getPoint() == null ? 0 : product.getPoint());

		entity.setPrice(product.getPrice());

		entity.setSn(product.getSn());

		entity.setSpec(product.getSpec());

		entity.setUnit(product.getUnit());

		entity.setVip1Price(product.getVip1Price());

		entity.setVip2Price(product.getVip2Price());

		entity.setVip3Price(product.getVip3Price());

		entity.setWeight(product.getWeight() == null ? 0 : product.getWeight());

		entity.setGoods(goodsService.find(goodsId));

		entity.setProductCategory(productCategoryService.find(productCategoryId));

		entity.setThumbnial(product.getThumbnial());
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            productService.update(entity);
            return Message.success(entity,"admin.update.success");
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error("admin.update.error");
        }
	}
	

	/**
     * 列表
     */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public Message list(Date beginDate, Date endDate, Pageable pageable, ModelMap model) {	

		Page<Product> page = productService.findPage(beginDate,endDate,pageable);
		return Message.success(PageBlock.bind(page), "admin.list.success");
	}
	
	
	/**
	 * 商品管理视图
	 */
	@RequestMapping(value = "/goodsView", method = RequestMethod.GET)
	public String goodsView(Long id, ModelMap model) {


		model.addAttribute("goods",goodsService.find(id));
		return "/admin/product/view/goodsView";
	}


	/**
	 * 商品分类视图
	 */
	@RequestMapping(value = "/productCategoryView", method = RequestMethod.GET)
	public String productCategoryView(Long id, ModelMap model) {
		model.addAttribute("members",memberService.findAll());

		model.addAttribute("productCategory",productCategoryService.find(id));
		return "/admin/product/view/productCategoryView";
	}



}