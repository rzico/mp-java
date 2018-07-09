package net.wit.controller.admin;

import net.wit.*;
import net.wit.entity.AgentCategory;
import net.wit.entity.Enterprise;
import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @ClassName: AgentCategoryController
 * @author 降魔战队
 * @date 2018-2-24 14:17:24
 */
 
@Controller("adminAgentCategoryController")
@RequestMapping("/admin/agentCategory")
public class AgentCategoryController extends BaseController {
	@Resource(name = "agentCategoryServiceImpl")
	private AgentCategoryService agentCategoryService;
	
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "topicServiceImpl")
	private TopicService topicService;

	@Resource(name = "occupationServiceImpl")
	private OccupationService occupationService;

	@Resource(name = "enterpriseServiceImpl")
	private EnterpriseService enterpriseService;

	@Resource(name = "tagServiceImpl")
	private TagService tagService;



	/**
	 * 主页
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(Long enterpriseId,ModelMap model) {
		model.addAttribute("enterpriseId",enterpriseId);
		return "/admin/agentCategory/list";
	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Long enterpriseId,ModelMap model) {
		model.addAttribute("enterpriseId",enterpriseId);
		return "/admin/agentCategory/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(Long enterpriseId,AgentCategory agentCategory, Long parentId, Long memberId){
		Enterprise enterprise = enterpriseService.find(enterpriseId);
		AgentCategory entity = new AgentCategory();

		entity.setOrders(agentCategory.getOrders() == null ? 0 : agentCategory.getOrders());

		entity.setName(agentCategory.getName());

		entity.setEnglish(agentCategory.getEnglish());

		entity.setEnterprise(enterprise);

        try {
            agentCategoryService.save(entity);
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
            agentCategoryService.delete(ids);
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
		model.addAttribute("data",agentCategoryService.find(id));

		return "/admin/agentCategory/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(AgentCategory agentCategory, Long parentId, Long memberId){
		AgentCategory entity = agentCategoryService.find(agentCategory.getId());

		entity.setOrders(agentCategory.getOrders() == null ? 0 : agentCategory.getOrders());

		entity.setName(agentCategory.getName());

		entity.setEnglish(agentCategory.getEnglish());

        try {
            agentCategoryService.update(entity);
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
	public Message list(Long enterpriseId,Date beginDate, Date endDate, Pageable pageable, ModelMap model) {
		Enterprise enterprise = enterpriseService.find(enterpriseId);

		List<Filter> filters = pageable.getFilters();
		filters.add(new Filter("enterprise", Filter.Operator.eq, enterprise));
		Page<AgentCategory> page = agentCategoryService.findPage(beginDate,endDate,pageable);
		return Message.success(PageBlock.bind(page), "admin.list.success");
	}
	
	
	/**
	 * AgentCategory视图
	 */
	@RequestMapping(value = "/agentCategoryView", method = RequestMethod.GET)
	public String agentCategoryView(Long id, ModelMap model) {
		model.addAttribute("members",memberService.findAll());

		model.addAttribute("agentCategory",agentCategoryService.find(id));
		return "/admin/agentCategory/view/agentCategoryView";
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
		return "/admin/agentCategory/view/memberView";
	}



}