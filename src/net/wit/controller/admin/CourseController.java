package net.wit.controller.admin;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;

import javax.annotation.Resource;
import net.wit.Filter;
import net.wit.Message;
import net.wit.Pageable;

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

	@Resource(name = "adminServiceImpl")
	private AdminService adminService;

	@Resource(name = "tagServiceImpl")
	private TagService tagService;


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

		model.addAttribute("tags", tagService.findList(Tag.Type.course));

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

		model.addAttribute("tags", tagService.findList(Tag.Type.course));

		return "/admin/course/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(Course course,String [] images,  Long enterpriseId){
		Admin admin = adminService.getCurrent();
		Course entity = new Course();	

		entity.setOrders(course.getOrders() == null ? 0 : course.getOrders());

		entity.setContent1(course.getContent1());
		entity.setContent2(course.getContent1());
		entity.setContent3(course.getContent1());
		entity.setContent4(course.getContent1());
		entity.setContent5(course.getContent1());
		entity.setContent6(course.getContent1());
		entity.setContent7(course.getContent1());
		entity.setContent1(course.getContent1());
		entity.setContentLogo(course.getContentLogo());
		List<String> id = new ArrayList<>();
		for (String s:images) {
			id.add(s);
		}
		entity.setImages(JsonUtils.toJson(id));

		entity.setDeleted(false);

		entity.setHits(0L);

		entity.setName(course.getName());

		entity.setPrice(course.getPrice());

		entity.setSignup(0L);

		entity.setStatus(course.getStatus());

		entity.setThumbnail(course.getThumbnail());

		entity.setType(course.getType());

		entity.setEnterprise(admin.getEnterprise());
		
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

		model.addAttribute("tags", tagService.findList(Tag.Type.course));

		model.addAttribute("data",courseService.find(id));

		return "/admin/course/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(Course course,String [] images, Long enterpriseId){
		Admin admin = adminService.getCurrent();
		Course entity = courseService.find(course.getId());
		
		entity.setOrders(course.getOrders() == null ? 0 : course.getOrders());

		entity.setContent1(course.getContent1());
		entity.setContent2(course.getContent1());
		entity.setContent3(course.getContent1());
		entity.setContent4(course.getContent1());
		entity.setContent5(course.getContent1());
		entity.setContent6(course.getContent1());
		entity.setContent7(course.getContent1());
		entity.setContent1(course.getContent1());
		entity.setContentLogo(course.getContentLogo());
		List<String> id = new ArrayList<>();
		for (String s:images) {
			id.add(s);
		}
		entity.setImages(JsonUtils.toJson(id));

		entity.setDeleted(false);

		entity.setName(course.getName());

		entity.setPrice(course.getPrice());

		entity.setStatus(course.getStatus());

		entity.setThumbnail(course.getThumbnail());

		entity.setType(course.getType());

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

		Admin admin =adminService.getCurrent();

		//非超级管理员都只能管本企业用户
		if (!admin.getId().equals(1L)) {
			filters.add(new Filter("enterprise", Filter.Operator.eq, admin.getEnterprise()));
		}

		Page<Course> page = courseService.findPage(beginDate,endDate,pageable);
		return Message.success(PageBlock.bind(page), "admin.list.success");
	}

}