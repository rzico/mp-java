package net.wit.controller.admin;

import java.util.Date;

import javax.annotation.Resource;
import net.wit.Filter;
import net.wit.Message;
import net.wit.Pageable;

import net.wit.entity.GoldBuy;
import net.wit.entity.Member;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.wit.entity.GoldExchange;
import net.wit.service.GoldExchangeService;

import java.util.*;

import net.wit.*;

import net.wit.service.*;


/**
 * @ClassName: GmGoldExchangeController
 * @author 降魔战队
 * @date 2018-3-25 14:59:8
 */
 
@Controller("adminGoldExchangeController")
@RequestMapping("/admin/goldExchange")
public class GoldExchangeController extends BaseController {
	@Resource(name = "goldExchangeServiceImpl")
	private GoldExchangeService goldExchangeService;
	
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

		return "/admin/goldExchange/list";
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

		return "/admin/goldExchange/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(GoldExchange goldExchange, Long memberId){
		GoldExchange entity = new GoldExchange();

		entity.setCreateDate(goldExchange.getCreateDate());

		entity.setModifyDate(goldExchange.getModifyDate());

		entity.setAmount(goldExchange.getAmount());

		entity.setDeleted(goldExchange.getDeleted());

		entity.setGold(goldExchange.getGold());

		entity.setMemo(goldExchange.getMemo());

		entity.setOperator(goldExchange.getOperator());

		entity.setStatus(goldExchange.getStatus());

		entity.setMember(memberService.find(memberId));
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            goldExchangeService.save(entity);
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
            goldExchangeService.delete(ids);
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

		model.addAttribute("data",goldExchangeService.find(id));

		return "/admin/goldExchange/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(GoldExchange goldExchange, Long memberId){
		GoldExchange entity = goldExchangeService.find(goldExchange.getId());
		
		entity.setCreateDate(goldExchange.getCreateDate());

		entity.setModifyDate(goldExchange.getModifyDate());

		entity.setAmount(goldExchange.getAmount());

		entity.setDeleted(goldExchange.getDeleted());

		entity.setGold(goldExchange.getGold());

		entity.setMemo(goldExchange.getMemo());

		entity.setOperator(goldExchange.getOperator());

		entity.setStatus(goldExchange.getStatus());

		entity.setMember(memberService.find(memberId));
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            goldExchangeService.update(entity);
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
	public Message list(Date beginDate, Date endDate, GoldExchange.Status status, Pageable pageable, ModelMap model) {
		ArrayList<Filter> filters = (ArrayList<Filter>) pageable.getFilters();
		if (status!=null) {
			Filter statusFilter = new Filter("status", Filter.Operator.eq, status);
			filters.add(statusFilter);
		}
		if (pageable.getSearchValue()!=null) {
			Member member = memberService.findByMobile(pageable.getSearchValue());
			if (member!=null) {
				Filter memberFilter = new Filter("member", Filter.Operator.eq, member);
				filters.add(memberFilter);
			} else {
				return Message.success(PageBlock.bind(new Page<GoldExchange>(new ArrayList<GoldExchange>(),0, pageable)), "admin.list.success");
			}
		}

		Page<GoldExchange> page = goldExchangeService.findPage(beginDate,endDate,pageable);
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
		return "/admin/goldExchange/view/memberView";
	}



}