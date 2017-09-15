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
import net.wit.entity.ProductStock;
import net.wit.service.ProductStockService;

import java.util.*;

import net.wit.*;

import net.wit.entity.*;
import net.wit.service.*;
import net.wit.controller.admin.model.*;



/**
 * @ClassName: ProductStockController
 * @author 降魔战队
 * @date 2017-9-14 19:42:16
 */
 
@Controller("adminProductStockController")
@RequestMapping("/admin/productStock")
public class ProductStockController extends BaseController {
	@Resource(name = "productStockServiceImpl")
	private ProductStockService productStockService;
	
	@Resource(name = "productServiceImpl")
	private ProductService productService;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "goodsServiceImpl")
	private GoodsService goodsService;

	@Resource(name = "productCategoryServiceImpl")
	private ProductCategoryService productCategoryService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "tagServiceImpl")
	private TagService tagService;



	/**
	 * 主页
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model) {

		model.addAttribute("products",productService.findAll());

		model.addAttribute("sellers",memberService.findAll());

		return "/admin/productStock/list";
	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {

		model.addAttribute("products",productService.findAll());

		model.addAttribute("sellers",memberService.findAll());

		return "/admin/productStock/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(ProductStock productStock, Long productId, Long sellerId){
		ProductStock entity = new ProductStock();	

		entity.setCreateDate(productStock.getCreateDate());

		entity.setModifyDate(productStock.getModifyDate());

		entity.setAllocatedStock(productStock.getAllocatedStock() == null ? 0 : productStock.getAllocatedStock());

		entity.setStock(productStock.getStock() == null ? 0 : productStock.getStock());

		entity.setProduct(productService.find(productId));

		entity.setSeller(memberService.find(sellerId));
		
		if (!isValid(entity, Save.class)) {
            return Message.error("admin.data.valid");
        }
        try {
            productStockService.save(entity);
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
            productStockService.delete(ids);
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

		model.addAttribute("products",productService.findAll());

		model.addAttribute("sellers",memberService.findAll());

		model.addAttribute("data",productStockService.find(id));

		return "/admin/productStock/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(ProductStock productStock, Long productId, Long sellerId){
		ProductStock entity = productStockService.find(productStock.getId());
		
		entity.setCreateDate(productStock.getCreateDate());

		entity.setModifyDate(productStock.getModifyDate());

		entity.setAllocatedStock(productStock.getAllocatedStock() == null ? 0 : productStock.getAllocatedStock());

		entity.setStock(productStock.getStock() == null ? 0 : productStock.getStock());

		entity.setProduct(productService.find(productId));

		entity.setSeller(memberService.find(sellerId));
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            productStockService.update(entity);
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

		Page<ProductStock> page = productStockService.findPage(beginDate,endDate,pageable);
		return Message.success(PageModel.bind(page), "admin.list.success");
	}
	
	
	/**
	 * 产品档案视图
	 */
	@RequestMapping(value = "/productView", method = RequestMethod.GET)
	public String productView(Long id, ModelMap model) {
		model.addAttribute("goodss",goodsService.findAll());

		model.addAttribute("productCategorys",productCategoryService.findAll());

		model.addAttribute("product",productService.find(id));
		return "/admin/productStock/view/productView";
	}


	/**
	 * 会员管理视图
	 */
	@RequestMapping(value = "/memberView", method = RequestMethod.GET)
	public String memberView(Long id, ModelMap model) {
		List<MapEntity> genders = new ArrayList<>();
		genders.add(new MapEntity("male","男"));
		genders.add(new MapEntity("female","女"));
		genders.add(new MapEntity("secrecy","保密"));
		model.addAttribute("genders",genders);

		model.addAttribute("areas",areaService.findAll());

		model.addAttribute("tags",tagService.findAll());

		model.addAttribute("member",memberService.find(id));
		return "/admin/productStock/view/memberView";
	}



}