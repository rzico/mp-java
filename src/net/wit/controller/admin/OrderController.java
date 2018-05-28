package net.wit.controller.admin;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.wit.Filter;
import net.wit.Message;
import net.wit.Pageable;

import net.wit.plugin.PaymentPlugin;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
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

	@Resource(name = "adminServiceImpl")
	private AdminService adminService;

	@Resource(name = "refundsServiceImpl")
	private RefundsService refundsService;

	@Resource(name = "pluginServiceImpl")
	private PluginService pluginService;


	/**
	 * 订单锁定
	 */
	@RequestMapping(value = "/lock", method = RequestMethod.POST)
	public @ResponseBody
	Message lock(String sn) {
		Admin admin = adminService.getCurrent();

		Order order = orderService.findBySn(sn);
		if (order != null && !order.isLocked(admin.getUsername())) {
			order.setLockExpire(DateUtils.addSeconds(new Date(), 20));
			order.setOperator(admin.getUsername());
			orderService.update(order);
			return Message.success(true,"true");
		}
		return Message.success(false,"false");
	}

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

//		model.addAttribute("areas",areaService.findAll());

//		model.addAttribute("couponCodes",couponCodeService.findAll());

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
	public Message list(Date beginDate, Date endDate,String searchValue, Order.OrderStatus orderStatus, Order.PaymentStatus paymentStatus, Order.ShippingStatus shippingStatus, Pageable pageable, ModelMap model) {
		ArrayList<Filter> filters = (ArrayList<Filter>) pageable.getFilters();
		if(null != orderStatus){
			Filter orderStatusFilter = new Filter("orderStatus",Filter.Operator.eq,orderStatus);
			filters.add(orderStatusFilter);
		}

		if(null != paymentStatus){
			Filter paymentStatusFilter = new Filter("paymentStatus",Filter.Operator.eq,paymentStatus);
			filters.add(paymentStatusFilter);
		}

		if(null != shippingStatus){
			Filter shippingStatusFilter = new Filter("shippingStatus",Filter.Operator.eq,shippingStatus);
			filters.add(shippingStatusFilter);
		}

		Admin admin =adminService.getCurrent();
//		System.out.println("admin.ID:"+admin.getId());
		Enterprise enterprise=admin.getEnterprise();
//		System.out.println("enter.ID:"+enterprise.getId());

		if(enterprise==null){
			return Message.error("您还未绑定企业");
		}
		//判断企业是否被删除
		if(enterprise.getDeleted()){
			Message.error("您的企业不存在");
		}
		//代理商(無權限)
		//个人代理商(無權限)
		//商家
		if(enterprise.getType()== Enterprise.Type.shop){
			if(enterprise.getMember()!=null){
				Filter mediaTypeFilter = new Filter("seller", Filter.Operator.eq, enterprise.getMember());
//				System.out.println("admin.enter.member.ID:"+enterprise.getMember().getId());
				filters.add(mediaTypeFilter);
			}
			else{
				return Message.error("该商家未绑定");
			}
		}

		if(searchValue!=null){
			Filter mediaTypeFilter = new Filter("consignee", Filter.Operator.like, "%"+searchValue+"%");
			filters.add(mediaTypeFilter);
		}
		Page<Order> page = orderService.findPage(beginDate,endDate,null,pageable);
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

	/**
	 * 订单取消
	 */
	@RequestMapping(value = "/cancel", method = RequestMethod.POST)
	@ResponseBody
	public Message cancel(Long orderId){
		Order order = orderService.find(orderId);
		Admin admin = adminService.getCurrent();

		if (admin.getEnterprise()==null) {
			return Message.error("没有就业不能操作订单");
		}

		if (null == admin.getEnterprise().getMember() || !admin.getEnterprise().getMember().equals(order.getSeller())) {
			return Message.error("只能操作本企业订单");
		}

		if (!order.getOrderStatus().equals(Order.OrderStatus.unconfirmed)) {
			return Message.error("订单已审核");
		}

		if (order.isLocked(admin.getUsername())) {
			return Message.error("订单处理中，请稍候再试");
		}

		try {
			orderService.cancel(order,admin);
			return Message.success(order,"订单关闭成功!");
		} catch (Exception e) {
			e.printStackTrace();
			return Message.error("订单关闭失败!");
		}
	}

	/**
	 * 订单确认
	 */
	@RequestMapping(value = "/confirm", method = RequestMethod.POST)
	@ResponseBody
	public Message confirm(Long orderId){
		Order order = orderService.find(orderId);
		Admin admin = adminService.getCurrent();
//System.out.println("order.seller.id"+order.getSeller().getId());
//		System.out.println("admin.enter.member.id"+admin.getEnterprise().getMember().getId());
		if (admin.getEnterprise()==null) {
			return Message.error("没有就业不能操作订单");
		}

		if (null == admin.getEnterprise().getMember() || !admin.getEnterprise().getMember().equals(order.getSeller())) {
			return Message.error("只能操作本企业订单");
		}

		if (!order.getOrderStatus().equals(Order.OrderStatus.unconfirmed)) {
			return Message.error("订单已审核");
		}

		if (order.isLocked(admin.getUsername())) {
			return Message.error("订单处理中，请稍候再试");
		}

		try {
			orderService.confirm(order,admin);
			order = orderService.find(orderId);
			return Message.success(order,"订单确认成功!");
		} catch (Exception e) {
			e.printStackTrace();
			return Message.error("订单确认失败!");
		}
	}

	/**
	 * 订单发货
	 */
	@RequestMapping(value = "/shipping", method = RequestMethod.POST)
	@ResponseBody
	public Message shipping(Long orderId){
		Order order = orderService.find(orderId);
		Admin admin = adminService.getCurrent();

		if (admin.getEnterprise()==null) {
			return Message.error("没有就业不能操作订单");
		}

		if (null != admin.getEnterprise().getMember() || !admin.getEnterprise().getMember().equals(order.getSeller())) {
			return Message.error("只能操作本企业订单");
		}

		if (!order.getOrderStatus().equals(Order.OrderStatus.confirmed)) {
			return Message.error("订单未审核");
		}

		if (!order.getShippingStatus().equals(Order.ShippingStatus.unshipped)) {
			return Message.error("不是待发货订单");
		}

		if (order.isLocked(admin.getUsername())) {
			return Message.error("订单处理中，请稍候再试");
		}

		try {
			orderService.shipping(order,null,null,admin);
			order = orderService.find(orderId);
			return Message.success(order,"订单发货成功!");
		} catch (Exception e) {
			e.printStackTrace();
			return Message.error("订单发货失败!");
		}
	}

	/**
	 * 订单退货
	 */
	@RequestMapping(value = "/returns", method = RequestMethod.POST)
	@ResponseBody
	public Message returns(Long orderId){
		Order order = orderService.find(orderId);
		Admin admin = adminService.getCurrent();

		if (admin.getEnterprise()==null) {
			return Message.error("没有就业不能操作订单");
		}

		if (null != admin.getEnterprise().getMember() || !admin.getEnterprise().getMember().equals(order.getSeller())) {
			return Message.error("只能操作本企业订单");
		}

		if (!order.getOrderStatus().equals(Order.OrderStatus.confirmed)) {
			return Message.error("订单未审核");
		}

		if (!order.getShippingStatus().equals(Order.ShippingStatus.shipped) && !order.getShippingStatus().equals(Order.ShippingStatus.returning)) {
			return Message.error("不能退货状态");
		}

		if (order.isLocked(admin.getUsername())) {
			return Message.error("订单处理中，请稍候再试");
		}

		try {
			orderService.returns(order,admin);
			return Message.success(order,"订单退货成功!");
		} catch (Exception e) {
			e.printStackTrace();
			return Message.error("订单退货失败!");
		}
	}

	/**
	 * 订单退款
	 */
	@RequestMapping(value = "/refunds", method = RequestMethod.POST)
	@ResponseBody
	public Message refunds(Long orderId ,HttpServletRequest request){
		Order order = orderService.find(orderId);
		if(null == order){
			return Message.error("无效订单ID");
		}

		Admin admin = adminService.getCurrent();

		if (admin.getEnterprise()==null) {
			return Message.error("没有就业不能操作订单");
		}

		if (null != admin.getEnterprise().getMember() || !admin.getEnterprise().getMember().equals(order.getSeller())) {
			return Message.error("只能操作本企业订单");
		}

		if (!order.getOrderStatus().equals(Order.OrderStatus.confirmed)) {
			return Message.error("订单未审核");
		}
//
//		if (!order.getPaymentStatus().equals(Order.PaymentStatus.paid) && !order.getPaymentStatus().equals(Order.PaymentStatus.refunding)) {
//			return Message.error("不能退款状态");
//		}

		if (order.isLocked(admin.getUsername())) {
			return Message.error("订单处理中，请稍候再试");
		}

		try {
			if (order.getPaymentStatus().equals(Order.PaymentStatus.paid) ) {
				orderService.refunds(order, admin);
			}
			//执行退款
		} catch (Exception e) {
			return Message.error(e.getMessage());
		}


		for(Refunds refunds:order.getRefunds()){
			if (null == refunds){
				return  Message.error("退款单无效");
			}

			String paymentPluginId = refunds.getPaymentPluginId();
			PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(paymentPluginId);
			if ((null == paymentPlugin) || (!paymentPlugin.getIsEnabled())){
				return Message.error("支付插件无效");
			}

			try {
				if(refunds.getStatus().equals(Refunds.Status.waiting)){
					refundsService.refunds(refunds,request);
				}
			} catch (Exception e) {
				e.printStackTrace();
				return Message.error(e.getMessage());
			}

			Map<String, Object> parameters = paymentPlugin.refunds(refunds,request);
			if ("SUCCESS".equals(parameters.get("return_code"))) {
					try {
						refundsService.handle(refunds);
						return Message.success(order,"退款成功!");
					} catch (Exception e) {
						e.printStackTrace();
						//模拟异常通知，通知失败忽略异常，因为也算支付成了，只是通知失败
						return Message.error("退款失败!");
					}
			} else {
				String resultCode  = null;
				try {
					resultCode = paymentPlugin.refundsQuery(refunds,request);
				} catch (Exception e) {
					e.printStackTrace();
					resultCode = "9999";
				}
				switch (resultCode) {
					case "0000":
						try {
							refundsService.handle(refunds);
						} catch (Exception e) {
							logger.error(e.getMessage());
						}
						return Message.success("退款成功");
					case "0001":
						try {
							refundsService.close(refunds);
						} catch (Exception e) {
							logger.error(e.getMessage());
						}
						return Message.success(order,"退款成功!");
					default:
						return Message.error("查询失败，稍候再试");
				}
			}
		}
		return Message.error("退款单无效!");
	}

}