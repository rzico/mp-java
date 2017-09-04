package net.wit.controller.admin;

import java.util.*;

import javax.annotation.Resource;

import net.wit.Filter;
import net.wit.Message;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Admin;
import net.wit.entity.BaseEntity.Save;
import net.wit.entity.Enterprise;
import net.wit.entity.Role;
import net.wit.service.AdminService;

import net.wit.util.MD5Utils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Filters;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 管理员
 */
@Controller("adminAdminController")
@RequestMapping("/admin/admin")
public class AdminController extends BaseController {

	@Resource(name = "adminServiceImpl")
	private AdminService adminService;

	/**
	 * 保存
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public Message save(Admin admin,Long [] roleIds,Long enterpriseId) {
		Admin entity = new Admin();
		//正常赋值
		entity.setUsername(admin.getUsername());
		entity.setPassword(MD5Utils.getMD5Str(admin.getPassword()));
		entity.setGender(admin.getGender());
		entity.setEmail(admin.getEmail());
		entity.setName(admin.getName());
		entity.setDepartment(admin.getDepartment());
		entity.setIsLocked(admin.getIsLocked());
		entity.setLoginFailureCount(1);
		entity.setLockedDate(admin.getLockedDate());
		entity.setLoginDate(admin.getLoginDate());
		entity.setLoginIp(admin.getLoginIp());
		entity.setEnterprise(new Enterprise());

		//entity.setRoles(new HashSet<Role>().add(r)));

		//多对一输出
		//entity.setArea(areaService.find(areaId));

//		entity.setRoles(roleService.findList(roleIds));

//		if (!isValid(admin, Save.class)) {
//			return Message.error("admin.data.valid");
//		}

		try {
			adminService.save(entity);
			return Message.success(entity,"admin.save.success");
		} catch (Exception e) {
			e.printStackTrace();
			return Message.error("admin.save.error");
		}
	}

	/**
	 * 获取数据
	 */
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	@ResponseBody
	public Admin view(Long id, ModelMap model) {
		return adminService.find(id);
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public Message update(Admin admin
						  //,Long areaId  多对一传参
	     ) {
		Admin entity = adminService.find(admin.getId());
		//正常赋值
		entity.setEmail(admin.getEmail());
		//多对一赋值
		//entity.setArea(areaService.find(areaId));
		if (!isValid(entity)) {
			return Message.error("admin.data.valid");
		}
		try {
			adminService.save(entity);
			return Message.success(entity,"admin.update.success");
		} catch (Exception e) {
			e.printStackTrace();
			return Message.error("admin.update.error");
		}
	}

	/**
	 * 添加页面
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add() {
		return "/admin/admin/add";
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Date beginDate, Date endDate
					   //,Status status
			, Pageable pageable, ModelMap model) {

		//常量输出
		//Map<String,String> status = new HashMap<String,String)();
		//status.put("key1","值1");
		//status.put("key2","值2");
		//model.addAttribute("status",status);
		//多对一输出
		//model.addAttribute("areas",areaService.findAll());

		//是否要返回例信息，待定
		//model.addAttribute("columns",例信息);

		//常输的过滤条件
		//ArrayList<Filter> filters = (ArrayList<Filter>) pageable.getFilters();
		//Filter statusFilter = new Filter("status", Filter.Operator.eq,status);
		//filters.add(statusFilter);

		Page<Admin> page = adminService.findPage(beginDate,endDate,pageable);
		model.addAttribute("page", page);
		return "/admin/admin/list";
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long[] ids) {
		try {
			adminService.delete(ids);
			return Message.success("admin.delete.success");
		} catch (Exception e) {
			e.printStackTrace();
			return Message.error("admin.delete.error");
		}
	}

}