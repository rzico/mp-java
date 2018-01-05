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
		List<MapEntity> paymentMethods = new ArrayList<>();
		paymentMethods.add(new MapEntity("online","线上结算"));
		paymentMethods.add(new MapEntity("offline","线下结算"));
		model.addAttribute("paymentMethods",paymentMethods);

		List<MapEntity> shippingMethods = new ArrayList<>();
		shippingMethods.add(new MapEntity("shipping","卖家配送"));
		shippingMethods.add(new MapEntity("pickup","线下提货"));
		model.addAttribute("shippingMethods",shippingMethods);

		List<MapEntity> orderStatuss = new ArrayList<>();
		orderStatuss.add(new MapEntity("unconfirmed","未确认"));
		orderStatuss.add(new MapEntity("confirmed","已确认"));
		orderStatuss.add(new MapEntity("completed","已完成"));
		orderStatuss.add(new MapEntity("cancelled","已取消"));
		model.addAttribute("orderStatuss",orderStatuss);

		List<MapEntity> paymentStatuss = new ArrayList<>();
		paymentStatuss.add(new MapEntity("unpaid","未支付"));
		paymentStatuss.add(new MapEntity("paid","已支付"));
		paymentStatuss.add(new MapEntity("refunding","退款中"));
		paymentStatuss.add(new MapEntity("refunded","已退款"));
		model.addAttribute("paymentStatuss",paymentStatuss);

		List<MapEntity> shippingStatuss = new ArrayList<>();
		shippingStatuss.add(new MapEntity("unshipped","未发货"));
		shippingStatuss.add(new MapEntity("shipped","已发货"));
		shippingStatuss.add(new MapEntity("returning","退货中"));
		shippingStatuss.add(new MapEntity("returned","已退货"));
		model.addAttribute("shippingStatuss",shippingStatuss);

		model.addAttribute("areas",areaService.findAll());

		model.addAttribute("couponCodes",couponCodeService.findAll());

		//model.addAttribute("members",memberService.findAll());

		//model.addAttribute("sellers",memberService.findAll());

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
			for(long id: ids){
				Order order = orderService.find(id);
				order.setDeleted(true);
				orderService.update(order);
			}
            //orderService.delete(ids);
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
		//订单信息
		List<MapEntity> paymentMethods = new ArrayList<>();
		paymentMethods.add(new MapEntity("online","线上结算"));
		paymentMethods.add(new MapEntity("offline","线下结算"));
		model.addAttribute("paymentMethods",paymentMethods);

		List<MapEntity> shippingMethods = new ArrayList<>();
		shippingMethods.add(new MapEntity("shipping","卖家配送"));
		shippingMethods.add(new MapEntity("pickup","线下提货"));
		model.addAttribute("shippingMethods",shippingMethods);

		List<MapEntity> orderStatuss = new ArrayList<>();
		orderStatuss.add(new MapEntity("unconfirmed","未确认"));
		orderStatuss.add(new MapEntity("confirmed","已确认"));
		orderStatuss.add(new MapEntity("completed","已完成"));
		orderStatuss.add(new MapEntity("cancelled","已取消"));
		model.addAttribute("orderStatuss",orderStatuss);

		List<MapEntity> paymentStatuss = new ArrayList<>();
		paymentStatuss.add(new MapEntity("unpaid","未支付"));
		paymentStatuss.add(new MapEntity("paid","已支付"));
		paymentStatuss.add(new MapEntity("refunding","退款中"));
		paymentStatuss.add(new MapEntity("refunded","已退款"));
		model.addAttribute("paymentStatuss",paymentStatuss);

		List<MapEntity> shippingStatuss = new ArrayList<>();
		shippingStatuss.add(new MapEntity("unshipped","未发货"));
		shippingStatuss.add(new MapEntity("shipped","已发货"));
		shippingStatuss.add(new MapEntity("returning","退货中"));
		shippingStatuss.add(new MapEntity("returned","已退货"));
		model.addAttribute("shippingStatuss",shippingStatuss);

		//收款信息
		List<MapEntity> shoukuanStatuss = new ArrayList<>();
		shoukuanStatuss.add(new MapEntity("waiting","等待支付"));
		shoukuanStatuss.add(new MapEntity("success","支付成功"));
		shoukuanStatuss.add(new MapEntity("failure","支付失败"));
		shoukuanStatuss.add(new MapEntity("refund_waiting","等待退款"));
		shoukuanStatuss.add(new MapEntity("refund_success","退款完成"));
		shoukuanStatuss.add(new MapEntity("refund_failure","退款失败"));
		model.addAttribute("shoukuanStatuss",shoukuanStatuss);

		List<MapEntity> shoukuanMethods = new ArrayList<>();
		shoukuanMethods.add(new MapEntity("online","在线支付"));
		shoukuanMethods.add(new MapEntity("offline","线下支付"));
		shoukuanMethods.add(new MapEntity("deposit","余额支付"));
		shoukuanMethods.add(new MapEntity("card","会员卡支付"));
		model.addAttribute("shoukuanMethods",shoukuanMethods);

		//退款信息
		List<MapEntity> tuikuanStatuss = new ArrayList<>();
		tuikuanStatuss.add(new MapEntity("waiting","等待退款"));
		tuikuanStatuss.add(new MapEntity("confirmed","确定提交"));
		tuikuanStatuss.add(new MapEntity("success","退款成功"));
		tuikuanStatuss.add(new MapEntity("failure","退款失败"));
		model.addAttribute("tuikuanStatuss",tuikuanStatuss);

		List<MapEntity> tuikuanMethods = new ArrayList<>();
		tuikuanMethods.add(new MapEntity("online","在线退款"));
		tuikuanMethods.add(new MapEntity("offline","线下退款"));
		tuikuanMethods.add(new MapEntity("deposit","余额退款"));
		tuikuanMethods.add(new MapEntity("card","会员卡退款"));
		model.addAttribute("tuikuanMethods",tuikuanMethods);

		//订单日志
		List<MapEntity> logTypes = new ArrayList<>();
		logTypes.add(new MapEntity("create","订单创建"));
		logTypes.add(new MapEntity("modify","订单修改"));
		logTypes.add(new MapEntity("confirm","订单确认"));
		logTypes.add(new MapEntity("payment","订单支付"));
		logTypes.add(new MapEntity("refunds","订单退款"));
		logTypes.add(new MapEntity("shipping","订单发货"));
		logTypes.add(new MapEntity("returns","订单退货"));
		logTypes.add(new MapEntity("complete","订单完成"));
		logTypes.add(new MapEntity("cancel","订单取消"));
		logTypes.add(new MapEntity("other","其它"));
		model.addAttribute("logTypes",logTypes);

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