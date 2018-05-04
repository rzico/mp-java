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
import net.wit.entity.LiveGiftExchange;
import net.wit.service.LiveGiftExchangeService;

import java.util.*;

import net.wit.*;

import net.wit.entity.*;
import net.wit.service.*;
import net.wit.controller.admin.model.*;



/**
 * @ClassName: LiveGiftExchangeController
 * @author 降魔战队
 * @date 2018-4-28 22:28:52
 */
 
@Controller("adminLiveGiftExchangeController")
@RequestMapping("/admin/liveGiftExchange")
public class LiveGiftExchangeController extends BaseController {

	@Resource(name = "liveGiftExchangeServiceImpl")
	private LiveGiftExchangeService liveGiftExchangeService;
	
	@Resource(name = "liveServiceImpl")
	private LiveService liveService;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "liveTapeServiceImpl")
	private LiveTapeService liveTapeService;

	@Resource(name = "liveGroupServiceImpl")
	private LiveGroupService liveGroupService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "topicServiceImpl")
	private TopicService topicService;

	@Resource(name = "occupationServiceImpl")
	private OccupationService occupationService;

	@Resource(name = "enterpriseServiceImpl")
	private EnterpriseService enterpriseService;

	@Resource(name = "tagServiceImpl")
	private TagService tagService;

	/**
	 * 主页
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model) {

		return "/admin/liveGiftExchange/list";
	}

	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {

		return "/admin/liveGiftExchange/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(LiveGiftExchange liveGiftExchange, Long liveId, Long memberId){
		LiveGiftExchange entity = new LiveGiftExchange();	

		entity.setCreateDate(liveGiftExchange.getCreateDate());

		entity.setModifyDate(liveGiftExchange.getModifyDate());

		entity.setAmount(liveGiftExchange.getAmount());

		entity.setGift(liveGiftExchange.getGift() == null ? 0 : liveGiftExchange.getGift());

		entity.setHeadpic(liveGiftExchange.getHeadpic());

		entity.setNickname(liveGiftExchange.getNickname());

		entity.setThumbnail(liveGiftExchange.getThumbnail());

		entity.setLive(liveService.find(liveId));

		entity.setMember(memberService.find(memberId));
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            liveGiftExchangeService.save(entity);
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
            liveGiftExchangeService.delete(ids);
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


		model.addAttribute("data",liveGiftExchangeService.find(id));

		return "/admin/liveGiftExchange/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(LiveGiftExchange liveGiftExchange, Long liveId, Long memberId){
		LiveGiftExchange entity = liveGiftExchangeService.find(liveGiftExchange.getId());
		
		entity.setCreateDate(liveGiftExchange.getCreateDate());

		entity.setModifyDate(liveGiftExchange.getModifyDate());

		entity.setAmount(liveGiftExchange.getAmount());

		entity.setGift(liveGiftExchange.getGift() == null ? 0 : liveGiftExchange.getGift());

		entity.setHeadpic(liveGiftExchange.getHeadpic());

		entity.setNickname(liveGiftExchange.getNickname());

		entity.setThumbnail(liveGiftExchange.getThumbnail());

		entity.setLive(liveService.find(liveId));

		entity.setMember(memberService.find(memberId));
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            liveGiftExchangeService.update(entity);
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

		Page<LiveGiftExchange> page = liveGiftExchangeService.findPage(beginDate,endDate,pageable);
		return Message.success(PageBlock.bind(page), "admin.list.success");
	}

	/**
	 * Live视图
	 */
	@RequestMapping(value = "/liveView", method = RequestMethod.GET)
	public String liveView(Long id, ModelMap model) {
		List<MapEntity> statuss = new ArrayList<>();
		statuss.add(new MapEntity("waiting","等待支付"));
		statuss.add(new MapEntity("success","开通成功"));
		statuss.add(new MapEntity("failure","支付失败"));
		model.addAttribute("statuss",statuss);

		model.addAttribute("liveGroups",liveGroupService.findAll());

		model.addAttribute("liveTapes",liveTapeService.findAll());

		model.addAttribute("members",memberService.findAll());

		model.addAttribute("live",liveService.find(id));
		return "/admin/liveGiftExchange/view/liveView";
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

		model.addAttribute("agents",enterpriseService.findAll());

		model.addAttribute("operates",enterpriseService.findAll());

		model.addAttribute("personals",enterpriseService.findAll());

		model.addAttribute("tags",tagService.findAll());

		model.addAttribute("member",memberService.find(id));
		return "/admin/liveGiftExchange/view/memberView";
	}



}