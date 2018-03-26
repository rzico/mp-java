package net.wit.controller.admin;

import java.io.File;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;
import net.wit.Filter;
import net.wit.Message;
import net.wit.Pageable;

import net.wit.plat.weixin.util.WeiXinUtils;
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
import net.wit.entity.TopicCard;
import net.wit.service.TopicCardService;

import java.util.*;

import net.wit.*;

import net.wit.entity.*;
import net.wit.service.*;
import net.wit.controller.admin.model.*;



/**
 * @ClassName: TopicCardController
 * @author 降魔战队
 * @date 2017-11-9 12:59:11
 */
 
@Controller("adminTopicCardController")
@RequestMapping("/admin/topicCard")
public class TopicCardController extends BaseController {
	@Resource(name = "topicCardServiceImpl")
	private TopicCardService topicCardService;
	
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



	/**
	 * 主页
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model) {

		List<MapEntity> statuss = new ArrayList<>();
		statuss.add(new MapEntity("waiting","待审核"));
		statuss.add(new MapEntity("failture","审核失败"));
		statuss.add(new MapEntity("success","审核通过"));
		model.addAttribute("statuss",statuss);

		List<MapEntity> colors = new ArrayList<>();
		colors.add(new MapEntity("c1","c1"));
		colors.add(new MapEntity("c2","c2"));
		colors.add(new MapEntity("c3","c3"));
		colors.add(new MapEntity("c4","c4"));
		colors.add(new MapEntity("c5","c5"));
		colors.add(new MapEntity("c6","c6"));
		colors.add(new MapEntity("c7","c7"));
		colors.add(new MapEntity("c8","c8"));
		colors.add(new MapEntity("c9","c9"));
		colors.add(new MapEntity("c10","c10"));
		model.addAttribute("colors",colors);

		return "/admin/topicCard/list";
	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {

		List<MapEntity> statuss = new ArrayList<>();
		statuss.add(new MapEntity("waiting","待审核"));
		statuss.add(new MapEntity("failture","审核失败"));
		statuss.add(new MapEntity("success","审核通过"));
		model.addAttribute("statuss",statuss);

		List<MapEntity> colors = new ArrayList<>();
		colors.add(new MapEntity("c1","c1"));
		colors.add(new MapEntity("c2","c2"));
		colors.add(new MapEntity("c3","c3"));
		colors.add(new MapEntity("c4","c4"));
		colors.add(new MapEntity("c5","c5"));
		colors.add(new MapEntity("c6","c6"));
		colors.add(new MapEntity("c7","c7"));
		colors.add(new MapEntity("c8","c8"));
		colors.add(new MapEntity("c9","c9"));
		colors.add(new MapEntity("c10","c10"));
		model.addAttribute("colors",colors);

		return "/admin/topicCard/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(TopicCard topicCard,String mobile,String physicalPath){

		TopicCard entity = new TopicCard();
		if (physicalPath!=null) {
        	File physical = new File(physicalPath);
			JSONObject data = WeiXinUtils.uploadImageToCDN(physical);
			if(data.getString("url")!=null) {
				entity.setBackground(data.getString("url"));
			} else {
				return Message.error("上传背景失败");
			}
		}

		entity.setCreateDate(topicCard.getCreateDate());

		entity.setModifyDate(topicCard.getModifyDate());

		entity.setColor(topicCard.getColor());

		entity.setDescription(topicCard.getDescription());

		entity.setPrerogative(topicCard.getPrerogative());

		entity.setStatus(topicCard.getStatus());

		entity.setTitle(topicCard.getTitle());
		if (mobile==null) {
			return Message.error("请输入店主手机");
		}
		Member member = memberService.findByMobile(mobile);
		if (member==null) {
			return Message.error("无效店主手机");
		}
		Topic topic = member.getTopic();
		if (topic==null) {
			return Message.error("没有开通专栏");
		}

		entity.setTopic(topic);
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            topicCardService.save(entity);
            if (entity.getId()==null) {
            	return Message.error("保存失败");
			}
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
            topicCardService.delete(ids);
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
		statuss.add(new MapEntity("waiting","待审核"));
		statuss.add(new MapEntity("failture","审核失败"));
		statuss.add(new MapEntity("success","审核通过"));
		model.addAttribute("statuss",statuss);

		List<MapEntity> colors = new ArrayList<>();
		colors.add(new MapEntity("c1","c1"));
		colors.add(new MapEntity("c2","c2"));
		colors.add(new MapEntity("c3","c3"));
		colors.add(new MapEntity("c4","c4"));
		colors.add(new MapEntity("c5","c5"));
		colors.add(new MapEntity("c6","c6"));
		colors.add(new MapEntity("c7","c7"));
		colors.add(new MapEntity("c8","c8"));
		colors.add(new MapEntity("c9","c9"));
		colors.add(new MapEntity("c10","c10"));
		model.addAttribute("colors",colors);

		model.addAttribute("data",topicCardService.find(id));

		return "/admin/topicCard/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(TopicCard topicCard,String physicalPath){
		TopicCard entity = topicCardService.find(topicCard.getId());
		if (physicalPath!=null) {
			File physical = new File(physicalPath);
			JSONObject data = WeiXinUtils.uploadImageToCDN(physical);
			if(data.getString("url")!=null) {
				entity.setBackground(data.getString("url"));
			} else {
				return Message.error("上传背景失败");
			}

		}

		entity.setColor(topicCard.getColor());

		entity.setDescription(topicCard.getDescription());

		entity.setPrerogative(topicCard.getPrerogative());

		entity.setStatus(topicCard.getStatus());

		entity.setTitle(topicCard.getTitle());

		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            if (topicCardService.update(entity)==null) {
            	return Message.error("修改失败");
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
	public Message list(Date beginDate, Date endDate, TopicCard.Status status,String searchValue, Pageable pageable, ModelMap model) {
		ArrayList<Filter> filters = (ArrayList<Filter>) pageable.getFilters();
		if (status!=null) {
			Filter statusFilter = new Filter("status", Filter.Operator.eq, status);
			filters.add(statusFilter);
		}

		if(searchValue!=null){
			Filter mediaTypeFilter = new Filter("title", Filter.Operator.like, "%"+searchValue+"%");
			filters.add(mediaTypeFilter);
		}
		Page<TopicCard> page = topicCardService.findPage(beginDate,endDate,pageable);
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

		model.addAttribute("members",memberService.findAll());

		model.addAttribute("templates",templateService.findAll());

		model.addAttribute("topic",topicService.find(id));
		return "/admin/topicCard/view/topicView";
	}



}