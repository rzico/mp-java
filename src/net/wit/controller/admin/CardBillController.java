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
import net.wit.entity.CardBill;
import net.wit.service.CardBillService;

import java.util.*;

import net.wit.*;

import net.wit.entity.*;
import net.wit.service.*;
import net.wit.controller.admin.model.*;



/**
 * @ClassName: CardBillController
 * @author 降魔战队
 * @date 2017-11-4 18:12:32
 */
 
@Controller("adminCardBillController")
@RequestMapping("/admin/cardBill")
public class CardBillController extends BaseController {
	@Resource(name = "cardBillServiceImpl")
	private CardBillService cardBillService;
	
	@Resource(name = "cardServiceImpl")
	private CardService cardService;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "payBillServiceImpl")
	private PayBillService payBillService;

	@Resource(name = "paymentServiceImpl")
	private PaymentService paymentService;

	@Resource(name = "shopServiceImpl")
	private ShopService shopService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "topicServiceImpl")
	private TopicService topicService;

	@Resource(name = "occupationServiceImpl")
	private OccupationService occupationService;

	@Resource(name = "tagServiceImpl")
	private TagService tagService;

	@Resource(name = "couponCodeServiceImpl")
	private CouponCodeService couponCodeService;

	@Resource(name = "enterpriseServiceImpl")
	private EnterpriseService enterpriseService;

	@Resource(name = "cardServiceImpl")
	private CardService CardService;

	@Resource(name = "articleRewardServiceImpl")
	private ArticleRewardService articleRewardService;

	@Resource(name = "orderServiceImpl")
	private OrderService orderService;



	/**
	 * 主页
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model) {

		List<MapEntity> methods = new ArrayList<>();
		methods.add(new MapEntity("online","线上结算"));
		methods.add(new MapEntity("offline","线下结算"));
		model.addAttribute("methods",methods);

		List<MapEntity> types = new ArrayList<>();
		types.add(new MapEntity("recharge","充值"));
		types.add(new MapEntity("consume","消费"));
		types.add(new MapEntity("refunds","退款"));
		model.addAttribute("types",types);

		return "/admin/cardBill/list";
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

		List<MapEntity> types = new ArrayList<>();
		types.add(new MapEntity("recharge","充值"));
		types.add(new MapEntity("consume","消费"));
		types.add(new MapEntity("refunds","退款"));
		model.addAttribute("types",types);

		return "/admin/cardBill/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(CardBill cardBill, Long cardId, Long memberId, Long payBillId, Long paymentId, Long shopId){
		CardBill entity = new CardBill();	

		entity.setCreateDate(cardBill.getCreateDate());

		entity.setModifyDate(cardBill.getModifyDate());

		entity.setBalance(cardBill.getBalance());

		entity.setCredit(cardBill.getCredit());

		entity.setDebit(cardBill.getDebit());

		entity.setDeleted(cardBill.getDeleted());

		entity.setMemo(cardBill.getMemo());

		entity.setMethod(cardBill.getMethod());

		entity.setOperator(cardBill.getOperator());

		entity.setType(cardBill.getType());

		entity.setCard(cardService.find(cardId));

		entity.setMember(memberService.find(memberId));

		entity.setPayBill(payBillService.find(payBillId));

		entity.setPayment(paymentService.find(paymentId));

		entity.setShop(shopService.find(shopId));
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            cardBillService.save(entity);
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
            cardBillService.delete(ids);
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

		List<MapEntity> types = new ArrayList<>();
		types.add(new MapEntity("recharge","充值"));
		types.add(new MapEntity("consume","消费"));
		types.add(new MapEntity("refunds","退款"));
		model.addAttribute("types",types);

		model.addAttribute("data",cardBillService.find(id));

		return "/admin/cardBill/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(CardBill cardBill, Long cardId, Long memberId, Long payBillId, Long paymentId, Long shopId){
		CardBill entity = cardBillService.find(cardBill.getId());
		
		entity.setCreateDate(cardBill.getCreateDate());

		entity.setModifyDate(cardBill.getModifyDate());

		entity.setBalance(cardBill.getBalance());

		entity.setCredit(cardBill.getCredit());

		entity.setDebit(cardBill.getDebit());

		entity.setDeleted(cardBill.getDeleted());

		entity.setMemo(cardBill.getMemo());

		entity.setMethod(cardBill.getMethod());

		entity.setOperator(cardBill.getOperator());

		entity.setType(cardBill.getType());

		entity.setCard(cardService.find(cardId));

		entity.setMember(memberService.find(memberId));

		entity.setPayBill(payBillService.find(payBillId));

		entity.setPayment(paymentService.find(paymentId));

		entity.setShop(shopService.find(shopId));
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            cardBillService.update(entity);
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
	public Message list(Date beginDate, Date endDate, CardBill.Method method, CardBill.Type type, Pageable pageable, ModelMap model) {	
		ArrayList<Filter> filters = (ArrayList<Filter>) pageable.getFilters();
		if (method!=null) {
			Filter methodFilter = new Filter("method", Filter.Operator.eq, method);
			filters.add(methodFilter);
		}
		if (type!=null) {
			Filter typeFilter = new Filter("type", Filter.Operator.eq, type);
			filters.add(typeFilter);
		}

		Page<CardBill> page = cardBillService.findPage(beginDate,endDate,pageable);
		return Message.success(PageBlock.bind(page), "admin.list.success");
	}
	
	
	/**
	 * Card视图
	 */
	@RequestMapping(value = "/cardView", method = RequestMethod.GET)
	public String cardView(Long id, ModelMap model) {


		model.addAttribute("card",cardService.find(id));
		return "/admin/cardBill/view/cardView";
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
		return "/admin/cardBill/view/memberView";
	}


	/**
	 * PayBill视图
	 */
	@RequestMapping(value = "/payBillView", method = RequestMethod.GET)
	public String payBillView(Long id, ModelMap model) {
		List<MapEntity> methods = new ArrayList<>();
		methods.add(new MapEntity("online","线上结算"));
		methods.add(new MapEntity("offline","线下结算"));
		model.addAttribute("methods",methods);

		List<MapEntity> statuss = new ArrayList<>();
		statuss.add(new MapEntity("none","线上结算"));
		statuss.add(new MapEntity("success","支付成功"));
		statuss.add(new MapEntity("failure","支付失败"));
		model.addAttribute("statuss",statuss);

		model.addAttribute("payBill",payBillService.find(id));
		return "/admin/cardBill/view/payBillView";
	}


	/**
	 * 收款单视图
	 */
	@RequestMapping(value = "/paymentView", method = RequestMethod.GET)
	public String paymentView(Long id, ModelMap model) {
		List<MapEntity> methods = new ArrayList<>();
		methods.add(new MapEntity("online","在线支付"));
		methods.add(new MapEntity("offline","线下支付"));
		methods.add(new MapEntity("deposit","钱包支付"));
		model.addAttribute("methods",methods);

		List<MapEntity> statuss = new ArrayList<>();
		statuss.add(new MapEntity("wait","等待支付"));
		statuss.add(new MapEntity("success","支付成功"));
		statuss.add(new MapEntity("failure","支付失败"));
		model.addAttribute("statuss",statuss);

		List<MapEntity> types = new ArrayList<>();
		types.add(new MapEntity("payment","消费支付"));
		types.add(new MapEntity("recharge","钱包充值"));
		model.addAttribute("types",types);

		model.addAttribute("members",memberService.findAll());

		model.addAttribute("orderss",orderService.findAll());

		model.addAttribute("articleRewards",articleRewardService.findAll());

		model.addAttribute("payees",memberService.findAll());

		model.addAttribute("payment",paymentService.find(id));
		return "/admin/cardBill/view/paymentView";
	}


	/**
	 * Shop视图
	 */
	@RequestMapping(value = "/shopView", method = RequestMethod.GET)
	public String shopView(Long id, ModelMap model) {


		model.addAttribute("shop",shopService.find(id));
		return "/admin/cardBill/view/shopView";
	}



}