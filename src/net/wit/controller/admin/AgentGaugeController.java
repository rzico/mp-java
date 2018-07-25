package net.wit.controller.admin;

import net.wit.*;
import net.wit.Message;
import net.wit.entity.*;
import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @ClassName: AgentGaugeController
 * @author 降魔战队
 * @date 2018-2-24 14:17:24
 */
 
@Controller("adminAgentGaugeController")
@RequestMapping("/admin/agentGauge")
public class AgentGaugeController extends BaseController {

	@Resource(name = "agentGaugeServiceImpl")
	private AgentGaugeService agentGaugeService;
	
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "gaugeServiceImpl")
	private GaugeService gaugeService;

	@Resource(name = "agentCategoryServiceImpl")
	private AgentCategoryService agentCategoryService;

	@Resource(name = "enterpriseServiceImpl")
	private EnterpriseService enterpriseService;

	@Resource(name = "tagServiceImpl")
	private TagService tagService;

	@Resource(name = "adminServiceImpl")
	private AdminService adminService;


	/**
	 * 主页
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(Long enterpriseId,ModelMap model) {
		Admin admin = adminService.getCurrent();
		if (enterpriseId==null) {
			enterpriseId = admin.getEnterprise().getId();
		}
		model.addAttribute("enterpriseId",enterpriseId);
        List<Filter> filters = new ArrayList<>();
        filters.add(new Filter("enterprise", Filter.Operator.eq,enterpriseService.find(enterpriseId)));
        List<AgentCategory> agentCategories = agentCategoryService.findList(null,null,filters,null);
		model.addAttribute("agentCategorys",agentCategories);


		model.addAttribute("tags",tagService.findList(Tag.Type.article));

		return "/admin/agentGauge/list";
	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Long enterpriseId,ModelMap model) {
		Admin admin = adminService.getCurrent();
		if (enterpriseId==null) {
			enterpriseId = admin.getEnterprise().getId();
		}
		model.addAttribute("enterpriseId",enterpriseId);
		List<Filter> filters = new ArrayList<>();
		filters.add(new Filter("enterprise", Filter.Operator.eq,enterpriseService.find(enterpriseId)));
		List<AgentCategory> agentCategories = agentCategoryService.findList(null,null,filters,null);
		model.addAttribute("agentCategorys",agentCategories);


		model.addAttribute("tags",tagService.findList(Tag.Type.article));

		return "/admin/agentGauge/add";
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		AgentGauge agentGauge = agentGaugeService.find(id);

		model.addAttribute("enterpriseId",agentGauge.getEnterprise().getId());
		List<Filter> filters = new ArrayList<>();
		filters.add(new Filter("enterprise", Filter.Operator.eq,agentGauge.getEnterprise()));
		List<AgentCategory> agentCategories = agentCategoryService.findList(null,null,filters,null);
		model.addAttribute("agentCategorys",agentCategories);


		model.addAttribute("tags",tagService.findList(Tag.Type.article));
		model.addAttribute("data",agentGauge);

		return "/admin/agentGauge/edit";
	}

	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(Long enterpriseId,Long agentCategoryId, BigDecimal price,Long gaugeId,Integer orders, Long [] tagIds){
		Admin admin = adminService.getCurrent();
		if (enterpriseId==null) {
			enterpriseId = admin.getEnterprise().getId();
		}
		Enterprise enterprise = enterpriseService.find(enterpriseId);
		AgentGauge entity = new AgentGauge();

		Gauge gauge = gaugeService.find(gaugeId);
		if (gauge==null) {
			return Message.error("无效量表编号");
		}
		AgentCategory agentCategory = agentCategoryService.find(agentCategoryId);
		if (agentCategory==null) {
			return Message.error("无效分类");
		}

		entity.setEnterprise(enterprise);

		entity.setOrders(orders);
		entity.setGauge(gauge);
		entity.setAgentCategory(agentCategory);

		entity.setEnterprise(enterprise);
		entity.setTitle(gauge.getTitle());
		entity.setSubTitle(gauge.getSubTitle());
		entity.setThumbnail(gauge.getThumbnail());

		if (price==null) {
			price = gauge.getPrice();
		}
		entity.setPrice(price);

		entity.setTags(tagService.findList(tagIds));

        try {
            agentGaugeService.save(entity);
            return Message.success(entity,"admin.save.success");
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error("admin.save.error");
        }
	}

	/**
	 * 修改
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public Message update(Long id, Long agentCategoryId,BigDecimal price,Integer orders, Long [] tagIds){
		AgentGauge entity = agentGaugeService.find(id);
        Gauge gauge = entity.getGauge();
		AgentCategory agentCategory = agentCategoryService.find(agentCategoryId);
		if (agentCategory==null) {
			return Message.error("无效分类");
		}
		entity.setOrders(orders);
		entity.setTitle(gauge.getTitle());
		entity.setSubTitle(gauge.getSubTitle());
		entity.setThumbnail(gauge.getThumbnail());

		if (price==null) {
			price = gauge.getPrice();
		}
		entity.setPrice(price);

		entity.setPrice(price);

		entity.setTags(tagService.findList(tagIds));

		try {
			agentGaugeService.save(entity);
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
            agentGaugeService.delete(ids);
            return Message.success("admin.delete.success");
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error("admin.delete.error");
        }
    }

	/**
     * 列表
     */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public Message list(Long enterpriseId,Long agentCategoryId,Date beginDate, Date endDate, Pageable pageable, ModelMap model) {
		Admin admin = adminService.getCurrent();
		Enterprise enterprise = enterpriseService.find(enterpriseId);
		if (enterprise==null) {
			enterprise = admin.getEnterprise();
		}

		List<Filter> filters = pageable.getFilters();
		filters.add(new Filter("enterprise", Filter.Operator.eq, enterprise));
		if (agentCategoryId!=null) {
			filters.add(new Filter("agentCategory", Filter.Operator.eq, agentCategoryService.find(agentCategoryId)));
		}
		Page<AgentGauge> page = agentGaugeService.findPage(beginDate,endDate,null,pageable);
		return Message.success(PageBlock.bind(page), "admin.list.success");
	}

}