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

import net.wit.entity.Gold;
import net.wit.service.GoldService;

import java.util.*;

import net.wit.*;

import net.wit.service.*;


/**
 * @ClassName: GoldController
 * @author 降魔战队
 * @date 2018-3-25 14:59:8
 */
 
@Controller("adminGoldController")
@RequestMapping("/admin/gold")
public class GoldController extends BaseController {
	@Resource(name = "goldServiceImpl")
	private GoldService goldService;
	
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

		List<MapEntity> types = new ArrayList<>();
		types.add(new MapEntity("recharge","充值"));
		types.add(new MapEntity("transfer","支付"));
		types.add(new MapEntity("transaction","退款"));
		types.add(new MapEntity("history","收益"));
		types.add(new MapEntity("reward","打赏"));
		model.addAttribute("types",types);

		return "/admin/gmGold/list";
	}


	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {

		List<MapEntity> types = new ArrayList<>();
		types.add(new MapEntity("recharge","充值"));
		types.add(new MapEntity("transfer","支付"));
		types.add(new MapEntity("transaction","退款"));
		types.add(new MapEntity("history","收益"));
		types.add(new MapEntity("reward","打赏"));
		model.addAttribute("types",types);

		model.addAttribute("members",memberService.findAll());

		return "/admin/gmGold/add";
	}


	/**
     * 保存
     */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
	public Message save(Gold gold, Long memberId){
		Gold entity = new Gold();

		entity.setCreateDate(gold.getCreateDate());

		entity.setModifyDate(gold.getModifyDate());

		entity.setBalance(gold.getBalance());

		entity.setCredit(gold.getCredit());

		entity.setDebit(gold.getDebit());

		entity.setDeleted(gold.getDeleted());

		entity.setGame(gold.getGame());

		entity.setMemo(gold.getMemo());

		entity.setOperator(gold.getOperator());

		entity.setType(gold.getType());

		entity.setMember(memberService.find(memberId));
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            goldService.save(entity);
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
            goldService.delete(ids);
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
		types.add(new MapEntity("recharge","充值"));
		types.add(new MapEntity("transfer","支付"));
		types.add(new MapEntity("transaction","退款"));
		types.add(new MapEntity("history","收益"));
		types.add(new MapEntity("reward","打赏"));
		model.addAttribute("types",types);

		model.addAttribute("members",memberService.findAll());

		model.addAttribute("data",goldService.find(id));

		return "/admin/gmGold/edit";
	}

	
	/**
     * 更新
     */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
	public Message update(Gold gold, Long memberId){
		Gold entity = goldService.find(gold.getId());
		
		entity.setCreateDate(gold.getCreateDate());

		entity.setModifyDate(gold.getModifyDate());

		entity.setBalance(gold.getBalance());

		entity.setCredit(gold.getCredit());

		entity.setDebit(gold.getDebit());

		entity.setDeleted(gold.getDeleted());

		entity.setGame(gold.getGame());

		entity.setMemo(gold.getMemo());

		entity.setOperator(gold.getOperator());

		entity.setType(gold.getType());

		entity.setMember(memberService.find(memberId));
		
		if (!isValid(entity)) {
            return Message.error("admin.data.valid");
        }
        try {
            goldService.update(entity);
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
	public Message list(Date beginDate, Date endDate, Gold.Type type, Pageable pageable, ModelMap model) {
		ArrayList<Filter> filters = (ArrayList<Filter>) pageable.getFilters();
		if (type!=null) {
			Filter typeFilter = new Filter("type", Filter.Operator.eq, type);
			filters.add(typeFilter);
		}

		Page<Gold> page = goldService.findPage(beginDate,endDate,pageable);
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
		return "/admin/gmGold/view/memberView";
	}



}