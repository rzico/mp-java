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

		model.addAttribute("enterprises",enterpriseService.findAll());

		List<MapEntity> genders = new ArrayList<>();
		genders.add(new MapEntity("male","男"));
		genders.add(new MapEntity("female","女"));
		genders.add(new MapEntity("secrecy","保密"));
		model.addAttribute("genders",genders);
//
//		model.addAttribute("areas",areaService.findAll());

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

		entity.setIsEnabled(true);

		entity.setIsLocked(false);
		//entity.setLockedDate(admin.getLockedDate());
		//entity.setLoginDate(admin.getLoginDate());
		entity.setLoginFailureCount(0);
		//entity.setLoginIp(admin.getLoginIp());
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
		for(long id:ids){
			// id=1 为系统管理员
			if (id == 1) {
				return Message.error("系统管理员不允许删除!");
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

		model.addAttribute("enterprises",enterpriseService.findAll());

		List<MapEntity> genders = new ArrayList<>();
		genders.add(new MapEntity("male","男"));
		genders.add(new MapEntity("female","女"));
		genders.add(new MapEntity("secrecy","保密"));
		model.addAttribute("genders",genders);
//
//		model.addAttribute("areas",areaService.findAll());

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
		//entity.setPassword(MD5Utils.getMD5Str(admin.getPassword()));
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
	public Message list(Date beginDate, Date endDate, Admin.Gender gender, Pageable pageable, ModelMap model) {	
		ArrayList<Filter> filters = (ArrayList<Filter>) pageable.getFilters();
		if (gender!=null) {
			Filter genderFilter = new Filter("gender", Filter.Operator.eq, gender);
			filters.add(genderFilter);
		}

		Admin admin =adminService.getCurrent();
//		System.out.println("admin.ID:"+admin.getId());
		Enterprise enterprise=admin.getEnterprise();
//		System.out.println("enter.ID:"+enterprise.getId());

		if(enterprise==null){
			return Message.error("您还未绑定企业");
		}
		//判断企业是否被删除
		if(enterprise.getDeleted()){
			Message.error("您的企业不存在");
		}
		//代理商
		//个人代理商(無權限)
		//商家
		if(enterprise.getType()== Enterprise.Type.shop||enterprise.getType()== Enterprise.Type.agent){
			if(enterprise.getMember()!=null){
				Filter mediaTypeFilter = new Filter("enterprise", Filter.Operator.eq, enterprise);
//				System.out.println("admin.enter.member.ID:"+enterprise.getMember().getId());
				filters.add(mediaTypeFilter);
			}
			else{
				return Message.error("该商家未绑定");
			}
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