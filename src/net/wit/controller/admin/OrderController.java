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
import net.wit.entity.Order;
import net.wit.service.OrderService;

import java.util.*;

import net.wit.*;

import net.wit.entity.*;
import net.wit.service.*;
import net.wit.controller.admin.model.*;



/**
 * @ClassName: OrderController
 * @author 降魔战队
 * @date 2017-10-11 15:37:11
 */
 
@Controller("adminOrderController")
@RequestMapping("/admin/order")
public class OrderController extends BaseController {
	@Resource(name = "orderServiceImpl")
	private OrderService orderService;
	
	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "couponCodeServiceImpl")
	private CouponCodeService couponCodeService;

	@Resource(name = "occupationServiceImpl")
	private OccupationService occupationService;

	@Resource(name = "tagServiceImpl")
	private TagService tagService;

	@Resource(name = "couponServiceImpl")
	private CouponService couponService;



	/**
	 * 主页
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model) {

		model.addAttribute("areas",areaService.findAll());

		model.addAttribute("couponCodes",couponCodeService.findAll());

		model.addAttribute("members",memberService.findAll());

		model.addAttribute("sellers",memberService.findAll());

		return "/admin/order/list";
	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {

		model.addAttribute("areas",areaService.findAll());

		model.addAttribute("couponCodes",couponCodeService.findAll());

		model.addAttribute("members",memberService.findAll());

		model.addAttribute("sellers",memberService.findAll());

		return "/admin/order/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(Order order, Long areaId, Long memberId, Long sellerId, Long couponCodeId){
		Order entity = new Order();	

		entity.setCreateDate(order.getCreateDate());

		entity.setModifyDate(order.getModifyDate());

		entity.setAddress(order.getAddress());

		entity.setAmountPaid(order.getAmountPaid());

		entity.setAreaName(order.getAreaName());

		entity.setConsignee(order.getConsignee());

		entity.setCouponDiscount(order.getCouponDiscount());

		entity.setExpire(order.getExpire());

		entity.setFee(order.getFee());

		entity.setFreight(order.getFreight());

		entity.setIsAllocatedStock(order.getIsAllocatedStock());

		entity.setLockExpire(order.getLockExpire());

		entity.setMemo(order.getMemo());

		entity.setOffsetAmount(order.getOffsetAmount());

		entity.setOperator(order.getOperator());

		entity.setOrderStatus(order.getOrderStatus() == null ? Order.OrderStatus.unconfirmed : order.getOrderStatus());

		entity.setPaymentStatus(order.getPaymentStatus() == null ? Order.PaymentStatus.unpaid : order.getPaymentStatus());

		entity.setPhone(order.getPhone());

		entity.setPoint(order.getPoint() == null ? 0 : order.getPoint());

		entity.setPointDiscount(order.getPointDiscount());

		entity.setShippingStatus(order.getShippingStatus() == null ? Order.ShippingStatus.unshipped : order.getShippingStatus());

		entity.setSn(order.getSn());

		entity.setZipCode(order.getZipCode());

		entity.setArea(areaService.find(areaId));

		entity.setCouponCode(couponCodeService.find(couponCodeId));

		entity.setMember(memberService.find(memberId));

		entity.setSeller(memberService.find(sellerId));
		
		if (!isValid(entity, Save.class)) {
            return Message.error("admin.data.valid");
        }
        try {
            orderService.save(entity);
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
            orderService.delete(ids);
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

		model.addAttribute("areas",areaService.findAll());

		model.addAttribute("couponCodes",couponCodeService.findAll());

		model.addAttribute("members",memberService.findAll());

		model.addAttribute("sellers",memberService.findAll());

		model.addAttribute("data",orderService.find(id));

		return "/admin/order/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(Order order, Long areaId, Long memberId, Long sellerId, Long couponCodeId){
		Order entity = orderService.find(order.getId());
		
		entity.setCreateDate(order.getCreateDate());

		entity.setModifyDate(order.getModifyDate());

		entity.setAddress(order.getAddress());

		entity.setAmountPaid(order.getAmountPaid());

		entity.setAreaName(order.getAreaName());

		entity.setConsignee(order.getConsignee());

		entity.setCouponDiscount(order.getCouponDiscount());

		entity.setExpire(order.getExpire());

		entity.setFee(order.getFee());

		entity.setFreight(order.getFreight());

		entity.setIsAllocatedStock(order.getIsAllocatedStock());

		entity.setLockExpire(order.getLockExpire());

		entity.setMemo(order.getMemo());

		entity.setOffsetAmount(order.getOffsetAmount());

		entity.setOperator(order.getOperator());

		entity.setOrderStatus(order.getOrderStatus() == null ? Order.OrderStatus.unconfirmed : order.getOrderStatus());

		entity.setPaymentStatus(order.getPaymentStatus() == null ? Order.PaymentStatus.unpaid : order.getPaymentStatus());

		entity.setPhone(order.getPhone());

		entity.setPoint(order.getPoint() == null ? 0 : order.getPoint());

		entity.setPointDiscount(order.getPointDiscount());

		entity.setShippingStatus(order.getShippingStatus() == null ? Order.ShippingStatus.unshipped : order.getShippingStatus());

		entity.setSn(order.getSn());

		entity.setZipCode(order.getZipCode());

		entity.setArea(areaService.find(areaId));

		entity.setCouponCode(couponCodeService.find(couponCodeId));

		entity.setMember(memberService.find(memberId));

		entity.setSeller(memberService.find(sellerId));
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            orderService.update(entity);
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

		Page<Order> page = orderService.findPage(beginDate,endDate,pageable);
		return Message.success(PageBlock.bind(page), "admin.list.success");
	}
	
	
	/**
	 * 地区视图
	 */
	@RequestMapping(value = "/areaView", method = RequestMethod.GET)
	public String areaView(Long id, ModelMap model) {


		model.addAttribute("area",areaService.find(id));
		return "/admin/order/view/areaView";
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

		model.addAttribute("occupations",occupationService.findAll());

		model.addAttribute("tags",tagService.findAll());

		model.addAttribute("member",memberService.find(id));
		return "/admin/order/view/memberView";
	}


	/**
	 * 优惠码视图
	 */
	@RequestMapping(value = "/couponCodeView", method = RequestMethod.GET)
	public String couponCodeView(Long id, ModelMap model) {
		model.addAttribute("coupons",couponService.findAll());

		model.addAttribute("members",memberService.findAll());

		model.addAttribute("couponCode",couponCodeService.find(id));
		return "/admin/order/view/couponCodeView";
	}



}