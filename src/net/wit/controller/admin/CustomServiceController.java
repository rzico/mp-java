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
import net.wit.entity.CustomService;
import net.wit.service.CustomServiceService;

import java.util.*;

import net.wit.*;

import net.wit.entity.*;
import net.wit.service.*;
import net.wit.controller.admin.model.*;



/**
 * @ClassName: CustomServiceController
 * @author 降魔战队
 * @date 2018-2-3 21:3:57
 */
 
@Controller("adminCustomServiceController")
@RequestMapping("/admin/customService")
public class CustomServiceController extends BaseController {
	@Resource(name = "customServiceServiceImpl")
	private CustomServiceService customServiceService;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;


	/**
	 * 主页
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model) {



		return "/admin/customService/list";
	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {



		return "/admin/customService/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(CustomService customService,Long memberId){
		CustomService entity = new CustomService();	

		entity.setCreateDate(customService.getCreateDate());

		entity.setModifyDate(customService.getModifyDate());

		entity.setDescription(customService.getDescription());

		entity.setName(customService.getName());

		entity.setQq(customService.getQq());

		entity.setWechat(customService.getWechat());

		entity.setMember(memberService.find(memberId));
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            customServiceService.save(entity);
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
            customServiceService.delete(ids);
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



		model.addAttribute("data",customServiceService.find(id));

		return "/admin/customService/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(CustomService customService,Long memberId){
		CustomService entity = customServiceService.find(customService.getId());
		
		entity.setCreateDate(customService.getCreateDate());

		entity.setModifyDate(customService.getModifyDate());

		entity.setDescription(customService.getDescription());

		entity.setName(customService.getName());

		entity.setQq(customService.getQq());

		entity.setWechat(customService.getWechat());

		entity.setMember(memberService.find(memberId));

		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            customServiceService.update(entity);
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

		Page<CustomService> page = customServiceService.findPage(beginDate,endDate,pageable);
		return Message.success(PageBlock.bind(page), "admin.list.success");
	}
	
	

}