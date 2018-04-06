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
import net.wit.entity.Live;
import net.wit.service.LiveService;

import java.util.*;

import net.wit.*;

import net.wit.entity.*;
import net.wit.service.*;
import net.wit.controller.admin.model.*;



/**
 * @ClassName: LiveController
 * @author 降魔战队
 * @date 2018-4-5 18:22:45
 */
 
@Controller("adminLiveController")
@RequestMapping("/admin/live")
public class LiveController extends BaseController {
	@Resource(name = "liveServiceImpl")
	private LiveService liveService;
	
	@Resource(name = "liveTapeServiceImpl")
	private LiveTapeService liveTapeService;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "liveGroupServiceImpl")
	private LiveGroupService liveGroupService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "topicServiceImpl")
	private TopicService topicService;

	@Resource(name = "occupationServiceImpl")
	private OccupationService occupationService;

	@Resource(name = "tagServiceImpl")
	private TagService tagService;



	/**
	 * 主页
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model) {

		model.addAttribute("liveGroups",liveGroupService.findAll());

		model.addAttribute("liveTapes",liveTapeService.findAll());

		model.addAttribute("members",memberService.findAll());

		return "/admin/live/list";
	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {

		model.addAttribute("liveGroups",liveGroupService.findAll());

		model.addAttribute("liveTapes",liveTapeService.findAll());

		model.addAttribute("members",memberService.findAll());

		return "/admin/live/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(Live live, Long liveTapeId, Long memberId, Long liveGroupId){
		Live entity = new Live();	

		entity.setCreateDate(live.getCreateDate());

		entity.setModifyDate(live.getModifyDate());

		entity.setFrontcover(live.getFrontcover());

		entity.setGift(live.getGift() == null ? 0 : live.getGift());

		entity.setHeadpic(live.getHeadpic());

		entity.setHlsPlayUrl(live.getHlsPlayUrl());

		entity.setLikeCount(live.getLikeCount() == null ? 0 : live.getLikeCount());

		entity.setLocation(live.getLocation());

		entity.setNickname(live.getNickname());

		entity.setPlayUrl(live.getPlayUrl());

		entity.setPushUrl(live.getPushUrl());

		entity.setStatus(live.getStatus());

		entity.setTitle(live.getTitle());

		entity.setViewerCount(live.getViewerCount() == null ? 0 : live.getViewerCount());

		entity.setLiveTape(liveTapeService.find(liveTapeId));

		entity.setMember(memberService.find(memberId));
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            liveService.save(entity);
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
            liveService.delete(ids);
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

		model.addAttribute("liveGroups",liveGroupService.findAll());

		model.addAttribute("liveTapes",liveTapeService.findAll());

		model.addAttribute("members",memberService.findAll());

		model.addAttribute("data",liveService.find(id));

		return "/admin/live/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(Live live, Long liveTapeId, Long memberId, Long liveGroupId){
		Live entity = liveService.find(live.getId());
		
		entity.setCreateDate(live.getCreateDate());

		entity.setModifyDate(live.getModifyDate());

		entity.setFrontcover(live.getFrontcover());

		entity.setGift(live.getGift() == null ? 0 : live.getGift());

		entity.setHeadpic(live.getHeadpic());

		entity.setHlsPlayUrl(live.getHlsPlayUrl());

		entity.setLikeCount(live.getLikeCount() == null ? 0 : live.getLikeCount());

		entity.setLocation(live.getLocation());

		entity.setNickname(live.getNickname());

		entity.setPlayUrl(live.getPlayUrl());

		entity.setPushUrl(live.getPushUrl());

		entity.setStatus(live.getStatus());

		entity.setTitle(live.getTitle());

		entity.setViewerCount(live.getViewerCount() == null ? 0 : live.getViewerCount());

		entity.setLiveTape(liveTapeService.find(liveTapeId));

		entity.setMember(memberService.find(memberId));
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            liveService.update(entity);
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

		Page<Live> page = liveService.findPage(beginDate,endDate,pageable);
		return Message.success(PageBlock.bind(page), "admin.list.success");
	}
	
	
	/**
	 * LiveTape视图
	 */
	@RequestMapping(value = "/liveTapeView", method = RequestMethod.GET)
	public String liveTapeView(Long id, ModelMap model) {
		model.addAttribute("liveGroups",liveGroupService.findAll());

		model.addAttribute("members",memberService.findAll());

		model.addAttribute("liveTape",liveTapeService.find(id));
		return "/admin/live/view/liveTapeView";
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

		model.addAttribute("occupations",occupationService.findAll());

		model.addAttribute("topics",topicService.findAll());

		List<MapEntity> vips = new ArrayList<>();
		vips.add(new MapEntity("vip1","vip1"));
		vips.add(new MapEntity("vip2","vip2"));
		vips.add(new MapEntity("vip3","vip3"));
		model.addAttribute("vips",vips);

		model.addAttribute("tags",tagService.findAll());

		model.addAttribute("member",memberService.find(id));
		return "/admin/live/view/memberView";
	}


	/**
	 * LiveGroup视图
	 */
	@RequestMapping(value = "/liveGroupView", method = RequestMethod.GET)
	public String liveGroupView(Long id, ModelMap model) {
		model.addAttribute("liveMembers",memberService.findAll());

		model.addAttribute("members",memberService.findAll());

		model.addAttribute("liveGroup",liveGroupService.find(id));
		return "/admin/live/view/liveGroupView";
	}



}