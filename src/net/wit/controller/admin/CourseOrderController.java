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
import net.wit.entity.CourseOrder;
import net.wit.service.CourseOrderService;

import java.util.*;

import net.wit.*;

import net.wit.entity.*;
import net.wit.service.*;
import net.wit.controller.admin.model.*;



/**
 * @ClassName: CourseOrderController
 * @author 降魔战队
 * @date 2018-7-13 14:38:50
 */
 
@Controller("adminCourseOrderController")
@RequestMapping("/admin/courseOrder")
public class CourseOrderController extends BaseController {
	@Resource(name = "courseOrderServiceImpl")
	private CourseOrderService courseOrderService;
	
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


	/**
	 * 主页
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model) {

		List<MapEntity> orderStatuss = new ArrayList<>();
		orderStatuss.add(new MapEntity("enabled","开启"));
		orderStatuss.add(new MapEntity("disabled","关闭"));
		model.addAttribute("orderStatuss",orderStatuss);

		List<MapEntity> types = new ArrayList<>();
		types.add(new MapEntity("_public","公共"));
		types.add(new MapEntity("_private","私有"));
		model.addAttribute("types",types);

		return "/admin/courseOrder/list";
	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {

		List<MapEntity> orderStatuss = new ArrayList<>();
		orderStatuss.add(new MapEntity("enabled","开启"));
		orderStatuss.add(new MapEntity("disabled","关闭"));
		model.addAttribute("orderStatuss",orderStatuss);

		List<MapEntity> types = new ArrayList<>();
		types.add(new MapEntity("_public","公共"));
		types.add(new MapEntity("_private","私有"));
		model.addAttribute("types",types);


		return "/admin/courseOrder/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(CourseOrder courseOrder, Long courseId, Long enterpriseId){
		CourseOrder entity = new CourseOrder();	

		entity.setCreateDate(courseOrder.getCreateDate());

		entity.setModifyDate(courseOrder.getModifyDate());

		entity.setName(courseOrder.getName());

		entity.setOrderStatus(courseOrder.getOrderStatus());

		entity.setPrice(courseOrder.getPrice());

		entity.setThumbnail(courseOrder.getThumbnail());

		entity.setType(courseOrder.getType());

		entity.setCourse(courseService.find(courseId));

		entity.setEnterprise(enterpriseService.find(enterpriseId));
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            courseOrderService.save(entity);
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
            courseOrderService.delete(ids);
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

		List<MapEntity> orderStatuss = new ArrayList<>();
		orderStatuss.add(new MapEntity("enabled","开启"));
		orderStatuss.add(new MapEntity("disabled","关闭"));
		model.addAttribute("orderStatuss",orderStatuss);

		model.addAttribute("data",courseOrderService.find(id));

		return "/admin/courseOrder/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(CourseOrder courseOrder, Long courseId, Long enterpriseId){
		CourseOrder entity = courseOrderService.find(courseOrder.getId());
		
		entity.setOrderStatus(courseOrder.getOrderStatus());

		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            courseOrderService.update(entity);
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
	public Message list(Date beginDate, Date endDate, CourseOrder.OrderStatus orderStatus, CourseOrder.Type type, Pageable pageable, ModelMap model) {	
		ArrayList<Filter> filters = (ArrayList<Filter>) pageable.getFilters();
		if (orderStatus!=null) {
			Filter orderStatusFilter = new Filter("orderStatus", Filter.Operator.eq, orderStatus);
			filters.add(orderStatusFilter);
		}

		if (type!=null) {
			Filter typeFilter = new Filter("type", Filter.Operator.eq, type);
			filters.add(typeFilter);
		}

		if (pageable.getSearchValue()!=null) {
			Filter keywordFilter = new Filter("name", Filter.Operator.like, "%"+pageable.getSearchValue()+"%");
			filters.add(keywordFilter);
		}

		Admin admin = adminService.getCurrent();
		filters.add(new Filter("enterprise",Filter.Operator.eq,admin.getEnterprise()));

		Page<CourseOrder> page = courseOrderService.findPage(beginDate,endDate,pageable);
		return Message.success(PageBlock.bind(page), "admin.list.success");
	}
	
	
	/**
	 * Course视图
	 */
	@RequestMapping(value = "/courseView", method = RequestMethod.GET)
	public String courseView(Long id, ModelMap model) {
		List<MapEntity> statuss = new ArrayList<>();
		statuss.add(new MapEntity("enabled","开启"));
		statuss.add(new MapEntity("disabled","关闭"));
		model.addAttribute("statuss",statuss);

		List<MapEntity> types = new ArrayList<>();
		types.add(new MapEntity("_public","公共"));
		types.add(new MapEntity("_private","私有"));
		model.addAttribute("types",types);

		model.addAttribute("enterprises",enterpriseService.findAll());

		model.addAttribute("course",courseService.find(id));
		return "/admin/courseOrder/view/courseView";
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
		return "/admin/courseOrder/view/enterpriseView";
	}



}