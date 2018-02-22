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
import net.wit.entity.PayBill;
import net.wit.service.PayBillService;

import java.util.*;

import net.wit.*;

import net.wit.entity.*;
import net.wit.service.*;
import net.wit.controller.admin.model.*;



/**
 * @ClassName: PayBillController
 * @author 降魔战队
 * @date 2017-11-4 18:12:37
 */
 
@Controller("adminPayBillController")
@RequestMapping("/admin/payBill")
public class PayBillController extends BaseController {
	@Resource(name = "payBillServiceImpl")
	private PayBillService payBillService;
	
	@Resource(name = "couponCodeServiceImpl")
	private CouponCodeService couponCodeService;

	@Resource(name = "enterpriseServiceImpl")
	private EnterpriseService enterpriseService;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "memberCardServiceImpl")
	private CardService cardService;

	@Resource(name = "shopServiceImpl")
	private ShopService shopService;

	@Resource(name = "couponServiceImpl")
	private CouponService couponService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "topicServiceImpl")
	private TopicService topicService;

	@Resource(name = "occupationServiceImpl")
	private OccupationService occupationService;

	@Resource(name = "tagServiceImpl")
	private TagService tagService;



	/**
	 * 主页
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model) {

		List<MapEntity> methods = new ArrayList<>();
		methods.add(new MapEntity("online","线上结算"));
		methods.add(new MapEntity("offline","线下结算"));
		model.addAttribute("methods",methods);

		List<MapEntity> statuss = new ArrayList<>();
		statuss.add(new MapEntity("none","线上结算"));
		statuss.add(new MapEntity("success","支付成功"));
		statuss.add(new MapEntity("failure","支付失败"));
		model.addAttribute("statuss",statuss);

		return "/admin/payBill/list";
	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {

		List<MapEntity> methods = new ArrayList<>();
		methods.add(new MapEntity("online","线上结算"));
		methods.add(new MapEntity("offline","线下结算"));
		model.addAttribute("methods",methods);

		List<MapEntity> statuss = new ArrayList<>();
		statuss.add(new MapEntity("none","线上结算"));
		statuss.add(new MapEntity("success","支付成功"));
		statuss.add(new MapEntity("failure","支付失败"));
		model.addAttribute("statuss",statuss);

		return "/admin/payBill/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(PayBill payBill, Long couponCodeId, Long enterpriseId, Long memberId, Long cardId,Long ownerId, Long shopId){
		PayBill entity = new PayBill();	

		entity.setCreateDate(payBill.getCreateDate());

		entity.setModifyDate(payBill.getModifyDate());

		entity.setAmount(payBill.getAmount());

		entity.setCouponDiscount(payBill.getCouponDiscount());

		entity.setFee(payBill.getFee());

		entity.setCardDiscount(payBill.getCardDiscount());

		entity.setMethod(payBill.getMethod());

		entity.setNoDiscount(payBill.getNoDiscount());

		entity.setStatus(payBill.getStatus());

		entity.setCouponCode(couponCodeService.find(couponCodeId));

		entity.setEnterprise(enterpriseService.find(enterpriseId));

		entity.setMember(memberService.find(memberId));

		entity.setCard(cardService.find(cardId));

		entity.setOwner(memberService.find(ownerId));

		entity.setShop(shopService.find(shopId));
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            payBillService.save(entity);
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
            payBillService.delete(ids);
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

		List<MapEntity> methods = new ArrayList<>();
		methods.add(new MapEntity("online","线上结算"));
		methods.add(new MapEntity("offline","线下结算"));
		model.addAttribute("methods",methods);

		List<MapEntity> statuss = new ArrayList<>();
		statuss.add(new MapEntity("none","线上结算"));
		statuss.add(new MapEntity("success","支付成功"));
		statuss.add(new MapEntity("failure","支付失败"));
		model.addAttribute("statuss",statuss);

		model.addAttribute("data",payBillService.find(id));

		return "/admin/payBill/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(PayBill payBill, Long couponCodeId, Long enterpriseId, Long memberId,Long ownerId, Long cardId, Long shopId){
		PayBill entity = payBillService.find(payBill.getId());
		
		entity.setCreateDate(payBill.getCreateDate());

		entity.setModifyDate(payBill.getModifyDate());

		entity.setAmount(payBill.getAmount());

		entity.setCouponDiscount(payBill.getCouponDiscount());

		entity.setFee(payBill.getFee());

		entity.setCardDiscount(payBill.getCardDiscount());

		entity.setMethod(payBill.getMethod());

		entity.setNoDiscount(payBill.getNoDiscount());

		entity.setStatus(payBill.getStatus());

		entity.setCouponCode(couponCodeService.find(couponCodeId));

		entity.setEnterprise(enterpriseService.find(enterpriseId));

		entity.setMember(memberService.find(memberId));

		entity.setCard(cardService.find(cardId));

		entity.setOwner(memberService.find(ownerId));

		entity.setShop(shopService.find(shopId));
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            payBillService.update(entity);
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
	public Message list(Date beginDate, Date endDate, PayBill.Method method, PayBill.Status status, Pageable pageable, ModelMap model) {	
		ArrayList<Filter> filters = (ArrayList<Filter>) pageable.getFilters();
		if (method!=null) {
			Filter methodFilter = new Filter("method", Filter.Operator.eq, method);
			filters.add(methodFilter);
		}
		if (status!=null) {
			Filter statusFilter = new Filter("status", Filter.Operator.eq, status);
			filters.add(statusFilter);
		}

		Page<PayBill> page = payBillService.findPage(beginDate,endDate,pageable);
		return Message.success(PageBlock.bind(page), "admin.list.success");
	}
	
	
	/**
	 * 优惠码视图
	 */
	@RequestMapping(value = "/couponCodeView", method = RequestMethod.GET)
	public String couponCodeView(Long id, ModelMap model) {
		model.addAttribute("coupons",couponService.findAll());

		model.addAttribute("members",memberService.findAll());

		model.addAttribute("couponCode",couponCodeService.find(id));
		return "/admin/payBill/view/couponCodeView";
	}


	/**
	 * 企业管理视图
	 */
	@RequestMapping(value = "/enterpriseView", method = RequestMethod.GET)
	public String enterpriseView(Long id, ModelMap model) {
		List<MapEntity> types = new ArrayList<>();
		types.add(new MapEntity("operate","运营商"));
		types.add(new MapEntity("agent","代理商"));
		model.addAttribute("types",types);

		model.addAttribute("areas",areaService.findAll());

		model.addAttribute("enterprise",enterpriseService.find(id));
		return "/admin/payBill/view/enterpriseView";
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

		model.addAttribute("topics",topicService.findAll());

		model.addAttribute("tags",tagService.findAll());

		model.addAttribute("member",memberService.find(id));
		return "/admin/payBill/view/memberView";
	}


	/**
	 * MemberCard视图
	 */
	@RequestMapping(value = "/memberCardView", method = RequestMethod.GET)
	public String memberCardView(Long id, ModelMap model) {


		model.addAttribute("card",cardService.find(id));
		return "/admin/payBill/view/memberCardView";
	}


	/**
	 * Shop视图
	 */
	@RequestMapping(value = "/shopView", method = RequestMethod.GET)
	public String shopView(Long id, ModelMap model) {


		model.addAttribute("shop",shopService.find(id));
		return "/admin/payBill/view/shopView";
	}



}