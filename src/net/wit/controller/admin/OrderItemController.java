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
import net.wit.entity.OrderItem;
import net.wit.service.OrderItemService;

import java.util.*;

import net.wit.*;

import net.wit.entity.*;
import net.wit.service.*;
import net.wit.controller.admin.model.*;



/**
 * @ClassName: OrderItemController
 * @author 降魔战队
 * @date 2017-9-14 19:42:15
 */
 
@Controller("adminOrderItemController")
@RequestMapping("/admin/orderItem")
public class OrderItemController extends BaseController {
	@Resource(name = "orderItemServiceImpl")
	private OrderItemService orderItemService;
	
	@Resource(name = "productServiceImpl")
	private ProductService productService;

	@Resource(name = "orderServiceImpl")
	private OrderService orderService;

	@Resource(name = "goodsServiceImpl")
	private GoodsService goodsService;

	@Resource(name = "productCategoryServiceImpl")
	private ProductCategoryService productCategoryService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "couponCodeServiceImpl")
	private CouponCodeService couponCodeService;



	/**
	 * 主页
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model) {

		model.addAttribute("orderss",orderService.findAll());

		model.addAttribute("products",productService.findAll());

		return "/admin/orderItem/list";
	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {

		model.addAttribute("orderss",orderService.findAll());

		model.addAttribute("products",productService.findAll());

		return "/admin/orderItem/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(OrderItem orderItem, Long productId, Long ordersId){
		OrderItem entity = new OrderItem();	

		entity.setCreateDate(orderItem.getCreateDate());

		entity.setModifyDate(orderItem.getModifyDate());

		entity.setIsGift(orderItem.getIsGift());

		entity.setName(orderItem.getName());

		entity.setPrice(orderItem.getPrice());

		entity.setQuantity(orderItem.getQuantity() == null ? 0 : orderItem.getQuantity());

		entity.setReturnQuantity(orderItem.getReturnQuantity() == null ? 0 : orderItem.getReturnQuantity());

		entity.setShippedQuantity(orderItem.getShippedQuantity() == null ? 0 : orderItem.getShippedQuantity());

		entity.setSn(orderItem.getSn());

		entity.setSpec(orderItem.getSpec());

		entity.setThumbnail(orderItem.getThumbnail());

		entity.setWeight(orderItem.getWeight());

		entity.setOrder(orderService.find(ordersId));

		entity.setProduct(productService.find(productId));
		
		if (!isValid(entity, Save.class)) {
            return Message.error("admin.data.valid");
        }
        try {
            orderItemService.save(entity);
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
            orderItemService.delete(ids);
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

		model.addAttribute("orderss",orderService.findAll());

		model.addAttribute("products",productService.findAll());

		model.addAttribute("data",orderItemService.find(id));

		return "/admin/orderItem/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(OrderItem orderItem, Long productId, Long ordersId){
		OrderItem entity = orderItemService.find(orderItem.getId());
		
		entity.setCreateDate(orderItem.getCreateDate());

		entity.setModifyDate(orderItem.getModifyDate());

		entity.setIsGift(orderItem.getIsGift());

		entity.setName(orderItem.getName());

		entity.setPrice(orderItem.getPrice());

		entity.setQuantity(orderItem.getQuantity() == null ? 0 : orderItem.getQuantity());

		entity.setReturnQuantity(orderItem.getReturnQuantity() == null ? 0 : orderItem.getReturnQuantity());

		entity.setShippedQuantity(orderItem.getShippedQuantity() == null ? 0 : orderItem.getShippedQuantity());

		entity.setSn(orderItem.getSn());

		entity.setSpec(orderItem.getSpec());

		entity.setThumbnail(orderItem.getThumbnail());

		entity.setWeight(orderItem.getWeight());

		entity.setOrder(orderService.find(ordersId));

		entity.setProduct(productService.find(productId));
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            orderItemService.update(entity);
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

		Page<OrderItem> page = orderItemService.findPage(beginDate,endDate,pageable);
		return Message.success(PageBlock.bind(page), "admin.list.success");
	}
	
	
	/**
	 * 产品档案视图
	 */
	@RequestMapping(value = "/productView", method = RequestMethod.GET)
	public String productView(Long id, ModelMap model) {
		model.addAttribute("goodss",goodsService.findAll());

		model.addAttribute("productCategorys",productCategoryService.findAll());

		model.addAttribute("product",productService.find(id));
		return "/admin/orderItem/view/productView";
	}


	/**
	 * 订单管理视图
	 */
	@RequestMapping(value = "/orderView", method = RequestMethod.GET)
	public String orderView(Long id, ModelMap model) {
		model.addAttribute("areas",areaService.findAll());

		model.addAttribute("couponCodes",couponCodeService.findAll());

		model.addAttribute("members",memberService.findAll());

		model.addAttribute("sellers",memberService.findAll());

		model.addAttribute("order",orderService.find(id));
		return "/admin/orderItem/view/orderView";
	}



}