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
import net.wit.entity.Enterprise;
import net.wit.service.EnterpriseService;

import java.util.*;

import net.wit.*;

import net.wit.entity.*;
import net.wit.service.*;
import net.wit.controller.admin.model.*;



/**
 * @ClassName: EnterpriseController
 * @author 降魔战队
 * @date 2017-9-14 19:42:13
 */
 
@Controller("adminEnterpriseController")
@RequestMapping("/admin/enterprise")
public class EnterpriseController extends BaseController {
	@Resource(name = "enterpriseServiceImpl")
	private EnterpriseService enterpriseService;
	
	@Resource(name = "areaServiceImpl")
	private AreaService areaService;



	/**
	 * 主页
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model) {

		List<MapEntity> types = new ArrayList<>();
		types.add(new MapEntity("operate","运营商"));
		types.add(new MapEntity("agent","代理商"));
		model.addAttribute("types",types);

		model.addAttribute("areas",areaService.findAll());

		return "/admin/enterprise/list";
	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {

		List<MapEntity> types = new ArrayList<>();
		types.add(new MapEntity("operate","运营商"));
		types.add(new MapEntity("agent","代理商"));
		model.addAttribute("types",types);

		model.addAttribute("areas",areaService.findAll());

		return "/admin/enterprise/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(Enterprise enterprise, Long areaId){
		Enterprise entity = new Enterprise();	

		entity.setCreateDate(enterprise.getCreateDate());

		entity.setModifyDate(enterprise.getModifyDate());

		entity.setName(enterprise.getName());

		entity.setBrokerage(enterprise.getBrokerage());

		entity.setType(enterprise.getType());

		entity.setArea(areaService.find(areaId));
		
		if (!isValid(entity, Save.class)) {
            return Message.error("admin.data.valid");
        }
        try {
            enterpriseService.save(entity);
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
            enterpriseService.delete(ids);
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
		types.add(new MapEntity("operate","运营商"));
		types.add(new MapEntity("agent","代理商"));
		model.addAttribute("types",types);

		model.addAttribute("areas",areaService.findAll());

		model.addAttribute("data",enterpriseService.find(id));

		return "/admin/enterprise/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(Enterprise enterprise, Long areaId){
		Enterprise entity = enterpriseService.find(enterprise.getId());
		
		entity.setCreateDate(enterprise.getCreateDate());

		entity.setModifyDate(enterprise.getModifyDate());

		entity.setName(enterprise.getName());

		entity.setBrokerage(enterprise.getBrokerage());

		entity.setType(enterprise.getType());

		entity.setArea(areaService.find(areaId));
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            enterpriseService.update(entity);
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
	public Message list(Date beginDate, Date endDate, Enterprise.Type type, Pageable pageable, ModelMap model) {
		ArrayList<Filter> filters = (ArrayList<Filter>) pageable.getFilters();
		if (type!=null) {
			Filter typeFilter = new Filter("type", Filter.Operator.eq, type);
			filters.add(typeFilter);
		}

		Page<Enterprise> page = enterpriseService.findPage(beginDate,endDate,pageable);
		return Message.success(PageModel.bind(page), "admin.list.success");
	}
	
	
	/**
	 * 地区视图
	 */
	@RequestMapping(value = "/areaView", method = RequestMethod.GET)
	public String areaView(Long id, ModelMap model) {


		model.addAttribute("area",areaService.find(id));
		return "/admin/enterprise/view/areaView";
	}



}