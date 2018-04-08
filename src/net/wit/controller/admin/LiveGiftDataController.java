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
import net.wit.entity.LiveGiftData;
import net.wit.service.LiveGiftDataService;

import java.util.*;

import net.wit.*;

import net.wit.entity.*;
import net.wit.service.*;
import net.wit.controller.admin.model.*;



/**
 * @ClassName: LiveGiftDataController
 * @author 降魔战队
 * @date 2018-4-5 18:22:46
 */
 
@Controller("adminLiveGiftDataController")
@RequestMapping("/admin/liveGiftData")
public class LiveGiftDataController extends BaseController {
	@Resource(name = "liveGiftDataServiceImpl")
	private LiveGiftDataService liveGiftDataService;
	
	@Resource(name = "liveGiftServiceImpl")
	private LiveGiftService liveGiftService;

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

		model.addAttribute("liveGifts",liveGiftService.findAll());

		model.addAttribute("liveTapes",liveTapeService.findAll());

		model.addAttribute("members",memberService.findAll());

		return "/admin/liveGiftData/list";
	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {

		model.addAttribute("liveGifts",liveGiftService.findAll());

		model.addAttribute("liveTapes",liveTapeService.findAll());

		model.addAttribute("members",memberService.findAll());

		return "/admin/liveGiftData/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(LiveGiftData liveGiftData, Long liveGiftId, Long liveTapeId, Long memberId){
		LiveGiftData entity = new LiveGiftData();	

		entity.setCreateDate(liveGiftData.getCreateDate());

		entity.setModifyDate(liveGiftData.getModifyDate());

		entity.setGiftName(liveGiftData.getGiftName());

		entity.setHeadpic(liveGiftData.getHeadpic());

		entity.setNickname(liveGiftData.getNickname());

		entity.setPrice(liveGiftData.getPrice());

		entity.setLiveGift(liveGiftService.find(liveGiftId));

		entity.setLiveTape(liveTapeService.find(liveTapeId));

		entity.setMember(memberService.find(memberId));
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            liveGiftDataService.save(entity);
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
            liveGiftDataService.delete(ids);
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

		model.addAttribute("liveGifts",liveGiftService.findAll());

		model.addAttribute("liveTapes",liveTapeService.findAll());

		model.addAttribute("members",memberService.findAll());

		model.addAttribute("data",liveGiftDataService.find(id));

		return "/admin/liveGiftData/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(LiveGiftData liveGiftData, Long liveGiftId, Long liveTapeId, Long memberId){
		LiveGiftData entity = liveGiftDataService.find(liveGiftData.getId());
		
		entity.setCreateDate(liveGiftData.getCreateDate());

		entity.setModifyDate(liveGiftData.getModifyDate());

		entity.setGiftName(liveGiftData.getGiftName());

		entity.setHeadpic(liveGiftData.getHeadpic());

		entity.setNickname(liveGiftData.getNickname());

		entity.setPrice(liveGiftData.getPrice());

		entity.setLiveGift(liveGiftService.find(liveGiftId));

		entity.setLiveTape(liveTapeService.find(liveTapeId));

		entity.setMember(memberService.find(memberId));
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            liveGiftDataService.update(entity);
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

		Page<LiveGiftData> page = liveGiftDataService.findPage(beginDate,endDate,pageable);
		return Message.success(PageBlock.bind(page), "admin.list.success");
	}
	
	
	/**
	 * LiveGift视图
	 */
	@RequestMapping(value = "/liveGiftView", method = RequestMethod.GET)
	public String liveGiftView(Long id, ModelMap model) {


		model.addAttribute("liveGift",liveGiftService.find(id));
		return "/admin/liveGiftData/view/liveGiftView";
	}


	/**
	 * LiveTape视图
	 */
	@RequestMapping(value = "/liveTapeView", method = RequestMethod.GET)
	public String liveTapeView(Long id, ModelMap model) {
		model.addAttribute("liveGroups",liveGroupService.findAll());

		model.addAttribute("members",memberService.findAll());

		model.addAttribute("liveTape",liveTapeService.find(id));
		return "/admin/liveGiftData/view/liveTapeView";
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
		return "/admin/liveGiftData/view/memberView";
	}



}