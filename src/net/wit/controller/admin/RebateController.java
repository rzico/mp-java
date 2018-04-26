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
import net.wit.entity.Rebate;
import net.wit.service.RebateService;

import java.util.*;

import net.wit.*;

import net.wit.entity.*;
import net.wit.service.*;
import net.wit.controller.admin.model.*;



/**
 * @ClassName: RebateController
 * @author 降魔战队
 * @date 2018-4-24 20:58:9
 */
 
@Controller("adminRebateController")
@RequestMapping("/admin/rebate")
public class RebateController extends BaseController {
	@Resource(name = "rebateServiceImpl")
	private RebateService rebateService;
	
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "orderServiceImpl")
	private OrderService orderService;

	@Resource(name = "enterpriseServiceImpl")
	private EnterpriseService enterpriseService;

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

	@Resource(name = "hostServiceImpl")
	private HostService hostService;



	/**
	 * 主页
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model) {

		model.addAttribute("enterprises",enterpriseService.findAll());

		model.addAttribute("members",memberService.findAll());

		model.addAttribute("orderss",orderService.findAll());

		return "/admin/rebate/list";
	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {

		model.addAttribute("enterprises",enterpriseService.findAll());

		model.addAttribute("members",memberService.findAll());

		model.addAttribute("orderss",orderService.findAll());

		return "/admin/rebate/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(Rebate rebate, Long memberId, Long ordersId, Long enterpriseId){
		Rebate entity = new Rebate();	

		entity.setCreateDate(rebate.getCreateDate());

		entity.setModifyDate(rebate.getModifyDate());

		entity.setAmount(rebate.getAmount());

		entity.setDirect(rebate.getDirect());

		entity.setIndirect(rebate.getIndirect());

		entity.setEnterprise(enterpriseService.find(enterpriseId));

		entity.setMember(memberService.find(memberId));

		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            rebateService.save(entity);
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
            rebateService.delete(ids);
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

		model.addAttribute("enterprises",enterpriseService.findAll());

		model.addAttribute("members",memberService.findAll());

		model.addAttribute("orderss",orderService.findAll());

		model.addAttribute("data",rebateService.find(id));

		return "/admin/rebate/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(Rebate rebate, Long memberId, Long ordersId, Long enterpriseId){
		Rebate entity = rebateService.find(rebate.getId());
		
		entity.setCreateDate(rebate.getCreateDate());

		entity.setModifyDate(rebate.getModifyDate());

		entity.setAmount(rebate.getAmount());

		entity.setDirect(rebate.getDirect());

		entity.setIndirect(rebate.getIndirect());

		entity.setEnterprise(enterpriseService.find(enterpriseId));

		entity.setMember(memberService.find(memberId));

		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            rebateService.update(entity);
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

		Page<Rebate> page = rebateService.findPage(beginDate,endDate,pageable);
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

		List<MapEntity> vips = new ArrayList<>();
		vips.add(new MapEntity("vip1","vip1"));
		vips.add(new MapEntity("vip2","vip2"));
		vips.add(new MapEntity("vip3","vip3"));
		model.addAttribute("vips",vips);

		model.addAttribute("agents",enterpriseService.findAll());

		model.addAttribute("operates",enterpriseService.findAll());

		model.addAttribute("personals",enterpriseService.findAll());

		model.addAttribute("tags",tagService.findAll());

		model.addAttribute("member",memberService.find(id));
		return "/admin/rebate/view/memberView";
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

		model.addAttribute("promoters",memberService.findAll());

		model.addAttribute("partners",memberService.findAll());

		model.addAttribute("order",orderService.find(id));
		return "/admin/rebate/view/orderView";
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

		List<MapEntity> statuss = new ArrayList<>();
		statuss.add(new MapEntity("waiting","待审核"));
		statuss.add(new MapEntity("success","已审核"));
		statuss.add(new MapEntity("failure","已关闭"));
		model.addAttribute("statuss",statuss);

		model.addAttribute("hosts",hostService.findAll());

		model.addAttribute("enterprise",enterpriseService.find(id));
		return "/admin/rebate/view/enterpriseView";
	}



}