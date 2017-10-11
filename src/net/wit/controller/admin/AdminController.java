package net.wit.controller.admin;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;

import javax.annotation.Resource;
import net.wit.Filter;
import net.wit.Message;
import net.wit.Pageable;

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

import net.wit.entity.BaseEntity.Save;
import net.wit.entity.Admin;
import net.wit.service.AdminService;

import java.util.*;

import net.wit.*;

import net.wit.entity.*;
import net.wit.service.*;
import net.wit.controller.admin.model.*;



/**
 * @ClassName: AdminController
 * @author 降魔战队
 * @date 2017-10-11 15:37:3
 */
 
@Controller("adminAdminController")
@RequestMapping("/admin/admin")
public class AdminController extends BaseController {
	@Resource(name = "adminServiceImpl")
	private AdminService adminService;
	
	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "enterpriseServiceImpl")
	private EnterpriseService enterpriseService;

	@Resource(name = "roleServiceImpl")
	private RoleService roleService;



	/**
	 * 主页
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model) {

		model.addAttribute("enterprises",enterpriseService.findAll());

		List<MapEntity> genders = new ArrayList<>();
		genders.add(new MapEntity("male","男"));
		genders.add(new MapEntity("female","女"));
		genders.add(new MapEntity("secrecy","保密"));
		model.addAttribute("genders",genders);

		model.addAttribute("areas",areaService.findAll());

		model.addAttribute("roles",roleService.findAll());

		return "/admin/admin/list";
	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {

		model.addAttribute("enterprises",enterpriseService.findAll());

		List<MapEntity> genders = new ArrayList<>();
		genders.add(new MapEntity("male","男"));
		genders.add(new MapEntity("female","女"));
		genders.add(new MapEntity("secrecy","保密"));
		model.addAttribute("genders",genders);

		model.addAttribute("areas",areaService.findAll());

		model.addAttribute("roles",roleService.findAll());

		return "/admin/admin/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(Admin admin, Long areaId, Long enterpriseId, Long [] roleIds){
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

		entity.setPassword(MD5Utils.getMD5Str(admin.getPassword()));

		entity.setUsername(admin.getUsername());

		entity.setEnterprise(enterpriseService.find(enterpriseId));

		entity.setGender(admin.getGender());

		entity.setArea(areaService.find(areaId));

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
	
	
	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {

		model.addAttribute("enterprises",enterpriseService.findAll());

		List<MapEntity> genders = new ArrayList<>();
		genders.add(new MapEntity("male","男"));
		genders.add(new MapEntity("female","女"));
		genders.add(new MapEntity("secrecy","保密"));
		model.addAttribute("genders",genders);

		model.addAttribute("areas",areaService.findAll());

		model.addAttribute("roles",roleService.findAll());

		model.addAttribute("data",adminService.find(id));

		return "/admin/admin/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(Admin admin, Long areaId, Long enterpriseId, Long [] roleIds){
		Admin entity = adminService.find(admin.getId());
		
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

		entity.setPassword(MD5Utils.getMD5Str(admin.getPassword()));

		entity.setUsername(admin.getUsername());

		entity.setEnterprise(enterpriseService.find(enterpriseId));

		entity.setGender(admin.getGender());

		entity.setArea(areaService.find(areaId));

		entity.setRoles(roleService.findList(roleIds));
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            adminService.update(entity);
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
	public Message list(Date beginDate, Date endDate, Admin.Gender gender, Pageable pageable, ModelMap model) {	
		ArrayList<Filter> filters = (ArrayList<Filter>) pageable.getFilters();
		if (gender!=null) {
			Filter genderFilter = new Filter("gender", Filter.Operator.eq, gender);
			filters.add(genderFilter);
		}

		Page<Admin> page = adminService.findPage(beginDate,endDate,pageable);
		return Message.success(PageBlock.bind(page), "admin.list.success");
	}
	
	
	/**
	 * 地区视图
	 */
	@RequestMapping(value = "/areaView", method = RequestMethod.GET)
	public String areaView(Long id, ModelMap model) {


		model.addAttribute("area",areaService.find(id));
		return "/admin/admin/view/areaView";
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

		model.addAttribute("enterprise",enterpriseService.find(id));
		return "/admin/admin/view/enterpriseView";
	}



}