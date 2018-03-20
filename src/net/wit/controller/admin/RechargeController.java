package net.wit.controller.admin;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;

import javax.annotation.Resource;
import net.wit.Filter;
import net.wit.Message;
import net.wit.Pageable;

import net.wit.plat.unspay.UnsPay;
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
import net.wit.entity.Recharge;
import net.wit.service.RechargeService;

import java.util.*;

import net.wit.*;

import net.wit.entity.*;
import net.wit.service.*;
import net.wit.controller.admin.model.*;



/**
 * @ClassName: RechargeController
 * @author 降魔战队
 * @date 2018-2-1 14:1:35
 */
 
@Controller("adminRechargeController")
@RequestMapping("/admin/recharge")
public class RechargeController extends BaseController {
	@Resource(name = "rechargeServiceImpl")
	private RechargeService rechargeService;
	
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "adminServiceImpl")
	private AdminService adminService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "topicServiceImpl")
	private TopicService topicService;

	@Resource(name = "occupationServiceImpl")
	private OccupationService occupationService;

	@Resource(name = "tagServiceImpl")
	private TagService tagService;

	@Resource(name = "snServiceImpl")
	private SnService snService;

	/**
	 * 主页
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model) {

		List<MapEntity> methods = new ArrayList<>();
		methods.add(new MapEntity("online","在线充值"));
		methods.add(new MapEntity("offline","线下充值"));
		model.addAttribute("methods",methods);

		List<MapEntity> statuss = new ArrayList<>();
		statuss.add(new MapEntity("waiting","等待充值"));
		statuss.add(new MapEntity("confirmed","提交充值"));
		statuss.add(new MapEntity("success","充值成功"));
		statuss.add(new MapEntity("failure","充值失败"));
		model.addAttribute("statuss",statuss);

		return "/admin/recharge/list";
	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {

		List<MapEntity> methods = new ArrayList<>();
		methods.add(new MapEntity("online","在线充值"));
		methods.add(new MapEntity("offline","线下充值"));
		model.addAttribute("methods",methods);

		return "/admin/recharge/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(Recharge recharge, Long memberId){
		Admin admin = adminService.getCurrent();
		Recharge entity = new Recharge();

		entity.setSn(snService.generate(Sn.Type.recharge));

		entity.setAmount(recharge.getAmount());

		entity.setFee(new BigDecimal(0));

		entity.setMemo(recharge.getMemo());

		entity.setMethod(Recharge.Method.offline);

		entity.setOperator(admin.getName());

		entity.setStatus(Recharge.Status.success);

		entity.setVoucher(recharge.getVoucher());

		entity.setMember(memberService.find(memberId));

		entity.setTransferDate(new Date());

		entity.setAdmin(admin);

		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            rechargeService.submit(entity);
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
            rechargeService.delete(ids);
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
		List<MapEntity> statuss = new ArrayList<>();
		statuss.add(new MapEntity("waiting","等待充值"));
		statuss.add(new MapEntity("confirmed","提交充值"));
		statuss.add(new MapEntity("success","充值成功"));
		statuss.add(new MapEntity("failure","充值失败"));
		model.addAttribute("statuss",statuss);

		model.addAttribute("data",rechargeService.find(id));

		return "/admin/recharge/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(Recharge recharge, Long memberId){
		Recharge entity = rechargeService.find(recharge.getId());
		try {

				String resp = UnsPay.query(entity.getSn());
				if ("00".equals(resp)) {
					rechargeService.handle(entity);
					return Message.success(entity,"充值成功");
				} else
				if ("20".equals(resp)) {
					rechargeService.close(entity);
					return Message.success(entity,"充值失败,款项退回账号");
				} else
				if ("10".equals(resp)) {
					return Message.success(entity,"正在处理中");
				} else {
					return Message.error("查询失败");
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
	public Message list(Date beginDate, Date endDate, Recharge.Method method, Recharge.Status status, Pageable pageable, ModelMap model) {	
		ArrayList<Filter> filters = (ArrayList<Filter>) pageable.getFilters();
		if (method!=null) {
			Filter methodFilter = new Filter("method", Filter.Operator.eq, method);
			filters.add(methodFilter);
		}
		if (status!=null) {
			Filter statusFilter = new Filter("status", Filter.Operator.eq, status);
			filters.add(statusFilter);
		}

		Page<Recharge> page = rechargeService.findPage(beginDate,endDate,pageable);
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

		model.addAttribute("topics",topicService.findAll());

		model.addAttribute("tags",tagService.findAll());

		model.addAttribute("member",memberService.find(id));
		return "/admin/recharge/view/memberView";
	}

	/**
	 * 通过会员手机号调取会员信息
	 */
	@RequestMapping(value = "/getMemberInfo", method = RequestMethod.GET)
	@ResponseBody
	public Message getMemberInfo(String phone){
		try {
			Member member = memberService.findByMobile(phone);
			if(member != null){
				List<MapEntity> memberinfo = new ArrayList<>();
				memberinfo.add(new MapEntity("name",member.getName()));
				memberinfo.add(new MapEntity("mobile",member.getMobile()));
				memberinfo.add(new MapEntity("email",member.getUsername()));
				memberinfo.add(new MapEntity("id",member.getId().toString()));
				return Message.success(memberinfo,"admin.update.success");
			}else{
				return Message.error("admin.update.error");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Message.error("admin.update.error");
		}
	}

	/**
	 * 申请手动转账
	 */
	@RequestMapping(value = "/manualTransfer", method = RequestMethod.GET)
	public String manualTransfer(ModelMap model,Long id) {
		model.addAttribute("data",rechargeService.find(id));

		return "/admin/recharge/view/manualTransfer";
	}

	/**
	 * 提交手动转账
	 */
	@RequestMapping(value = "/manualTransferSave", method = RequestMethod.POST)
	@ResponseBody
	public Message manualTransferSave(String voucher, BigDecimal amount, Long Id){
		if("".equals(voucher)){
			return Message.error("凭证号不能为空!");
		}
		Recharge entity = rechargeService.find(Id);
		if(amount.compareTo(entity.getAmount()) != 0){
			return Message.error("充值金额不正确,请重新填写!");
		}
		Admin admin = adminService.getCurrent();
		Member member = admin.getMember();
		entity.setVoucher(voucher);
		entity.setStatus(Recharge.Status.confirmed);
		if(member == null){
			entity.setOperator("系统操作员");
		}else{
			entity.setOperator(member.getName());
		}
		try {
			rechargeService.handle(entity);
			return Message.success(entity,"充值成功");
		} catch (Exception e) {
			e.printStackTrace();
			return Message.error(e.getMessage());
		}
	}

}