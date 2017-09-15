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
import net.wit.entity.Deposit;
import net.wit.service.DepositService;

import java.util.*;

import net.wit.*;

import net.wit.entity.*;
import net.wit.service.*;
import net.wit.controller.admin.model.*;



/**
 * @ClassName: DepositController
 * @author 降魔战队
 * @date 2017-9-14 19:42:13
 */
 
@Controller("adminDepositController")
@RequestMapping("/admin/deposit")
public class DepositController extends BaseController {
	@Resource(name = "depositServiceImpl")
	private DepositService depositService;
	
	@Resource(name = "paymentServiceImpl")
	private PaymentService paymentService;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "refundsServiceImpl")
	private RefundsService refundsService;

	@Resource(name = "orderServiceImpl")
	private OrderService orderService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "tagServiceImpl")
	private TagService tagService;



	/**
	 * 主页
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model) {

		List<MapEntity> types = new ArrayList<>();
		types.add(new MapEntity("recharge","充值"));
		types.add(new MapEntity("payment","支付"));
		types.add(new MapEntity("refunds","退款"));
		model.addAttribute("types",types);

		model.addAttribute("members",memberService.findAll());

		model.addAttribute("payments",paymentService.findAll());

		model.addAttribute("refundss",refundsService.findAll());

		return "/admin/deposit/list";
	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {

		List<MapEntity> types = new ArrayList<>();
		types.add(new MapEntity("recharge","充值"));
		types.add(new MapEntity("payment","支付"));
		types.add(new MapEntity("refunds","退款"));
		model.addAttribute("types",types);

		model.addAttribute("members",memberService.findAll());

		model.addAttribute("payments",paymentService.findAll());

		model.addAttribute("refundss",refundsService.findAll());

		return "/admin/deposit/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(Deposit deposit, Long paymentId, Long memberId, Long refundsId){
		Deposit entity = new Deposit();	

		entity.setCreateDate(deposit.getCreateDate());

		entity.setModifyDate(deposit.getModifyDate());

		entity.setBalance(deposit.getBalance());

		entity.setCredit(deposit.getCredit());

		entity.setDebit(deposit.getDebit());

		entity.setMemo(deposit.getMemo());

		entity.setOperator(deposit.getOperator());

		entity.setType(deposit.getType());

		entity.setMember(memberService.find(memberId));

		entity.setPayment(paymentService.find(paymentId));

		entity.setRefunds(refundsService.find(refundsId));

		entity.setDeleted(deposit.getDeleted());
		
		if (!isValid(entity, Save.class)) {
            return Message.error("admin.data.valid");
        }
        try {
            depositService.save(entity);
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
            depositService.delete(ids);
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

		List<MapEntity> types = new ArrayList<>();
		types.add(new MapEntity("recharge","充值"));
		types.add(new MapEntity("payment","支付"));
		types.add(new MapEntity("refunds","退款"));
		model.addAttribute("types",types);

		model.addAttribute("members",memberService.findAll());

		model.addAttribute("payments",paymentService.findAll());

		model.addAttribute("refundss",refundsService.findAll());

		model.addAttribute("data",depositService.find(id));

		return "/admin/deposit/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(Deposit deposit, Long paymentId, Long memberId, Long refundsId){
		Deposit entity = depositService.find(deposit.getId());
		
		entity.setCreateDate(deposit.getCreateDate());

		entity.setModifyDate(deposit.getModifyDate());

		entity.setBalance(deposit.getBalance());

		entity.setCredit(deposit.getCredit());

		entity.setDebit(deposit.getDebit());

		entity.setMemo(deposit.getMemo());

		entity.setOperator(deposit.getOperator());

		entity.setType(deposit.getType());

		entity.setMember(memberService.find(memberId));

		entity.setPayment(paymentService.find(paymentId));

		entity.setRefunds(refundsService.find(refundsId));

		entity.setDeleted(deposit.getDeleted());
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            depositService.update(entity);
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
	public Message list(Date beginDate, Date endDate, Deposit.Type type, Pageable pageable, ModelMap model) {
		ArrayList<Filter> filters = (ArrayList<Filter>) pageable.getFilters();
		if (type!=null) {
			Filter typeFilter = new Filter("type", Filter.Operator.eq, type);
			filters.add(typeFilter);
		}

		Page<Deposit> page = depositService.findPage(beginDate,endDate,pageable);
		return Message.success(PageModel.bind(page), "admin.list.success");
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

		model.addAttribute("payment",paymentService.find(id));
		return "/admin/deposit/view/paymentView";
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
		return "/admin/deposit/view/memberView";
	}


	/**
	 * 退款单视图
	 */
	@RequestMapping(value = "/refundsView", method = RequestMethod.GET)
	public String refundsView(Long id, ModelMap model) {
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

		model.addAttribute("refunds",refundsService.find(id));
		return "/admin/deposit/view/refundsView";
	}



}