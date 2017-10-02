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
import net.wit.entity.Shipping;
import net.wit.service.ShippingService;

import java.util.*;

import net.wit.*;

import net.wit.entity.*;
import net.wit.service.*;
import net.wit.controller.admin.model.*;



/**
 * @ClassName: ShippingController
 * @author 降魔战队
 * @date 2017-9-14 19:42:17
 */
 
@Controller("adminShippingController")
@RequestMapping("/admin/shipping")
public class ShippingController extends BaseController {
	@Resource(name = "shippingServiceImpl")
	private ShippingService shippingService;
	
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "orderServiceImpl")
	private OrderService orderService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "tagServiceImpl")
	private TagService tagService;

	@Resource(name = "couponCodeServiceImpl")
	private CouponCodeService couponCodeService;



	/**
	 * 主页
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model) {

		model.addAttribute("members",memberService.findAll());

		model.addAttribute("orderss",orderService.findAll());

		model.addAttribute("sellers",memberService.findAll());

		return "/admin/shipping/list";
	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {

		model.addAttribute("members",memberService.findAll());

		model.addAttribute("orderss",orderService.findAll());

		model.addAttribute("sellers",memberService.findAll());

		return "/admin/shipping/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(Shipping shipping, Long memberId, Long sellerId, Long ordersId){
		Shipping entity = new Shipping();	

		entity.setCreateDate(shipping.getCreateDate());

		entity.setModifyDate(shipping.getModifyDate());

		entity.setAddress(shipping.getAddress());

		entity.setAreaName(shipping.getAreaName());

		entity.setConsignee(shipping.getConsignee());

		entity.setDeliveryCorp(shipping.getDeliveryCorp());

		entity.setFreight(shipping.getFreight());

		entity.setMemo(shipping.getMemo());

		entity.setOperator(shipping.getOperator());

		entity.setPhone(shipping.getPhone());

		entity.setShippingMethod(shipping.getShippingMethod());

		entity.setSn(shipping.getSn());

		entity.setTrackingNo(shipping.getTrackingNo());

		entity.setZipCode(shipping.getZipCode());

		entity.setMember(memberService.find(memberId));

		entity.setOrder(orderService.find(ordersId));

		entity.setSeller(memberService.find(sellerId));
		
		if (!isValid(entity, Save.class)) {
            return Message.error("admin.data.valid");
        }
        try {
            shippingService.save(entity);
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
            shippingService.delete(ids);
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

		model.addAttribute("members",memberService.findAll());

		model.addAttribute("orderss",orderService.findAll());

		model.addAttribute("sellers",memberService.findAll());

		model.addAttribute("data",shippingService.find(id));

		return "/admin/shipping/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(Shipping shipping, Long memberId, Long sellerId, Long ordersId){
		Shipping entity = shippingService.find(shipping.getId());
		
		entity.setCreateDate(shipping.getCreateDate());

		entity.setModifyDate(shipping.getModifyDate());

		entity.setAddress(shipping.getAddress());

		entity.setAreaName(shipping.getAreaName());

		entity.setConsignee(shipping.getConsignee());

		entity.setDeliveryCorp(shipping.getDeliveryCorp());

		entity.setFreight(shipping.getFreight());

		entity.setMemo(shipping.getMemo());

		entity.setOperator(shipping.getOperator());

		entity.setPhone(shipping.getPhone());

		entity.setShippingMethod(shipping.getShippingMethod());

		entity.setSn(shipping.getSn());

		entity.setTrackingNo(shipping.getTrackingNo());

		entity.setZipCode(shipping.getZipCode());

		entity.setMember(memberService.find(memberId));

		entity.setOrder(orderService.find(ordersId));

		entity.setSeller(memberService.find(sellerId));
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            shippingService.update(entity);
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

		Page<Shipping> page = shippingService.findPage(beginDate,endDate,pageable);
		return Message.success(PageBlock.bind(page), "admin.list.success");
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
		return "/admin/shipping/view/memberView";
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
		return "/admin/shipping/view/orderView";
	}



}