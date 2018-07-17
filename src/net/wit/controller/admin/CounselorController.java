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
import net.wit.entity.Counselor;
import net.wit.service.CounselorService;

import java.util.*;

import net.wit.*;

import net.wit.entity.*;
import net.wit.service.*;
import net.wit.controller.admin.model.*;



/**
 * @ClassName: CounselorController
 * @author 降魔战队
 * @date 2018-7-13 14:38:49
 */
 
@Controller("adminCounselorController")
@RequestMapping("/admin/counselor")
public class CounselorController extends BaseController {
	@Resource(name = "counselorServiceImpl")
	private CounselorService counselorService;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "enterpriseServiceImpl")
	private EnterpriseService enterpriseService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "topicServiceImpl")
	private TopicService topicService;

	@Resource(name = "occupationServiceImpl")
	private OccupationService occupationService;

	@Resource(name = "tagServiceImpl")
	private TagService tagService;

	@Resource(name = "hostServiceImpl")
	private HostService hostService;


	@Resource(name = "adminServiceImpl")
	private AdminService adminService;


	/**
	 * 主页
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model) {

		List<MapEntity> statuss = new ArrayList<>();
		statuss.add(new MapEntity("enabled", "开启"));
		statuss.add(new MapEntity("disabled", "关闭"));
		model.addAttribute("statuss", statuss);

		model.addAttribute("tags", tagService.findList(Tag.Type.counselor));

		return "/admin/counselor/list";
	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {

		List<MapEntity> statuss = new ArrayList<>();
		statuss.add(new MapEntity("enabled", "开启"));
		statuss.add(new MapEntity("disabled", "关闭"));
		model.addAttribute("statuss", statuss);

		model.addAttribute("tags", tagService.findList(Tag.Type.counselor));

		return "/admin/counselor/add";
	}


	/**
	 * 保存
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public Message save(Counselor counselor, Long [] tagIds,Long memberId) {

		Admin admin = adminService.getCurrent();

		Counselor entity = new Counselor();

		entity.setOrders(counselor.getOrders() == null ? 0 : counselor.getOrders());
		entity.setTags(tagService.findList(tagIds));

		entity.setAutograph(counselor.getAutograph());

		entity.setContent(counselor.getContent());

		entity.setDeleted(false);

		entity.setLogo(counselor.getLogo());

		entity.setName(counselor.getName());

		entity.setPhone(counselor.getPhone());

		entity.setSpeciality(counselor.getSpeciality());

		entity.setStatus(counselor.getStatus());

		entity.setEnterprise(admin.getEnterprise());

		entity.setMember(memberService.find(memberId));

		if (!isValid(entity)) {
			return Message.error("admin.data.valid");
		}
		try {
			counselorService.save(entity);
			return Message.success(entity, "admin.save.success");
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
			counselorService.delete(ids);
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

		List<MapEntity> statuss = new ArrayList<>();
		statuss.add(new MapEntity("enabled", "开启"));
		statuss.add(new MapEntity("disabled", "关闭"));
		model.addAttribute("statuss", statuss);

		model.addAttribute("tags", tagService.findList(Tag.Type.counselor));

		model.addAttribute("data", counselorService.find(id));

		return "/admin/counselor/edit";
	}


	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public Message update(Counselor counselor,Long [] tagIds, Long memberId) {
		Admin admin = adminService.getCurrent();

		Counselor entity = counselorService.find(counselor.getId());

		entity.setOrders(counselor.getOrders() == null ? 0 : counselor.getOrders());
		entity.setTags(tagService.findList(tagIds));

		entity.setAutograph(counselor.getAutograph());

		entity.setContent(counselor.getContent());

		entity.setDeleted(false);

		entity.setLogo(counselor.getLogo());

		entity.setName(counselor.getName());

		entity.setPhone(counselor.getPhone());

		entity.setSpeciality(counselor.getSpeciality());

		entity.setStatus(counselor.getStatus());

		entity.setEnterprise(admin.getEnterprise());

		entity.setMember(memberService.find(memberId));

		if (!isValid(entity)) {
			return Message.error("admin.data.valid");
		}

		try {
			counselorService.update(entity);
			return Message.success(entity, "admin.update.success");
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
	public Message list(Date beginDate, Date endDate, Counselor.Status status, Pageable pageable, ModelMap model) {
		Admin admin = adminService.getCurrent();
		ArrayList<Filter> filters = (ArrayList<Filter>) pageable.getFilters();
		if (status != null) {
			Filter statusFilter = new Filter("status", Filter.Operator.eq, status);
			filters.add(statusFilter);
		}

		//非超级管理员都只能管本企业用户
		if (!admin.getId().equals(1L)) {
			filters.add(new Filter("enterprise", Filter.Operator.eq, admin.getEnterprise()));
		}

		Page<Counselor> page = counselorService.findPage(beginDate, endDate, pageable);
		return Message.success(PageBlock.bind(page), "admin.list.success");
	}


}