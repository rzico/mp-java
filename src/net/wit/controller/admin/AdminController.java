package net.wit.controller.admin;

import java.util.*;

import javax.annotation.Resource;
import net.wit.Filter;
import net.wit.Message;
import net.wit.Page;
import net.wit.Pageable;

import net.wit.entity.Role;
import net.wit.service.RoleService;
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
import net.wit.entity.Admin;
import net.wit.service.AdminService;

import net.wit.entity.Enterprise;
import net.wit.service.EnterpriseService;

/**
 * @ClassName: AdminController
 * @author 降魔战队
 * @date 2017-9-3 23:17:34
 */

@Controller("adminAdminController")
@RequestMapping("/admin/admin")
public class AdminController extends BaseController {
	@Resource(name = "adminServiceImpl")
	private AdminService adminService;

	@Resource(name = "enterpriseServiceImpl")
	private EnterpriseService enterpriseService;

	@Resource(name = "roleServiceImpl")
	private RoleService roleService;

	/**
	 * 保存
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public Message save(Admin admin, Long enterpriseId,Long [] roleIds){
		Admin entity = new Admin();


		entity.setCreateDate(admin.getCreateDate());

		entity.setModifyDate(admin.getModifyDate());

		entity.setDepartment(admin.getDepartment());

		entity.setEmail(admin.getEmail());

		entity.setIsEnabled(admin.getIsEnabled());

		entity.setIsLocked(admin.getIsLocked());

		entity.setLockedDate(admin.getLockedDate());

		entity.setLoginDate(admin.getLoginDate());

		entity.setLoginFailureCount(admin.getLoginFailureCount() == null ? 0 : admin.getLoginFailureCount());

		entity.setLoginIp(admin.getLoginIp());

		entity.setName(admin.getName());

		entity.setPassword(admin.getPassword());

		entity.setUsername(admin.getUsername());

		entity.setEnterprise(enterpriseService.find(enterpriseId));

		entity.setGender(admin.getGender());


		entity.setGender(admin.getGender());

		entity.setRoles(roleService.findList(roleIds));

		if (!isValid(entity, Save.class)) {
			return Message.error("admin.data.valid");
		}
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
	public Message update(Admin admin, Long enterpriseId,Long [] roleIds){
		Admin entity = adminService.find(admin.getId());

		entity.setCreateDate(admin.getCreateDate());

		entity.setModifyDate(admin.getModifyDate());

		entity.setDepartment(admin.getDepartment());

		entity.setEmail(admin.getEmail());

		entity.setIsEnabled(admin.getIsEnabled());

		entity.setIsLocked(admin.getIsLocked());

		entity.setLockedDate(admin.getLockedDate());

		entity.setLoginDate(admin.getLoginDate());

		entity.setLoginFailureCount(admin.getLoginFailureCount());

		entity.setLoginIp(admin.getLoginIp());

		entity.setName(admin.getName());

		entity.setPassword(admin.getPassword());

		entity.setUsername(admin.getUsername());

		entity.setEnterprise(enterpriseService.find(enterpriseId));

		entity.setGender(admin.getGender());

		entity.setRoles(roleService.findList(roleIds));


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
	 * 主页
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String main() {

		return "/admin/admin/add";
	}


	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Date beginDate, Date endDate, Pageable pageable, ModelMap model) {
		//常量输出
		Map<String,String> gender = new HashMap<String,String>();
		gender.put("male","男");
		gender.put("female","女");
		gender.put("secrecy","保密");

		//model.addAttribute("gender",gender);

		//多对一输出
		model.addAttribute("enterprises",enterpriseService.findAll());

		//多对一输出
		model.addAttribute("roles",roleService.findAll());

		//常输的过滤条件
		ArrayList<Filter> filters = (ArrayList<Filter>) pageable.getFilters();
		Filter genderFilter = new Filter("gender", Filter.Operator.eq,gender);
		filters.add(genderFilter);
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
