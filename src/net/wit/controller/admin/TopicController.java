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
import net.wit.entity.Topic;
import net.wit.service.TopicService;

import java.util.*;

import net.wit.*;

import net.wit.entity.*;
import net.wit.service.*;
import net.wit.controller.admin.model.*;



/**
 * @ClassName: TopicController
 * @author 降魔战队
 * @date 2017-10-11 15:37:16
 */
 
@Controller("adminTopicController")
@RequestMapping("/admin/topic")
public class TopicController extends BaseController {
	@Resource(name = "topicServiceImpl")
	private TopicService topicService;
	
	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "templateServiceImpl")
	private TemplateService templateService;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "categoryServiceImpl")
	private CategoryService categoryService;

	@Resource(name = "occupationServiceImpl")
	private OccupationService occupationService;

	@Resource(name = "tagServiceImpl")
	private TagService tagService;

	@Resource(name = "adminServiceImpl")
	private AdminService adminService;

	@Resource(name = "enterpriseServiceImpl")
	private EnterpriseService enterpriseService;

	/**
	 * 主页
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model) {

		List<MapEntity> statuss = new ArrayList<>();
		statuss.add(new MapEntity("waiting","等待"));
		statuss.add(new MapEntity("success","通过"));
		statuss.add(new MapEntity("failure","驳回"));
		model.addAttribute("statuss",statuss);

		List<MapEntity> types = new ArrayList<>();
		types.add(new MapEntity("company","企业"));
		types.add(new MapEntity("individual","个体"));
		types.add(new MapEntity("personal","个人"));
		types.add(new MapEntity("student","学生"));
		model.addAttribute("types",types);

		//model.addAttribute("areas",areaService.findAll());

		//model.addAttribute("categorys",categoryService.findAll());

		//model.addAttribute("members",memberService.findAll());

		//model.addAttribute("tags",tagService.findList(Tag.Type.topic));

		//model.addAttribute("templates",templateService.findList(Template.Type.topic));

		return "/admin/topic/list";
	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {

		List<MapEntity> statuss = new ArrayList<>();
		statuss.add(new MapEntity("waiting","等待"));
		statuss.add(new MapEntity("success","通过"));
		statuss.add(new MapEntity("failure","驳回"));
		model.addAttribute("statuss",statuss);

		List<MapEntity> types = new ArrayList<>();
		types.add(new MapEntity("company","企业"));
		types.add(new MapEntity("individual","个体"));
		types.add(new MapEntity("personal","个人"));
		types.add(new MapEntity("student","学生"));
		model.addAttribute("types",types);

//		model.addAttribute("areas",areaService.findAll());
//
		model.addAttribute("categorys",categoryService.findAll());
//
//		model.addAttribute("members",memberService.findAll());
//
		model.addAttribute("tags",tagService.findList(Tag.Type.topic));

		model.addAttribute("templates",templateService.findList(Template.Type.topic));

		return "/admin/topic/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(Topic topic, Long areaId, Long templateId, Long memberId, Long categoryId){
		Topic entity = new Topic();	

		entity.setAddress(topic.getAddress());

		entity.setBrokerage(topic.getBrokerage());

		entity.setExpire(topic.getExpire());

		entity.setName(topic.getName());

		entity.setStatus(topic.getStatus());

		entity.setType(topic.getType());

		entity.setRanking(0L);

		entity.setArea(areaService.find(areaId));

		entity.setCategory(categoryService.find(categoryId));

		entity.setMember(memberService.find(memberId));

		entity.setTemplate(templateService.find(templateId));
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            topicService.save(entity);
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
            topicService.delete(ids);
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
		statuss.add(new MapEntity("waiting","等待"));
		statuss.add(new MapEntity("success","通过"));
		statuss.add(new MapEntity("failure","驳回"));
		model.addAttribute("statuss",statuss);

		List<MapEntity> types = new ArrayList<>();
		types.add(new MapEntity("company","企业"));
		types.add(new MapEntity("individual","个体"));
		types.add(new MapEntity("personal","个人"));
		types.add(new MapEntity("student","学生"));
		model.addAttribute("types",types);
//
//		model.addAttribute("areas",areaService.findAll());

		model.addAttribute("categorys",categoryService.findAll());

		//model.addAttribute("members",memberService.findAll());

		model.addAttribute("tags",tagService.findList(Tag.Type.topic));

		model.addAttribute("templates",templateService.findList(Template.Type.topic));

		model.addAttribute("data",topicService.find(id));

		return "/admin/topic/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(Topic topic, Long areaId, Long templateId, Long memberId, Long categoryId){

		Topic entity = topicService.find(topic.getId());

		TopicConfig topicConfig = entity.getConfig();

		entity.setAddress(topic.getAddress());

		entity.setBrokerage(topic.getBrokerage());

		entity.setPaybill(topic.getPaybill());

		entity.setExpire(topic.getExpire());

		entity.setName(topic.getName());

		entity.setStatus(topic.getStatus());

		entity.setType(topic.getType());

		if(areaId != null){
			entity.setArea(areaService.find(areaId));
		}

		if(categoryId != null){
			entity.setCategory(categoryService.find(categoryId));
		}

		if(memberId != null){
			entity.setMember(memberService.find(memberId));
		}

		if(templateId != null){
			entity.setTemplate(templateService.find(templateId));
		}

		topicConfig.setAppetAppId(topic.getConfig().getAppetAppId());

		topicConfig.setAppetAppSerect(topic.getConfig().getAppetAppSerect());

		topicConfig.setWxAppId(topic.getConfig().getWxAppId());

		topicConfig.setWxAppSerect(topic.getConfig().getWxAppSerect());

		topicConfig.setWxPartner(topic.getConfig().getWxPartner());

		topicConfig.setWxkey(topic.getConfig().getWxkey());

		entity.setConfig(topicConfig);

		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            topicService.update(entity);
            Member member = topic.getMember();
            Admin admin = adminService.findByMember(member);
            if (admin!=null && admin.getEnterprise()!=null) {
            	Enterprise enterprise = admin.getEnterprise();
            	if (enterprise!=null) {
            		enterprise.setName(topic.getName());
            		if (topic.getStatus().equals(Topic.Status.success)) {
            			enterprise.setStatus(Enterprise.Status.success);
					} else
					if (topic.getStatus().equals(Topic.Status.failure)) {
						enterprise.setStatus(Enterprise.Status.failure);
					} else {
						enterprise.setStatus(Enterprise.Status.waiting);
					}
					enterpriseService.update(enterprise);
				}
			}

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
	public Message list(Date beginDate, Date endDate, Topic.Status status, Topic.Type type, Pageable pageable,String searchValue, ModelMap model) {
		ArrayList<Filter> filters = (ArrayList<Filter>) pageable.getFilters();
		if (status!=null) {
			Filter statusFilter = new Filter("status", Filter.Operator.eq, status);
			filters.add(statusFilter);
		}
		if (type!=null) {
			Filter typeFilter = new Filter("type", Filter.Operator.eq, type);
			filters.add(typeFilter);
		}

		if(searchValue!=null){
			Filter mediaTypeFilter = new Filter("name", Filter.Operator.like, "%"+searchValue+"%");
			filters.add(mediaTypeFilter);
		}
		Page<Topic> page = topicService.findPage(beginDate,endDate,pageable);
		return Message.success(PageBlock.bind(page), "admin.list.success");
	}
	
	
	/**
	 * 地区视图
	 */
	@RequestMapping(value = "/areaView", method = RequestMethod.GET)
	public String areaView(Long id, ModelMap model) {


		model.addAttribute("area",areaService.find(id));
		return "/admin/topic/view/areaView";
	}


	/**
	 * 模版管理视图
	 */
	@RequestMapping(value = "/templateView", method = RequestMethod.GET)
	public String templateView(Long id, ModelMap model) {
		List<MapEntity> types = new ArrayList<>();
		types.add(new MapEntity("article","文章"));
		types.add(new MapEntity("product","商品"));
		model.addAttribute("types",types);

		model.addAttribute("template",templateService.find(id));
		return "/admin/topic/view/templateView";
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
		return "/admin/topic/view/memberView";
	}


	/**
	 * 行业分类视图
	 */
	@RequestMapping(value = "/categoryView", method = RequestMethod.GET)
	public String categoryView(Long id, ModelMap model) {
		List<MapEntity> statuss = new ArrayList<>();
		statuss.add(new MapEntity("enabled","开启"));
		statuss.add(new MapEntity("disabled","关闭"));
		model.addAttribute("statuss",statuss);

		model.addAttribute("category",categoryService.find(id));
		return "/admin/topic/view/categoryView";
	}



}