package net.wit.controller.component;

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
import net.wit.entity.AppletSowingMap;
import net.wit.service.AppletSowingMapService;

import java.util.*;

import net.wit.*;

import net.wit.entity.*;
import net.wit.service.*;
import net.wit.controller.admin.model.*;



/**
 * @ClassName: AppletSowingMapController
 * @author 降魔战队
 * @date 2018-5-29 16:52:28
 */
 
@Controller("adminAppletSowingMapController")
@RequestMapping("/admin/appletSowingMap")
public class AppletSowingMapController extends BaseController {
	@Resource(name = "appletSowingMapServiceImpl")
	private AppletSowingMapService appletSowingMapService;
	
	@Resource(name = "topicServiceImpl")
	private TopicService topicService;

	@Resource(name = "topicCardServiceImpl")
	private TopicCardService topicCardService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "templateServiceImpl")
	private TemplateService templateService;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "categoryServiceImpl")
	private CategoryService categoryService;



	/**
	 * 主页
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model) {

		List<MapEntity> actions = new ArrayList<>();
		actions.add(new MapEntity("OPENARTICLE","打开文章"));
		actions.add(new MapEntity("OPENPRODUCT","打开产品"));
		actions.add(new MapEntity("OPENWEBVIEW","打开网页"));
		model.addAttribute("actions",actions);

		model.addAttribute("topics",topicService.findAll());

		return "/admin/appletSowingMap/list";
	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {

		List<MapEntity> actions = new ArrayList<>();
		actions.add(new MapEntity("OPENARTICLE","打开文章"));
		actions.add(new MapEntity("OPENPRODUCT","打开产品"));
		actions.add(new MapEntity("OPENWEBVIEW","打开网页"));
		model.addAttribute("actions",actions);

		model.addAttribute("topics",topicService.findAll());

		return "/admin/appletSowingMap/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(AppletSowingMap appletSowingMap, Long topicId){
		AppletSowingMap entity = new AppletSowingMap();	

		entity.setCreateDate(appletSowingMap.getCreateDate());

		entity.setModifyDate(appletSowingMap.getModifyDate());

		entity.setAction(appletSowingMap.getAction());

		entity.setActionId(appletSowingMap.getActionId() == null ? 0 : appletSowingMap.getActionId());

		entity.setFrontcover(appletSowingMap.getFrontcover());

		entity.setUrl(appletSowingMap.getUrl());

		entity.setTopic(topicService.find(topicId));
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            appletSowingMapService.save(entity);
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
            appletSowingMapService.delete(ids);
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

		List<MapEntity> actions = new ArrayList<>();
		actions.add(new MapEntity("OPENARTICLE","打开文章"));
		actions.add(new MapEntity("OPENPRODUCT","打开产品"));
		actions.add(new MapEntity("OPENWEBVIEW","打开网页"));
		model.addAttribute("actions",actions);

		model.addAttribute("topics",topicService.findAll());

		model.addAttribute("data",appletSowingMapService.find(id));

		return "/admin/appletSowingMap/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(AppletSowingMap appletSowingMap, Long topicId){
		AppletSowingMap entity = appletSowingMapService.find(appletSowingMap.getId());
		
		entity.setCreateDate(appletSowingMap.getCreateDate());

		entity.setModifyDate(appletSowingMap.getModifyDate());

		entity.setAction(appletSowingMap.getAction());

		entity.setActionId(appletSowingMap.getActionId() == null ? 0 : appletSowingMap.getActionId());

		entity.setFrontcover(appletSowingMap.getFrontcover());

		entity.setUrl(appletSowingMap.getUrl());

		entity.setTopic(topicService.find(topicId));
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            appletSowingMapService.update(entity);
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
	public Message list(Date beginDate, Date endDate, AppletSowingMap.ACTION action, Pageable pageable, ModelMap model) {
		ArrayList<Filter> filters = (ArrayList<Filter>) pageable.getFilters();
		if (action!=null) {
			Filter actionFilter = new Filter("action", Filter.Operator.eq, action);
			filters.add(actionFilter);
		}

		Page<AppletSowingMap> page = appletSowingMapService.findPage(beginDate,endDate,pageable);
		Topic topic = null;
		topic.getAppletSowingMaps();
		return Message.success(PageBlock.bind(page), "admin.list.success");
	}
	
	
	/**
	 * 专题管理视图
	 */
	@RequestMapping(value = "/topicView", method = RequestMethod.GET)
	public String topicView(Long id, ModelMap model) {
		List<MapEntity> statuss = new ArrayList<>();
		statuss.add(new MapEntity("waiting","等待"));
		statuss.add(new MapEntity("success","通过"));
		statuss.add(new MapEntity("failure","驳回"));
		model.addAttribute("statuss",statuss);

		List<MapEntity> types = new ArrayList<>();
		types.add(new MapEntity("company","公司/企业"));
		types.add(new MapEntity("individual","个体工商户"));
		types.add(new MapEntity("personal","个人"));
		types.add(new MapEntity("student","学生"));
		model.addAttribute("types",types);

		model.addAttribute("areas",areaService.findAll());

		model.addAttribute("categorys",categoryService.findAll());

		model.addAttribute("templates",templateService.findAll());

		model.addAttribute("topicCards",topicCardService.findAll());

		List<MapEntity> conditions = new ArrayList<>();
		conditions.add(new MapEntity("暂停服务","outOfService"));
		conditions.add(new MapEntity("未上传","notUploaded"));
		conditions.add(new MapEntity("待审核","audit"));
		conditions.add(new MapEntity("已上线","online"));
		conditions.add(new MapEntity("已通过","passed"));
		model.addAttribute("conditions",conditions);

		List<MapEntity> estates = new ArrayList<>();
		estates.add(new MapEntity("暂停服务","outOfService"));
		estates.add(new MapEntity("未上传","notUploaded"));
		estates.add(new MapEntity("待审核","audit"));
		estates.add(new MapEntity("已上线","online"));
		estates.add(new MapEntity("已通过","passed"));
		model.addAttribute("estates",estates);

		model.addAttribute("topic",topicService.find(id));
		return "/admin/appletSowingMap/view/topicView";
	}



}