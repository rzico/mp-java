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
import net.wit.entity.Course;
import net.wit.service.CourseService;

import java.util.*;

import net.wit.*;

import net.wit.entity.*;
import net.wit.service.*;
import net.wit.controller.admin.model.*;



/**
 * @ClassName: CourseController
 * @author 降魔战队
 * @date 2018-7-13 14:38:50
 */
 
@Controller("adminCourseController")
@RequestMapping("/admin/course")
public class CourseController extends BaseController {
	@Resource(name = "courseServiceImpl")
	private CourseService courseService;
	
	@Resource(name = "enterpriseServiceImpl")
	private EnterpriseService enterpriseService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "hostServiceImpl")
	private HostService hostService;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;



	/**
	 * 主页
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model) {

		List<MapEntity> statuss = new ArrayList<>();
		statuss.add(new MapEntity("enabled","开启"));
		statuss.add(new MapEntity("disabled","关闭"));
		model.addAttribute("statuss",statuss);

		List<MapEntity> types = new ArrayList<>();
		types.add(new MapEntity("_public","公共"));
		types.add(new MapEntity("_private","私有"));
		model.addAttribute("types",types);

		model.addAttribute("enterprises",enterpriseService.findAll());

		return "/admin/course/list";
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

		List<MapEntity> types = new ArrayList<>();
		types.add(new MapEntity("_public","公共"));
		types.add(new MapEntity("_private","私有"));
		model.addAttribute("types",types);

		model.addAttribute("enterprises",enterpriseService.findAll());

		return "/admin/course/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(Course course, Long enterpriseId){
		Course entity = new Course();	

		entity.setCreateDate(course.getCreateDate());

		entity.setModifyDate(course.getModifyDate());

		entity.setOrders(course.getOrders() == null ? 0 : course.getOrders());

		entity.setContent(course.getContent());

		entity.setDeleted(course.getDeleted());

		entity.setHits(course.getHits() == null ? 0 : course.getHits());

		entity.setName(course.getName());

		entity.setPrice(course.getPrice());

		entity.setSignup(course.getSignup() == null ? 0 : course.getSignup());

		entity.setStatus(course.getStatus());

		entity.setThumbnail(course.getThumbnail());

		entity.setType(course.getType());

		entity.setEnterprise(enterpriseService.find(enterpriseId));
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            courseService.save(entity);
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
            courseService.delete(ids);
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

		List<MapEntity> types = new ArrayList<>();
		types.add(new MapEntity("_public","公共"));
		types.add(new MapEntity("_private","私有"));
		model.addAttribute("types",types);

		model.addAttribute("enterprises",enterpriseService.findAll());

		model.addAttribute("data",courseService.find(id));

		return "/admin/course/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(Course course, Long enterpriseId){
		Course entity = courseService.find(course.getId());
		
		entity.setCreateDate(course.getCreateDate());

		entity.setModifyDate(course.getModifyDate());

		entity.setOrders(course.getOrders() == null ? 0 : course.getOrders());

		entity.setContent(course.getContent());

		entity.setDeleted(course.getDeleted());

		entity.setHits(course.getHits() == null ? 0 : course.getHits());

		entity.setName(course.getName());

		entity.setPrice(course.getPrice());

		entity.setSignup(course.getSignup() == null ? 0 : course.getSignup());

		entity.setStatus(course.getStatus());

		entity.setThumbnail(course.getThumbnail());

		entity.setType(course.getType());

		entity.setEnterprise(enterpriseService.find(enterpriseId));
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            courseService.update(entity);
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
	public Message list(Date beginDate, Date endDate, Course.Status status, Course.Type type, Pageable pageable, ModelMap model) {	
		ArrayList<Filter> filters = (ArrayList<Filter>) pageable.getFilters();
		if (status!=null) {
			Filter statusFilter = new Filter("status", Filter.Operator.eq, status);
			filters.add(statusFilter);
		}
		if (type!=null) {
			Filter typeFilter = new Filter("type", Filter.Operator.eq, type);
			filters.add(typeFilter);
		}

		Page<Course> page = courseService.findPage(beginDate,endDate,pageable);
		return Message.success(PageBlock.bind(page), "admin.list.success");
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
		return "/admin/course/view/enterpriseView";
	}



}