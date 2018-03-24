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
import net.wit.entity.Role;
import net.wit.service.RoleService;

import java.util.*;

import net.wit.*;

import net.wit.entity.*;
import net.wit.service.*;
import net.wit.controller.admin.model.*;



/**
 * @ClassName: RoleController
 * @author 降魔战队
 * @date 2017-10-11 15:37:14
 */
 
@Controller("adminRoleController")
@RequestMapping("/admin/role")
public class RoleController extends BaseController {
	@Resource(name = "roleServiceImpl")
	private RoleService roleService;
	


	/**
	 * 主页
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model) {



		return "/admin/role/list";
	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {



		return "/admin/role/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(Role role){
		Role entity = new Role();	

		entity.setCreateDate(role.getCreateDate());

		entity.setModifyDate(role.getModifyDate());

		entity.setDescription(role.getDescription());

		entity.setIsSystem(role.getIsSystem());

		entity.setName(role.getName());
		
		if (!isValid(entity, Save.class)) {
            return Message.error("admin.data.valid");
        }
        try {
            roleService.save(entity);
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
            roleService.delete(ids);
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



		model.addAttribute("data",roleService.find(id));

		return "/admin/role/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(Role role){
		Role entity = roleService.find(role.getId());
		
		entity.setCreateDate(role.getCreateDate());

		entity.setModifyDate(role.getModifyDate());

		entity.setDescription(role.getDescription());

		entity.setIsSystem(role.getIsSystem());

		entity.setName(role.getName());
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            roleService.update(entity);
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
	public Message list(Date beginDate, Date endDate, Pageable pageable,String searchValue, ModelMap model) {
		ArrayList<Filter> filters = (ArrayList<Filter>) pageable.getFilters();
		if(searchValue!=null){
			Filter mediaTypeFilter = new Filter("name", Filter.Operator.like, "%"+searchValue+"%");
			filters.add(mediaTypeFilter);
		}
		Page<Role> page = roleService.findPage(beginDate,endDate,pageable);
		return Message.success(PageBlock.bind(page), "admin.list.success");
	}
	
	

}