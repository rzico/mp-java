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
import net.wit.entity.Organization;
import net.wit.service.OrganizationService;

import java.util.*;

import net.wit.*;

import net.wit.entity.*;
import net.wit.service.*;
import net.wit.controller.admin.model.*;



/**
 * @ClassName: OrganizationController
 * @author 降魔战队
 * @date 2018-2-28 16:42:16
 */
 
@Controller("adminOrganizationController")
@RequestMapping("/admin/organization")
public class OrganizationController extends BaseController {
	@Resource(name = "organizationServiceImpl")
	private OrganizationService organizationService;
	


	/**
	 * 主页
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model) {

		List<MapEntity> types = new ArrayList<>();
		types.add(new MapEntity("school","学校"));
		types.add(new MapEntity("enterprise","企业"));
		model.addAttribute("types",types);

		return "/admin/organization/list";
	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {

		List<MapEntity> types = new ArrayList<>();
		types.add(new MapEntity("school","学校"));
		types.add(new MapEntity("enterprise","企业"));
		model.addAttribute("types",types);

		return "/admin/organization/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(Organization organization){
		Organization entity = new Organization();	

		entity.setCreateDate(organization.getCreateDate());

		entity.setModifyDate(organization.getModifyDate());

		entity.setOrders(organization.getOrders() == null ? 0 : organization.getOrders());

		entity.setName(organization.getName());

		entity.setType(organization.getType());
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            organizationService.save(entity);
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
            organizationService.delete(ids);
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

		List<MapEntity> types = new ArrayList<>();
		types.add(new MapEntity("school","学校"));
		types.add(new MapEntity("enterprise","企业"));
		model.addAttribute("types",types);

		model.addAttribute("data",organizationService.find(id));

		return "/admin/organization/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(Organization organization){
		Organization entity = organizationService.find(organization.getId());
		
		entity.setCreateDate(organization.getCreateDate());

		entity.setModifyDate(organization.getModifyDate());

		entity.setOrders(organization.getOrders() == null ? 0 : organization.getOrders());

		entity.setName(organization.getName());

		entity.setType(organization.getType());
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            organizationService.update(entity);
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
	public Message list(Date beginDate, Date endDate, Organization.Type type, Pageable pageable, ModelMap model) {	
		ArrayList<Filter> filters = (ArrayList<Filter>) pageable.getFilters();
		if (type!=null) {
			Filter typeFilter = new Filter("type", Filter.Operator.eq, type);
			filters.add(typeFilter);
		}

		Page<Organization> page = organizationService.findPage(beginDate,endDate,pageable);
		return Message.success(PageBlock.bind(page), "admin.list.success");
	}
	
	

}