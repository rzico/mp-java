/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.plugin.ftp;

import javax.annotation.Resource;

import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.entity.PluginConfig;
import net.wit.service.PluginConfigService;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - FTP
 * 
 * @author rsico Team
 * @version 3.0
 */
@Controller("adminPluginFtpController")
@RequestMapping("/admin/storage_plugin/ftp")
public class FtpController extends BaseController {

	@Resource(name = "ftpPlugin")
	private FtpPlugin ftpPlugin;
	@Resource(name = "pluginConfigServiceImpl")
	private PluginConfigService pluginConfigService;

	/**
	 * 安装
	 */
	@RequestMapping(value = "/install", method = RequestMethod.POST)
	public @ResponseBody
	Message install() {
		if (!ftpPlugin.getIsInstalled()) {
			PluginConfig pluginConfig = new PluginConfig();
			pluginConfig.setPluginId(ftpPlugin.getId());
			pluginConfig.setIsEnabled(false);
			pluginConfigService.save(pluginConfig);
		}
		return Message.success("admin.save.success");
	}

	/**
	 * 卸载
	 */
	@RequestMapping(value = "/uninstall", method = RequestMethod.POST)
	public @ResponseBody
	Message uninstall() {
		if (ftpPlugin.getIsInstalled()) {
			PluginConfig pluginConfig = ftpPlugin.getPluginConfig();
			pluginConfigService.delete(pluginConfig);
		}
		return Message.success("admin.save.success");
	}

	/**
	 * 设置
	 */
	@RequestMapping(value = "/setting", method = RequestMethod.GET)
	public String setting(ModelMap model) {
		PluginConfig pluginConfig = ftpPlugin.getPluginConfig();
		model.addAttribute("pluginConfig", pluginConfig);
		return "/net/wit/plugin/ftp/setting";
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(String host, Integer port, String username, String password, String urlPrefix, @RequestParam(defaultValue = "false") Boolean isEnabled, Integer order, RedirectAttributes redirectAttributes) {
		PluginConfig pluginConfig = ftpPlugin.getPluginConfig();
		pluginConfig.setAttribute("host", host);
		pluginConfig.setAttribute("port", port.toString());
		pluginConfig.setAttribute("username", username);
		pluginConfig.setAttribute("password", password);
		pluginConfig.setAttribute("urlPrefix", StringUtils.removeEnd(urlPrefix, "/"));
		pluginConfig.setIsEnabled(isEnabled);
		pluginConfig.setOrders(order);
		pluginConfigService.update(pluginConfig);
		return "redirect:/admin/storage_plugin/list.jhtml";
	}

}