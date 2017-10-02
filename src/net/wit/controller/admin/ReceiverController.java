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
import net.wit.entity.Receiver;
import net.wit.service.ReceiverService;

import java.util.*;

import net.wit.*;

import net.wit.entity.*;
import net.wit.service.*;
import net.wit.controller.admin.model.*;



/**
 * @ClassName: ReceiverController
 * @author 降魔战队
 * @date 2017-9-14 19:42:16
 */
 
@Controller("adminReceiverController")
@RequestMapping("/admin/receiver")
public class ReceiverController extends BaseController {
	@Resource(name = "receiverServiceImpl")
	private ReceiverService receiverService;
	
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "tagServiceImpl")
	private TagService tagService;



	/**
	 * 主页
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model) {

		model.addAttribute("areas",areaService.findAll());

		model.addAttribute("members",memberService.findAll());

		return "/admin/receiver/list";
	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {

		model.addAttribute("areas",areaService.findAll());

		model.addAttribute("members",memberService.findAll());

		return "/admin/receiver/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(Receiver receiver, Long memberId, Long areaId){
		Receiver entity = new Receiver();	

		entity.setCreateDate(receiver.getCreateDate());

		entity.setModifyDate(receiver.getModifyDate());

		entity.setAddress(receiver.getAddress());

		entity.setAreaName(receiver.getAreaName());

		entity.setConsignee(receiver.getConsignee());

		entity.setIsDefault(receiver.getIsDefault());

		entity.setPhone(receiver.getPhone());

		entity.setZipCode(receiver.getZipCode());

		entity.setArea(areaService.find(areaId));

		entity.setMember(memberService.find(memberId));
		
		if (!isValid(entity, Save.class)) {
            return Message.error("admin.data.valid");
        }
        try {
            receiverService.save(entity);
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
            receiverService.delete(ids);
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

		model.addAttribute("areas",areaService.findAll());

		model.addAttribute("members",memberService.findAll());

		model.addAttribute("data",receiverService.find(id));

		return "/admin/receiver/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(Receiver receiver, Long memberId, Long areaId){
		Receiver entity = receiverService.find(receiver.getId());
		
		entity.setCreateDate(receiver.getCreateDate());

		entity.setModifyDate(receiver.getModifyDate());

		entity.setAddress(receiver.getAddress());

		entity.setAreaName(receiver.getAreaName());

		entity.setConsignee(receiver.getConsignee());

		entity.setIsDefault(receiver.getIsDefault());

		entity.setPhone(receiver.getPhone());

		entity.setZipCode(receiver.getZipCode());

		entity.setArea(areaService.find(areaId));

		entity.setMember(memberService.find(memberId));
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            receiverService.update(entity);
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

		Page<Receiver> page = receiverService.findPage(beginDate,endDate,pageable);
		return Message.success(PageBlock.bind(page), "admin.list.success");
	}
	
	
	/**
	 * 会员管理视图
	 */
	@RequestMapping(value = "/memberView", method = RequestMethod.GET)
	public String memberView(Long id, ModelMap model) {
		List<MapEntity> genders = new ArrayList<>();
		genders.add(new MapEntity("male","男"));
		genders.add(new MapEntity("female","女"));
		genders.add(new MapEntity("secrecy","保密"));
		model.addAttribute("genders",genders);

		model.addAttribute("areas",areaService.findAll());

		model.addAttribute("tags",tagService.findAll());

		model.addAttribute("member",memberService.find(id));
		return "/admin/receiver/view/memberView";
	}


	/**
	 * 地区视图
	 */
	@RequestMapping(value = "/areaView", method = RequestMethod.GET)
	public String areaView(Long id, ModelMap model) {


		model.addAttribute("area",areaService.find(id));
		return "/admin/receiver/view/areaView";
	}



}