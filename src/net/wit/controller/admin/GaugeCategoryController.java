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
import net.wit.entity.GaugeCategory;
import net.wit.service.GaugeCategoryService;

import java.util.*;

import net.wit.*;

import net.wit.entity.*;
import net.wit.service.*;
import net.wit.controller.admin.model.*;



/**
 * @ClassName: GaugeCategoryController
 * @author 降魔战队
 * @date 2018-2-24 14:17:24
 */
 
@Controller("adminGaugeCategoryController")
@RequestMapping("/admin/gaugeCategory")
public class GaugeCategoryController extends BaseController {
	@Resource(name = "gaugeCategoryServiceImpl")
	private GaugeCategoryService gaugeCategoryService;
	
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "topicServiceImpl")
	private TopicService topicService;

	@Resource(name = "occupationServiceImpl")
	private OccupationService occupationService;

	@Resource(name = "tagServiceImpl")
	private TagService tagService;



	/**
	 * 主页
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model) {

		model.addAttribute("members",memberService.findAll());

		return "/admin/gaugeCategory/list";
	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {

		model.addAttribute("members",memberService.findAll());

		return "/admin/gaugeCategory/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(GaugeCategory gaugeCategory, Long parentId, Long memberId){
		GaugeCategory entity = new GaugeCategory();	

		entity.setOrders(gaugeCategory.getOrders() == null ? 0 : gaugeCategory.getOrders());

		entity.setName(gaugeCategory.getName());

        try {
            gaugeCategoryService.save(entity);
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
            gaugeCategoryService.delete(ids);
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

		model.addAttribute("members",memberService.findAll());

		model.addAttribute("data",gaugeCategoryService.find(id));

		return "/admin/gaugeCategory/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(GaugeCategory gaugeCategory, Long parentId, Long memberId){
		GaugeCategory entity = gaugeCategoryService.find(gaugeCategory.getId());

		entity.setOrders(gaugeCategory.getOrders() == null ? 0 : gaugeCategory.getOrders());

		entity.setName(gaugeCategory.getName());

        try {
            gaugeCategoryService.update(entity);
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

		Page<GaugeCategory> page = gaugeCategoryService.findPage(beginDate,endDate,pageable);
		return Message.success(PageBlock.bind(page), "admin.list.success");
	}
	
	
	/**
	 * GaugeCategory视图
	 */
	@RequestMapping(value = "/gaugeCategoryView", method = RequestMethod.GET)
	public String gaugeCategoryView(Long id, ModelMap model) {
		model.addAttribute("members",memberService.findAll());

		model.addAttribute("gaugeCategory",gaugeCategoryService.find(id));
		return "/admin/gaugeCategory/view/gaugeCategoryView";
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
		return "/admin/gaugeCategory/view/memberView";
	}



}