package net.wit.controller.component;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.wit.Filter;
import net.wit.Message;
import net.wit.Pageable;

import net.wit.controller.model.AppletSowingModel;
import net.wit.entity.model.AppletSowingMapModel;
import net.wit.util.JsonUtils;
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
 
@Controller("componentAppletSowingMapController")
@RequestMapping("/component/appletSowingMap")
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
	 * 保存文章信息
	 */
	@RequestMapping(value = "/submit", method = RequestMethod.POST)
	@ResponseBody
	public Message submit(String body, HttpServletRequest request) {

		Member member = memberService.getCurrent();
		if(member == null) return Message.error(Message.SESSION_INVAILD);
		AppletSowingModel appletSowingModel = JsonUtils.toObject(body, AppletSowingModel.class);
		List<AppletSowingMap> appletSowingMaps = new ArrayList<>();
		List<AppletSowingMap> oldAppletSowingMaps = member.getTopic().getAppletSowingMaps();
		int oldLen = oldAppletSowingMaps.size();

		for (int i = 0; i < oldLen; i++){
			appletSowingMapService.delete(oldAppletSowingMaps.get(i));
		}
			if(appletSowingModel != null && appletSowingModel.getAppletSowingMapModels() != null){
				int len = appletSowingModel.getAppletSowingMapModels().size();

				for (int i = 0; i < len; i++){
					AppletSowingMap appletSowingMap = new AppletSowingMap();
					appletSowingMap.setAction(appletSowingModel.getAppletSowingMapModels().get(i).getAction());
					appletSowingMap.setActionId(appletSowingModel.getAppletSowingMapModels().get(i).getActionId());
					appletSowingMap.setFrontcover(appletSowingModel.getAppletSowingMapModels().get(i).getFrontcover());
					appletSowingMap.setUrl(appletSowingModel.getAppletSowingMapModels().get(i).getUrl());
					appletSowingMap.setOrders(appletSowingModel.getAppletSowingMapModels().get(i).getOrders());
					appletSowingMap.setTopic(member.getTopic());
					appletSowingMapService.save(appletSowingMap);
					appletSowingMaps.add(appletSowingMap);
				}
				member.getTopic().setAppletSowingMaps(appletSowingMaps);
				memberService.update(member);
				return Message.success("admin.save.success");
			}

		//回滚数据
		for (int i = 0; i < oldLen; i++){
			appletSowingMapService.save(oldAppletSowingMaps.get(i));
		}
		member.getTopic().setAppletSowingMaps(oldAppletSowingMaps);
		memberService.update(member);
			return Message.error("admin.save.error");

	}

	/**
     * 添加
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(AppletSowingMap appletSowingMap){
		Member member = memberService.getCurrent();
		if(member == null) return Message.error(Message.SESSION_INVAILD);

		AppletSowingMap entity = new AppletSowingMap();	

		entity.setAction(appletSowingMap.getAction());

		entity.setActionId(appletSowingMap.getActionId() == null ? 0 : appletSowingMap.getActionId());

		entity.setFrontcover(appletSowingMap.getFrontcover());

		entity.setUrl(appletSowingMap.getUrl());

		entity.setTopic(member.getTopic());
		
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
	public Message list(Long memberId) {
		Member member = memberService.find(memberId);//获取当前会员
		List<AppletSowingMapModel> appletSowingMapModels = new ArrayList<>();
		if(member == null) return Message.error("没有找到该小程序账号id");
		Topic topic = member.getTopic();
		if(topic != null){
			List<AppletSowingMap> appletSowingMaps = topic.getAppletSowingMaps();
			if(appletSowingMaps != null && appletSowingMaps.size() > 0){
				for (AppletSowingMap item : appletSowingMaps){
					AppletSowingMapModel appletSowingMapModel = AppletSowingMapModel.bind(item);
					appletSowingMapModels.add(appletSowingMapModel);
				}
				return Message.success(appletSowingMapModels, "admin.list.success");
			}
		}
		return Message.success(appletSowingMapModels, "admin.list.success");
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