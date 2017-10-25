package net.wit.controller.mch;

import net.wit.*;
import net.wit.entity.BaseEntity.Save;
import net.wit.entity.Payment;
import net.wit.entity.Transfer;
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
 * @ClassName: PaymentController
 * @author 降魔战队
 * @date 2017-10-11 15:37:11
 */
 
@Controller("mchPaymentController")
@RequestMapping("/mch/payment")
public class PaymentController extends BaseController {

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "articleRewardServiceImpl")
	private ArticleRewardService articleRewardService;

	@Resource(name = "orderServiceImpl")
	private OrderService orderService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "occupationServiceImpl")
	private OccupationService occupationService;

	@Resource(name = "tagServiceImpl")
	private TagService tagService;

	@Resource(name = "paymentServiceImpl")
	private PaymentService paymentService;

	@Resource(name = "articleServiceImpl")
	private ArticleService articleService;

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
		methods.add(new MapEntity("online","在线支付"));
		methods.add(new MapEntity("offline","线下支付"));
		methods.add(new MapEntity("deposit","钱包支付"));
		model.addAttribute("methods",methods);

		List<MapEntity> statuss = new ArrayList<>();
		statuss.add(new MapEntity("wait","等待支付"));
		statuss.add(new MapEntity("success","支付成功"));
		statuss.add(new MapEntity("failure","支付失败"));
		statuss.add(new MapEntity("refund_waiting","等待退款"));
		statuss.add(new MapEntity("refund_success","退款完成"));
		statuss.add(new MapEntity("refund_failure","退款失败"));
		model.addAttribute("statuss",statuss);

		List<MapEntity> types = new ArrayList<>();
		types.add(new MapEntity("payment","消费支付"));
		types.add(new MapEntity("recharge","钱包充值"));
		model.addAttribute("types",types);

		model.addAttribute("members",memberService.findAll());

		model.addAttribute("orderss",orderService.findAll());

		model.addAttribute("articleRewards",articleRewardService.findAll());

		model.addAttribute("payees",memberService.findAll());

		return "/mch/payment/list";
	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {

		List<MapEntity> methods = new ArrayList<>();
		methods.add(new MapEntity("online","在线支付"));
		methods.add(new MapEntity("offline","线下支付"));
		methods.add(new MapEntity("deposit","钱包支付"));
		model.addAttribute("methods",methods);

		List<MapEntity> statuss = new ArrayList<>();
		statuss.add(new MapEntity("wait","等待支付"));
		statuss.add(new MapEntity("success","支付成功"));
		statuss.add(new MapEntity("failure","支付失败"));
		statuss.add(new MapEntity("refund_waiting","等待退款"));
		statuss.add(new MapEntity("refund_success","退款完成"));
		statuss.add(new MapEntity("refund_failure","退款失败"));
		model.addAttribute("statuss",statuss);

		List<MapEntity> types = new ArrayList<>();
		types.add(new MapEntity("payment","消费支付"));
		types.add(new MapEntity("recharge","钱包充值"));
		model.addAttribute("types",types);

		model.addAttribute("members",memberService.findAll());

		model.addAttribute("orderss",orderService.findAll());

		model.addAttribute("articleRewards",articleRewardService.findAll());

		model.addAttribute("payees",memberService.findAll());

		return "/mch/payment/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(Payment payment, Long memberId, Long articleRewardId, Long payeeId, Long ordersId){
		Payment entity = new Payment();	

		entity.setCreateDate(payment.getCreateDate());

		entity.setModifyDate(payment.getModifyDate());

		entity.setAmount(payment.getAmount());

		entity.setExpire(payment.getExpire());

		entity.setMemo(payment.getMemo());

		entity.setMethod(payment.getMethod());

		entity.setOperator(payment.getOperator());

		entity.setPaymentDate(payment.getPaymentDate());

		entity.setPaymentMethod(payment.getPaymentMethod());

		entity.setPaymentPluginId(payment.getPaymentPluginId());

		entity.setSn(payment.getSn());

		entity.setStatus(payment.getStatus());

		entity.setType(payment.getType());

		entity.setMember(memberService.find(memberId));

		entity.setOrder(orderService.find(ordersId));

		entity.setArticleReward(articleRewardService.find(articleRewardId));

		entity.setPayee(memberService.find(payeeId));
		
		if (!isValid(entity, Save.class)) {
            return Message.error("admin.data.valid");
        }
        try {
            paymentService.save(entity);
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
            paymentService.delete(ids);
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
		methods.add(new MapEntity("online","在线支付"));
		methods.add(new MapEntity("offline","线下支付"));
		methods.add(new MapEntity("deposit","钱包支付"));
		model.addAttribute("methods",methods);

		List<MapEntity> statuss = new ArrayList<>();
		statuss.add(new MapEntity("wait","等待支付"));
		statuss.add(new MapEntity("success","支付成功"));
		statuss.add(new MapEntity("failure","支付失败"));
		statuss.add(new MapEntity("refund_waiting","等待退款"));
		statuss.add(new MapEntity("refund_success","退款完成"));
		statuss.add(new MapEntity("refund_failure","退款失败"));
		model.addAttribute("statuss",statuss);

		List<MapEntity> types = new ArrayList<>();
		types.add(new MapEntity("payment","消费支付"));
		types.add(new MapEntity("recharge","钱包充值"));
		model.addAttribute("types",types);

		model.addAttribute("members",memberService.findAll());

		model.addAttribute("orderss",orderService.findAll());

		model.addAttribute("articleRewards",articleRewardService.findAll());

		model.addAttribute("payees",memberService.findAll());

		model.addAttribute("data",paymentService.find(id));

		return "/mch/payment/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(Payment payment,HttpServletRequest request){
		Payment entity = paymentService.find(payment.getId());
		try {
			if (entity.getStatus().equals(Transfer.Status.waiting)) {
				PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(payment.getPaymentPluginId());
				String resultCode = null;
				try {
					resultCode = paymentPlugin.queryOrder(payment,request);
				} catch (Exception e) {
					logger.error(e.getMessage());
					return Message.success(e.getMessage());
				}
				switch (resultCode) {
					case "0000":
						try {
							paymentService.handle(payment);
						} catch (Exception e) {
							logger.error(e.getMessage());
						}
						return Message.success((Object) resultCode,"支付成功");
					case "0001":
						try {
							paymentService.close(payment);
						} catch (Exception e) {
							logger.error(e.getMessage());
						}
						return Message.success((Object) resultCode,"支付失败");
					default:
						return Message.success((Object) resultCode,"支付中");
				}
			} else {
				return Message.error("已处理了");
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
	public Message list(Date beginDate, Date endDate, Payment.Method method, Payment.Status status, Payment.Type type, Pageable pageable, ModelMap model) {	
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

		Page<Payment> page = paymentService.findPage(beginDate,endDate,pageable);
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

		model.addAttribute("occupations",occupationService.findAll());

		model.addAttribute("tags",tagService.findAll());

		model.addAttribute("member",memberService.find(id));
		return "/mch/payment/view/memberView";
	}


	/**
	 * ArticleReward视图
	 */
	@RequestMapping(value = "/articleRewardView", method = RequestMethod.GET)
	public String articleRewardView(Long id, ModelMap model) {
		List<MapEntity> statuss = new ArrayList<>();
		statuss.add(new MapEntity("wait","等待支付"));
		statuss.add(new MapEntity("success","支付成功"));
		statuss.add(new MapEntity("failure","支付失败"));
		model.addAttribute("statuss",statuss);

		model.addAttribute("articles",articleService.findAll());

		model.addAttribute("authors",memberService.findAll());

		model.addAttribute("members",memberService.findAll());

		model.addAttribute("payments",paymentService.findAll());

		model.addAttribute("articleReward",articleRewardService.find(id));
		return "/mch/payment/view/articleRewardView";
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
		return "/mch/payment/view/orderView";
	}



}