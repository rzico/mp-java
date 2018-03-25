package net.wit.controller.admin;

import java.util.Date;

import javax.annotation.Resource;
import net.wit.Filter;
import net.wit.Message;
import net.wit.Pageable;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.wit.entity.GoldBuy;
import net.wit.service.GoldBuyService;

import java.util.*;

import net.wit.*;

import net.wit.service.*;


/**
 * @ClassName: GoldBuyController
 * @author 降魔战队
 * @date 2018-3-25 14:59:8
 */
 
@Controller("adminGoldBuyController")
@RequestMapping("/admin/goldBuy")
public class GoldBuyController extends BaseController {
	@Resource(name = "goldBuyServiceImpl")
	private GoldBuyService goldBuyService;
	
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

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

		List<MapEntity> statuss = new ArrayList<>();
		statuss.add(new MapEntity("none","待支付"));
		statuss.add(new MapEntity("success","支付成功"));
		statuss.add(new MapEntity("failure","支付失败"));
		model.addAttribute("statuss",statuss);

		return "/admin/goldBuy/list";
	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {

		List<MapEntity> statuss = new ArrayList<>();
		statuss.add(new MapEntity("none","待支付"));
		statuss.add(new MapEntity("success","支付成功"));
		statuss.add(new MapEntity("failure","支付失败"));
		model.addAttribute("statuss",statuss);

		return "/admin/goldBuy/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(GoldBuy goldBuy, Long memberId){
		GoldBuy entity = new GoldBuy();

		entity.setCreateDate(goldBuy.getCreateDate());

		entity.setModifyDate(goldBuy.getModifyDate());

		entity.setAmount(goldBuy.getAmount());

		entity.setDeleted(goldBuy.getDeleted());

		entity.setGold(goldBuy.getGold());

		entity.setMemo(goldBuy.getMemo());

		entity.setOperator(goldBuy.getOperator());

		entity.setStatus(goldBuy.getStatus());

		entity.setMember(memberService.find(memberId));
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            goldBuyService.save(entity);
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
            goldBuyService.delete(ids);
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
		statuss.add(new MapEntity("none","待支付"));
		statuss.add(new MapEntity("success","支付成功"));
		statuss.add(new MapEntity("failure","支付失败"));
		model.addAttribute("statuss",statuss);

		model.addAttribute("data",goldBuyService.find(id));

		return "/admin/gmGoldBuy/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(GoldBuy goldBuy, Long memberId){
		GoldBuy entity = goldBuyService.find(goldBuy.getId());
		
		entity.setCreateDate(goldBuy.getCreateDate());

		entity.setModifyDate(goldBuy.getModifyDate());

		entity.setAmount(goldBuy.getAmount());

		entity.setDeleted(goldBuy.getDeleted());

		entity.setGold(goldBuy.getGold());

		entity.setMemo(goldBuy.getMemo());

		entity.setOperator(goldBuy.getOperator());

		entity.setStatus(goldBuy.getStatus());

		entity.setMember(memberService.find(memberId));
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            goldBuyService.update(entity);
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
	public Message list(Date beginDate, Date endDate, GoldBuy.Status status, Pageable pageable, ModelMap model) {
		ArrayList<Filter> filters = (ArrayList<Filter>) pageable.getFilters();
		if (status!=null) {
			Filter statusFilter = new Filter("status", Filter.Operator.eq, status);
			filters.add(statusFilter);
		}

		Page<GoldBuy> page = goldBuyService.findPage(beginDate,endDate,pageable);
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
		return "/admin/gmGoldBuy/view/memberView";
	}



}