/*
 * Copyright 2005-2013 shopxx.net. All rights reserved.
 * Support: http://www.shopxx.net
 * License: http://www.shopxx.net/license
 */
package net.wit.controller.admin;

import java.util.Date;
import java.util.HashSet;

import javax.annotation.Resource;

import net.wit.Message;
import net.wit.Pageable;
import net.wit.entity.Admin;
import net.wit.entity.BaseEntity.Save;
import net.wit.entity.Role;
import net.wit.service.AdminService;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 管理员
 * 
 * @author SHOP++ Team
 * @version 3.0
 */
@Controller("adminAdminController")
@RequestMapping("/admin/admin")
public class AdminController extends BaseController {

	@Resource(name = "adminServiceImpl")
	private AdminService adminService;

	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {
		return "/admin/admin/add";
	}

	/**
	 * 保存
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public Message save(Admin admin,RedirectAttributes redirectAttributes) {
		Admin entity = new Admin();
		//正常赋值
		entity.setEmail(admin.getEmail());
		//多对一赋值

		if (!isValid(admin, Save.class)) {
			return Message.error("admin.data.valid");
		}
		try {
			adminService.save(entity);
			return Message.success("admin.save.success");
		} catch (Exception e) {
			e.printStackTrace();
			return Message.error("admin.save.error");
		}
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		model.addAttribute("admin", adminService.find(id));
		return "/admin/admin/edit";
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public Message update(Admin admin) {
		Admin entity = adminService.find(admin.getId());
		//正常赋值
		entity.setEmail(admin.getEmail());
		//多对一赋值
		if (!isValid(entity)) {
			return Message.error("admin.data.valid");
		}
		try {
			adminService.save(entity);
			return Message.success("admin.update.success");
		} catch (Exception e) {
			e.printStackTrace();
			return Message.error("admin.update.error");
		}
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Date beginDate, Date endDate, Pageable pageable, ModelMap model) {
		model.addAttribute("page", adminService.findPage(beginDate,endDate,pageable));
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