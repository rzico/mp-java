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

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "smssendServiceImpl")
	private SmssendService smssendService;
	
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

//		model.addAttribute("areas",areaService.findAll());

//		model.addAttribute("roles",roleService.findAll());

		return "/admin/admin/list";
	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {

		List<MapEntity> genders = new ArrayList<>();
		genders.add(new MapEntity("male","男"));
		genders.add(new MapEntity("female","女"));
		genders.add(new MapEntity("secrecy","保密"));
		model.addAttribute("genders",genders);

		model.addAttribute("roles",roleService.findAll());

		return "/admin/admin/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(Admin admin, Long areaId, Long enterpriseId, Long [] roleIds){
		Admin loginAdmin = adminService.getCurrent();


		Admin check = adminService.findByUsername(admin.getUsername());
		if (check!=null) {
			return Message.error("用户名重复");
		}

		Admin entity = new Admin();	

		entity.setDepartment(admin.getDepartment());

		entity.setEmail(admin.getEmail());

		entity.setIsEnabled(true);

		entity.setIsLocked(false);
		entity.setLoginFailureCount(0);
		entity.setName(admin.getName());

		entity.setPassword(MD5Utils.getMD5Str(admin.getPassword()));

		entity.setUsername(admin.getUsername());

		entity.setEnterprise(loginAdmin.getEnterprise());

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
		for(long id:ids){
			// id=1 为系统管理员
			if (id == 1) {
				return Message.error("系统管理员不允许删除!");
			}
		}

		for (Long id:ids) {
			Admin admin = adminService.find(id);
			if (admin.getMember()!=null) {
				return Message.error("用户账号不允许删除!");
			}
		}

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

		List<MapEntity> genders = new ArrayList<>();
		genders.add(new MapEntity("male","男"));
		genders.add(new MapEntity("female","女"));
		genders.add(new MapEntity("secrecy","保密"));
		model.addAttribute("genders",genders);

		model.addAttribute("roles",roleService.findAll());

        Admin admin = adminService.find(id);
		model.addAttribute("dataRoles",admin.getRoles());

		model.addAttribute("data",admin);

		return "/admin/admin/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(Admin admin, Long areaId,Long [] roleIds){
		Admin entity = adminService.find(admin.getId());

		entity.setDepartment(admin.getDepartment());

		entity.setEmail(admin.getEmail());

		entity.setIsEnabled(admin.getIsEnabled());

		entity.setIsLocked(admin.getIsLocked());

		entity.setLockedDate(admin.getLockedDate());

		entity.setLoginDate(admin.getLoginDate());

		entity.setLoginFailureCount(admin.getLoginFailureCount() == null ? 0 : admin.getLoginFailureCount());

		entity.setLoginIp(admin.getLoginIp());

		entity.setName(admin.getName());

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
	 * 重置密码
	 */
	@RequestMapping(value = "/reset", method = RequestMethod.POST)
	@ResponseBody
	public Message reset(Long Id){
		Admin entity = adminService.find(Id);

		String securityCode;
		String m = "";
		if (entity.getMember()!=null) {
			Member member = memberService.find(entity.getMember().getId());
			m = member.getMobile();
			if ("" != m){
				int challege = net.wit.util.StringUtils.Random6Code();
				securityCode = String.valueOf(challege);
			}else{
				securityCode = "123456";
			}
		}else{
			securityCode = "123456";
		}
		entity.setPassword(MD5Utils.getMD5Str(securityCode));

		Smssend smsSend = new Smssend();
		if ("" != m) {
			smsSend.setMobile(m);
			smsSend.setContent("重置密码 :" + securityCode + ",只用于登录使用。");
		}

		if (!isValid(entity)) {
			return Message.error("admin.data.valid");
		}
		try {
			adminService.update(entity);
			if ("" != m){
				smssendService.smsSend(smsSend);
				return Message.success("发送成功");
			}else{
				return Message.success(entity,"admin.update.success");
			}

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
	public Message list(Date beginDate, Date endDate, Admin.Gender gender, Pageable pageable,String searchValue, ModelMap model) {
		ArrayList<Filter> filters = (ArrayList<Filter>) pageable.getFilters();
		if (gender!=null) {
			Filter genderFilter = new Filter("gender", Filter.Operator.eq, gender);
			filters.add(genderFilter);
		}

		Admin admin =adminService.getCurrent();

		//非超级管理员都只能管本企业用户
		if (!admin.isManager()) {
			filters.add(new Filter("enterprise", Filter.Operator.eq, admin.getEnterprise()));
		}

		if(searchValue!=null){
			filters.add( new Filter("name", Filter.Operator.like, "%"+searchValue+"%"));
		}

		Page<Admin> page = adminService.findPage(beginDate,endDate,pageable);
		return Message.success(PageBlock.bind(page), "admin.list.success");

	}
}