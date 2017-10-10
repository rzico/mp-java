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
import net.wit.entity.PluginConfig;
import net.wit.service.PluginConfigService;

import java.util.*;

import net.wit.*;

import net.wit.entity.*;
import net.wit.service.*;
import net.wit.controller.admin.model.*;



/**
 * @ClassName: PluginConfigController
 * @author 降魔战队
 * @date 2017-9-14 19:42:15
 */
 
@Controller("adminPluginConfigController")
@RequestMapping("/admin/pluginConfig")
public class PluginConfigController extends BaseController {
	@Resource(name = "pluginConfigServiceImpl")
	private PluginConfigService pluginConfigService;
	


	/**
	 * 主页
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model) {



		return "/admin/pluginConfig/list";
	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {



		return "/admin/pluginConfig/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(PluginConfig pluginConfig){
		PluginConfig entity = new PluginConfig();	

		entity.setCreateDate(pluginConfig.getCreateDate());

		entity.setModifyDate(pluginConfig.getModifyDate());

		entity.setOrders(pluginConfig.getOrders() == null ? 0 : pluginConfig.getOrders());

		entity.setIsEnabled(pluginConfig.getIsEnabled());

		entity.setPluginId(pluginConfig.getPluginId());
		
		if (!isValid(entity, Save.class)) {
            return Message.error("admin.data.valid");
        }
        try {
            pluginConfigService.save(entity);
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
            pluginConfigService.delete(ids);
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



		model.addAttribute("data",pluginConfigService.find(id));

		return "/admin/pluginConfig/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(PluginConfig pluginConfig){
		PluginConfig entity = pluginConfigService.find(pluginConfig.getId());
		
		entity.setCreateDate(pluginConfig.getCreateDate());

		entity.setModifyDate(pluginConfig.getModifyDate());

		entity.setOrders(pluginConfig.getOrders() == null ? 0 : pluginConfig.getOrders());

		entity.setIsEnabled(pluginConfig.getIsEnabled());

		entity.setPluginId(pluginConfig.getPluginId());
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            pluginConfigService.update(entity);
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
	public Message list(Date beginDate, Date endDate, Pageable pageable, ModelMap model) {

		Page<PluginConfig> page = pluginConfigService.findPage(beginDate,endDate,pageable);
		return Message.success(PageBlock.bind(page), "admin.list.success");
	}
	
	

}