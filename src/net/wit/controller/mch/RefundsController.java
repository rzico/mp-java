package net.wit.controller.mch;

import net.wit.*;
import net.wit.entity.BaseEntity.Save;
import net.wit.entity.Refunds;
import net.wit.plugin.PaymentPlugin;
import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @ClassName: RefundsController
 * @author 降魔战队
 * @date 2017-10-11 15:37:13
 */
 
@Controller("mchRefundsController")
@RequestMapping("/mch/refunds")
public class RefundsController extends BaseController {
	@Resource(name = "refundsServiceImpl")
	private RefundsService refundsService;
	
	@Resource(name = "paymentServiceImpl")
	private PaymentService paymentService;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "orderServiceImpl")
	private OrderService orderService;

	@Resource(name = "articleRewardServiceImpl")
	private ArticleRewardService articleRewardService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "occupationServiceImpl")
	private OccupationService occupationService;

	@Resource(name = "tagServiceImpl")
	private TagService tagService;

	@Resource(name = "couponCodeServiceImpl")
	private CouponCodeService couponCodeService;

	@Resource(name = "pluginServiceImpl")
	private PluginService pluginService;


	/**
	 * 主页
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model) {

		List<MapEntity> methods = new ArrayList<>();
		methods.add(new MapEntity("online","在线退款"));
		methods.add(new MapEntity("offline","线下退款"));
		methods.add(new MapEntity("deposit","钱包退款"));
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

		model.addAttribute("payments",paymentService.findAll());

		model.addAttribute("orderss",orderService.findAll());

		return "/mch/refunds/list";
	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {

		List<MapEntity> methods = new ArrayList<>();
		methods.add(new MapEntity("online","在线退款"));
		methods.add(new MapEntity("offline","线下退款"));
		methods.add(new MapEntity("deposit","钱包退款"));
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

		model.addAttribute("payments",paymentService.findAll());

		model.addAttribute("orderss",orderService.findAll());

		return "/mch/refunds/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(Refunds refunds, Long paymentId, Long memberId, Long ordersId){
		Refunds entity = new Refunds();	

		entity.setCreateDate(refunds.getCreateDate());

		entity.setModifyDate(refunds.getModifyDate());

		entity.setAmount(refunds.getAmount());

		entity.setMemo(refunds.getMemo());

		entity.setMethod(refunds.getMethod());

		entity.setOperator(refunds.getOperator());

		entity.setPaymentMethod(refunds.getPaymentMethod());

		entity.setPaymentPluginId(refunds.getPaymentPluginId());

		entity.setSn(refunds.getSn());

		entity.setStatus(refunds.getStatus());

		entity.setType(refunds.getType());

		entity.setMember(memberService.find(memberId));

		entity.setPayment(paymentService.find(paymentId));

		entity.setOrder(orderService.find(ordersId));
		
		if (!isValid(entity, Save.class)) {
            return Message.error("admin.data.valid");
        }
        try {
            refundsService.save(entity);
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
            refundsService.delete(ids);
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
		methods.add(new MapEntity("online","在线退款"));
		methods.add(new MapEntity("offline","线下退款"));
		methods.add(new MapEntity("deposit","钱包退款"));
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

		model.addAttribute("payments",paymentService.findAll());

		model.addAttribute("orderss",orderService.findAll());

		model.addAttribute("data",refundsService.find(id));

		return "/mch/refunds/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(Refunds refunds, HttpServletRequest request){
		Refunds entity = refundsService.find(refunds.getId());
		try {
			if (entity.getStatus().equals(Refunds.Status.waiting)) {
				if (refundsService.refunds(entity,request)) {
					return Message.success(entity,"提交成功");
				} else {
					return Message.error("提交失败");
				}
			} else {
				PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(refunds.getPaymentPluginId());
				String resp = paymentPlugin.refundsQuery(refunds,request);
				if ("0000".equals(resp)) {
					refundsService.handle(entity);
					return Message.success(entity,"付款成功");
				} else
				if ("0001".equals(resp)) {
					refundsService.close(entity);
					return Message.success(entity,"退款失败,款项退回账号");
				} else
				if ("9999".equals(resp)) {
					return Message.success(entity,"正在处理中");
				} else {
					return Message.error("查询失败");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Message.error(e.getMessage());
		}
	}
	

	/**
     * 列表
     */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public Message list(Date beginDate, Date endDate, Refunds.Method method, Refunds.Status status, Refunds.Type type, Pageable pageable, ModelMap model) {	
		ArrayList<Filter> filters = (ArrayList<Filter>) pageable.getFilters();
		if (method!=null) {
			Filter methodFilter = new Filter("method", Filter.Operator.eq, method);
			filters.add(methodFilter);
		}
		if (status!=null) {
			Filter statusFilter = new Filter("status", Filter.Operator.eq, status);
			filters.add(statusFilter);
		}
		if (type!=null) {
			Filter typeFilter = new Filter("type", Filter.Operator.eq, type);
			filters.add(typeFilter);
		}

		Page<Refunds> page = refundsService.findPage(beginDate,endDate,pageable);
		return Message.success(PageBlock.bind(page), "admin.list.success");
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
		return "/mch/refunds/view/paymentView";
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
		return "/mch/refunds/view/memberView";
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
		return "/mch/refunds/view/orderView";
	}



}