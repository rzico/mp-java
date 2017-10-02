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
import net.wit.entity.ShippingItem;
import net.wit.service.ShippingItemService;

import java.util.*;

import net.wit.*;

import net.wit.entity.*;
import net.wit.service.*;
import net.wit.controller.admin.model.*;



/**
 * @ClassName: ShippingItemController
 * @author 降魔战队
 * @date 2017-9-14 19:42:17
 */
 
@Controller("adminShippingItemController")
@RequestMapping("/admin/shippingItem")
public class ShippingItemController extends BaseController {
	@Resource(name = "shippingItemServiceImpl")
	private ShippingItemService shippingItemService;
	
	@Resource(name = "productStockServiceImpl")
	private ProductStockService productStockService;

	@Resource(name = "productServiceImpl")
	private ProductService productService;

	@Resource(name = "shippingServiceImpl")
	private ShippingService shippingService;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "goodsServiceImpl")
	private GoodsService goodsService;

	@Resource(name = "productCategoryServiceImpl")
	private ProductCategoryService productCategoryService;

	@Resource(name = "orderServiceImpl")
	private OrderService orderService;



	/**
	 * 主页
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model) {

		model.addAttribute("products",productService.findAll());

		model.addAttribute("productStocks",productStockService.findAll());

		model.addAttribute("shippings",shippingService.findAll());

		return "/admin/shippingItem/list";
	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {

		model.addAttribute("products",productService.findAll());

		model.addAttribute("productStocks",productStockService.findAll());

		model.addAttribute("shippings",shippingService.findAll());

		return "/admin/shippingItem/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(ShippingItem shippingItem, Long productStockId, Long productId, Long shippingId){
		ShippingItem entity = new ShippingItem();	

		entity.setCreateDate(shippingItem.getCreateDate());

		entity.setModifyDate(shippingItem.getModifyDate());

		entity.setName(shippingItem.getName());

		entity.setQuantity(shippingItem.getQuantity() == null ? 0 : shippingItem.getQuantity());

		entity.setSn(shippingItem.getSn());

		entity.setProduct(productService.find(productId));

		entity.setProductStock(productStockService.find(productStockId));

		entity.setShipping(shippingService.find(shippingId));
		
		if (!isValid(entity, Save.class)) {
            return Message.error("admin.data.valid");
        }
        try {
            shippingItemService.save(entity);
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
            shippingItemService.delete(ids);
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

		model.addAttribute("productStocks",productStockService.findAll());

		model.addAttribute("shippings",shippingService.findAll());

		model.addAttribute("data",shippingItemService.find(id));

		return "/admin/shippingItem/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(ShippingItem shippingItem, Long productStockId, Long productId, Long shippingId){
		ShippingItem entity = shippingItemService.find(shippingItem.getId());
		
		entity.setCreateDate(shippingItem.getCreateDate());

		entity.setModifyDate(shippingItem.getModifyDate());

		entity.setName(shippingItem.getName());

		entity.setQuantity(shippingItem.getQuantity() == null ? 0 : shippingItem.getQuantity());

		entity.setSn(shippingItem.getSn());

		entity.setProduct(productService.find(productId));

		entity.setProductStock(productStockService.find(productStockId));

		entity.setShipping(shippingService.find(shippingId));
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            shippingItemService.update(entity);
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

		Page<ShippingItem> page = shippingItemService.findPage(beginDate,endDate,pageable);
		return Message.success(PageBlock.bind(page), "admin.list.success");
	}
	
	
	/**
	 * 商品库存视图
	 */
	@RequestMapping(value = "/productStockView", method = RequestMethod.GET)
	public String productStockView(Long id, ModelMap model) {
		model.addAttribute("products",productService.findAll());

		model.addAttribute("sellers",memberService.findAll());

		model.addAttribute("productStock",productStockService.find(id));
		return "/admin/shippingItem/view/productStockView";
	}


	/**
	 * 产品档案视图
	 */
	@RequestMapping(value = "/productView", method = RequestMethod.GET)
	public String productView(Long id, ModelMap model) {
		model.addAttribute("goodss",goodsService.findAll());

		model.addAttribute("productCategorys",productCategoryService.findAll());

		model.addAttribute("product",productService.find(id));
		return "/admin/shippingItem/view/productView";
	}


	/**
	 * 发货单视图
	 */
	@RequestMapping(value = "/shippingView", method = RequestMethod.GET)
	public String shippingView(Long id, ModelMap model) {
		model.addAttribute("members",memberService.findAll());

		model.addAttribute("orderss",orderService.findAll());

		model.addAttribute("sellers",memberService.findAll());

		model.addAttribute("shipping",shippingService.find(id));
		return "/admin/shippingItem/view/shippingView";
	}



}