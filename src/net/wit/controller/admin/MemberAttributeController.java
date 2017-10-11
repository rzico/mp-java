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
import net.wit.entity.MemberAttribute;
import net.wit.service.MemberAttributeService;

import java.util.*;

import net.wit.*;

import net.wit.entity.*;
import net.wit.service.*;
import net.wit.controller.admin.model.*;



/**
 * @ClassName: MemberAttributeController
 * @author 降魔战队
 * @date 2017-10-11 15:37:10
 */
 
@Controller("adminMemberAttributeController")
@RequestMapping("/admin/memberAttribute")
public class MemberAttributeController extends BaseController {
	@Resource(name = "memberAttributeServiceImpl")
	private MemberAttributeService memberAttributeService;
	


	/**
	 * 主页
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model) {

		List<MapEntity> types = new ArrayList<>();
		types.add(new MapEntity("name","姓名"));
		types.add(new MapEntity("gender","性别"));
		types.add(new MapEntity("birth","出生日期"));
		types.add(new MapEntity("area","地区"));
		types.add(new MapEntity("address","地址"));
		types.add(new MapEntity("zipCode","邮编"));
		types.add(new MapEntity("phone","电话"));
		types.add(new MapEntity("mobile","手机"));
		types.add(new MapEntity("text","文本"));
		types.add(new MapEntity("select","单选项"));
		types.add(new MapEntity("checkbox","多选项"));
		model.addAttribute("types",types);

		return "/admin/memberAttribute/list";
	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {

		List<MapEntity> types = new ArrayList<>();
		types.add(new MapEntity("name","姓名"));
		types.add(new MapEntity("gender","性别"));
		types.add(new MapEntity("birth","出生日期"));
		types.add(new MapEntity("area","地区"));
		types.add(new MapEntity("address","地址"));
		types.add(new MapEntity("zipCode","邮编"));
		types.add(new MapEntity("phone","电话"));
		types.add(new MapEntity("mobile","手机"));
		types.add(new MapEntity("text","文本"));
		types.add(new MapEntity("select","单选项"));
		types.add(new MapEntity("checkbox","多选项"));
		model.addAttribute("types",types);

		return "/admin/memberAttribute/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(MemberAttribute memberAttribute){
		MemberAttribute entity = new MemberAttribute();	

		entity.setCreateDate(memberAttribute.getCreateDate());

		entity.setModifyDate(memberAttribute.getModifyDate());

		entity.setOrders(memberAttribute.getOrders() == null ? 0 : memberAttribute.getOrders());

		entity.setIsEnabled(memberAttribute.getIsEnabled());

		entity.setIsRequired(memberAttribute.getIsRequired());

		entity.setName(memberAttribute.getName());

		entity.setPropertyIndex(memberAttribute.getPropertyIndex() == null ? 0 : memberAttribute.getPropertyIndex());

		entity.setType(memberAttribute.getType());
		
		if (!isValid(entity, Save.class)) {
            return Message.error("admin.data.valid");
        }
        try {
            memberAttributeService.save(entity);
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
            memberAttributeService.delete(ids);
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
		types.add(new MapEntity("name","姓名"));
		types.add(new MapEntity("gender","性别"));
		types.add(new MapEntity("birth","出生日期"));
		types.add(new MapEntity("area","地区"));
		types.add(new MapEntity("address","地址"));
		types.add(new MapEntity("zipCode","邮编"));
		types.add(new MapEntity("phone","电话"));
		types.add(new MapEntity("mobile","手机"));
		types.add(new MapEntity("text","文本"));
		types.add(new MapEntity("select","单选项"));
		types.add(new MapEntity("checkbox","多选项"));
		model.addAttribute("types",types);

		model.addAttribute("data",memberAttributeService.find(id));

		return "/admin/memberAttribute/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(MemberAttribute memberAttribute){
		MemberAttribute entity = memberAttributeService.find(memberAttribute.getId());
		
		entity.setCreateDate(memberAttribute.getCreateDate());

		entity.setModifyDate(memberAttribute.getModifyDate());

		entity.setOrders(memberAttribute.getOrders() == null ? 0 : memberAttribute.getOrders());

		entity.setIsEnabled(memberAttribute.getIsEnabled());

		entity.setIsRequired(memberAttribute.getIsRequired());

		entity.setName(memberAttribute.getName());

		entity.setPropertyIndex(memberAttribute.getPropertyIndex() == null ? 0 : memberAttribute.getPropertyIndex());

		entity.setType(memberAttribute.getType());
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            memberAttributeService.update(entity);
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
	public Message list(Date beginDate, Date endDate, MemberAttribute.Type type, Pageable pageable, ModelMap model) {	
		ArrayList<Filter> filters = (ArrayList<Filter>) pageable.getFilters();
		if (type!=null) {
			Filter typeFilter = new Filter("type", Filter.Operator.eq, type);
			filters.add(typeFilter);
		}

		Page<MemberAttribute> page = memberAttributeService.findPage(beginDate,endDate,pageable);
		return Message.success(PageBlock.bind(page), "admin.list.success");
	}
	
	

}