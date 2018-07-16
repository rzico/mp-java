package net.wit.controller.admin;

import java.util.Date;

import javax.annotation.Resource;
import net.wit.Filter;
import net.wit.Message;
import net.wit.Pageable;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.wit.entity.CounselorOrder;
import net.wit.service.CounselorOrderService;

import java.util.*;

import net.wit.*;

import net.wit.service.*;


/**
 * @ClassName: CounselorOrderController
 * @author 降魔战队
 * @date 2018-7-13 14:39:5
 */
 
@Controller("adminCounselorOrderController")
@RequestMapping("/admin/counselorOrder")
public class CounselorOrderController extends BaseController {
	@Resource(name = "counselorOrderServiceImpl")
	private CounselorOrderService counselorOrderService;
	
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "enterpriseServiceImpl")
	private EnterpriseService enterpriseService;

	@Resource(name = "counselorServiceImpl")
	private CounselorService counselorService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "topicServiceImpl")
	private TopicService topicService;

	@Resource(name = "occupationServiceImpl")
	private OccupationService occupationService;

	@Resource(name = "tagServiceImpl")
	private TagService tagService;

	@Resource(name = "hostServiceImpl")
	private HostService hostService;



	/**
	 * 主页
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model) {

		List<MapEntity> statuss = new ArrayList<>();
		statuss.add(new MapEntity("enabled","开启"));
		statuss.add(new MapEntity("disabled","关闭"));
		model.addAttribute("statuss",statuss);

		model.addAttribute("counselors",counselorService.findAll());

		model.addAttribute("enterprises",enterpriseService.findAll());

		model.addAttribute("members",memberService.findAll());

		return "/admin/subscribe/list";
	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {

		List<MapEntity> statuss = new ArrayList<>();
		statuss.add(new MapEntity("enabled","开启"));
		statuss.add(new MapEntity("disabled","关闭"));
		model.addAttribute("statuss",statuss);

		model.addAttribute("counselors",counselorService.findAll());

		model.addAttribute("enterprises",enterpriseService.findAll());

		model.addAttribute("members",memberService.findAll());

		return "/admin/subscribe/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(CounselorOrder subscribe, Long memberId, Long enterpriseId, Long counselorId){
		CounselorOrder entity = new CounselorOrder();

		entity.setCreateDate(subscribe.getCreateDate());

		entity.setModifyDate(subscribe.getModifyDate());

		entity.setMobile(subscribe.getMobile());

		entity.setName(subscribe.getName());

		entity.setSex(subscribe.getSex());

		entity.setStatus(subscribe.getStatus());

		entity.setWorry(subscribe.getWorry());

		entity.setCounselor(counselorService.find(counselorId));

		entity.setEnterprise(enterpriseService.find(enterpriseId));

		entity.setMember(memberService.find(memberId));
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
			counselorOrderService.save(entity);
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
			counselorOrderService.delete(ids);
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
		statuss.add(new MapEntity("enabled","开启"));
		statuss.add(new MapEntity("disabled","关闭"));
		model.addAttribute("statuss",statuss);

		model.addAttribute("counselors",counselorService.findAll());

		model.addAttribute("enterprises",enterpriseService.findAll());

		model.addAttribute("members",memberService.findAll());

		model.addAttribute("data",counselorOrderService.find(id));

		return "/admin/subscribe/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(CounselorOrder subscribe, Long memberId, Long enterpriseId, Long counselorId){
		CounselorOrder entity = counselorOrderService.find(subscribe.getId());
		
		entity.setCreateDate(subscribe.getCreateDate());

		entity.setModifyDate(subscribe.getModifyDate());

		entity.setMobile(subscribe.getMobile());

		entity.setName(subscribe.getName());

		entity.setSex(subscribe.getSex());

		entity.setStatus(subscribe.getStatus());

		entity.setWorry(subscribe.getWorry());

		entity.setCounselor(counselorService.find(counselorId));

		entity.setEnterprise(enterpriseService.find(enterpriseId));

		entity.setMember(memberService.find(memberId));
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
			counselorOrderService.update(entity);
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
	public Message list(Date beginDate, Date endDate, CounselorOrder.Status status, Pageable pageable, ModelMap model) {
		ArrayList<Filter> filters = (ArrayList<Filter>) pageable.getFilters();
		if (status!=null) {
			Filter statusFilter = new Filter("status", Filter.Operator.eq, status);
			filters.add(statusFilter);
		}

		Page<CounselorOrder> page = counselorOrderService.findPage(beginDate,endDate,pageable);
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
		return "/admin/subscribe/view/memberView";
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
		return "/admin/subscribe/view/enterpriseView";
	}


	/**
	 * Counselor视图
	 */
	@RequestMapping(value = "/counselorView", method = RequestMethod.GET)
	public String counselorView(Long id, ModelMap model) {
		List<MapEntity> statuss = new ArrayList<>();
		statuss.add(new MapEntity("enabled","开启"));
		statuss.add(new MapEntity("disabled","关闭"));
		model.addAttribute("statuss",statuss);

		model.addAttribute("enterprises",enterpriseService.findAll());

		model.addAttribute("members",memberService.findAll());

		model.addAttribute("counselor",counselorService.find(id));
		return "/admin/subscribe/view/counselorView";
	}



}