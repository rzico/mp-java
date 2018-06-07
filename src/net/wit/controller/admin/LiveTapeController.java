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
import net.wit.entity.LiveTape;
import net.wit.service.LiveTapeService;

import java.util.*;

import net.wit.*;

import net.wit.entity.*;
import net.wit.service.*;
import net.wit.controller.admin.model.*;



/**
 * @ClassName: LiveTapeController
 * @author 降魔战队
 * @date 2018-4-5 18:22:46
 */
 
@Controller("adminLiveTapeController")
@RequestMapping("/admin/liveTape")
public class LiveTapeController extends BaseController {
	@Resource(name = "liveTapeServiceImpl")
	private LiveTapeService liveTapeService;
	
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "liveServiceImpl")
	private LiveService liveService;

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
	public String index(Long liveId,ModelMap model) {

		model.addAttribute("liveId",liveId);

		return "/admin/liveTape/list";
	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {

		return "/admin/liveTape/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(LiveTape liveTape, Long memberId, Long liveGroupId){
		LiveTape entity = new LiveTape();	

		entity.setCreateDate(liveTape.getCreateDate());

		entity.setModifyDate(liveTape.getModifyDate());

		entity.setFrontcover(liveTape.getFrontcover());

		entity.setGift(liveTape.getGift() == null ? 0 : liveTape.getGift());

		entity.setHeadpic(liveTape.getHeadpic());

		entity.setHlsPlayUrl(liveTape.getHlsPlayUrl());

		entity.setLikeCount(liveTape.getLikeCount() == null ? 0 : liveTape.getLikeCount());

		entity.setLocation(liveTape.getLocation());

		entity.setNickname(liveTape.getNickname());

		entity.setPlayUrl(liveTape.getPlayUrl());

		entity.setTitle(liveTape.getTitle());

		entity.setViewerCount(liveTape.getViewerCount() == null ? 0 : liveTape.getViewerCount());

		entity.setMember(memberService.find(memberId));
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            liveTapeService.save(entity);
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
            liveTapeService.delete(ids);
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

		model.addAttribute("data",liveTapeService.find(id));

		return "/admin/liveTape/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(LiveTape liveTape, Long memberId, Long liveGroupId){
		LiveTape entity = liveTapeService.find(liveTape.getId());
		
		entity.setCreateDate(liveTape.getCreateDate());

		entity.setModifyDate(liveTape.getModifyDate());

		entity.setFrontcover(liveTape.getFrontcover());

		entity.setGift(liveTape.getGift() == null ? 0 : liveTape.getGift());

		entity.setHeadpic(liveTape.getHeadpic());

		entity.setHlsPlayUrl(liveTape.getHlsPlayUrl());

		entity.setLikeCount(liveTape.getLikeCount() == null ? 0 : liveTape.getLikeCount());

		entity.setLocation(liveTape.getLocation());

		entity.setNickname(liveTape.getNickname());

		entity.setPlayUrl(liveTape.getPlayUrl());

		entity.setTitle(liveTape.getTitle());

		entity.setViewerCount(liveTape.getViewerCount() == null ? 0 : liveTape.getViewerCount());

		entity.setMember(memberService.find(memberId));
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            liveTapeService.update(entity);
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
	public Message list(Long liveId,Date beginDate, Date endDate, Pageable pageable, ModelMap model) {

		List<Filter> filters = pageable.getFilters();
		if (liveId!=null) {
			filters.add(new Filter("live", Filter.Operator.eq, liveService.find(liveId)));
		}

		Page<LiveTape> page = liveTapeService.findPage(beginDate,endDate,pageable);
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

		model.addAttribute("occupations",occupationService.findAll());

		model.addAttribute("topics",topicService.findAll());

		List<MapEntity> vips = new ArrayList<>();
		vips.add(new MapEntity("vip1","vip1"));
		vips.add(new MapEntity("vip2","vip2"));
		vips.add(new MapEntity("vip3","vip3"));
		model.addAttribute("vips",vips);

		model.addAttribute("tags",tagService.findAll());

		model.addAttribute("member",memberService.find(id));
		return "/admin/liveTape/view/memberView";
	}


	/**
	 * LiveGroup视图
	 */
	@RequestMapping(value = "/liveGroupView", method = RequestMethod.GET)
	public String liveGroupView(Long id, ModelMap model) {
		return "/admin/liveTape/view/liveGroupView";
	}



}